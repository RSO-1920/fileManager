package si.fri.rso.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import si.fri.rso.config.FileManagerConfigProperties;
import si.fri.rso.lib.responses.ChannelBucketName;
import si.fri.rso.lib.responses.NewFileMetadata;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@RequestScoped
public class FileManagerBean {

    @Inject RequestSenderBean requestSenderBean;

    @Inject FileManagerConfigProperties fileManagerConfigProperties;

    @Inject
    @DiscoverService(value = "rso1920-catalog")
    private Optional<String> fileMetadataUrl;

    @Inject
    @DiscoverService(value = "rso1920-fileStorage")
    private Optional<String> fileStorageUrl;

    @Inject
    private  AWSClient awsClient;

    private Client httpClient;

    @PostConstruct
    private void init(){
        this.httpClient = ClientBuilder.newClient();
    }

    public boolean uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetails,
                              String userId, Integer channelId, String requestUniqueID) {
        File file = new File(fileDetails.getFileName());

        try {
            FileUtils.copyInputStreamToFile(uploadedInputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String [] nameType = fileDetails.getFileName().split("\\.");

        String fileType = nameType[nameType.length-1];
        String user = userId;
        Integer channel = channelId;

        ChannelBucketName channelBucketName = this.requestSenderBean.getBucketName(channel, requestUniqueID);

        System.out.println("channel bucket name: " + channelBucketName.getBucketName());

        boolean isFileUploaded = requestSenderBean.sendFileToUploadOnS3(file, channelBucketName.getBucketName(), fileDetails.getFileName(), requestUniqueID);

        if (!isFileUploaded) {
            System.out.println("NO UPLOADED");
            return false;
        }

        ArrayList<String> filelabels = null;
        if (fileType.toLowerCase().equals("jpg") || fileType.toLowerCase().equals("png")) {
            filelabels = awsClient.ImageLabels(fileDetails.getFileName(), channelBucketName.getBucketName());
            System.out.println(Arrays.toString(filelabels.toArray()));
        }

        boolean isDeleted = file.delete();
        if(!isDeleted){
           System.out.println("File was not deleted!");
        }
        String filePath =  channelBucketName.getBucketName() + "/" + fileDetails.getFileName(); //
        NewFileMetadata newFile = new NewFileMetadata(filePath, fileDetails.getFileName(), fileType, user, channel, filelabels);
        System.out.println("new file metadata upload");
        return requestSenderBean.saveMetadata(newFile, requestUniqueID);
    }

    public boolean deleteFile(Integer FileId, String path, String requestUniqueID, String bucketName, String fileName) {
        String target = "";
        if (path.equals("storage")){
            System.out.println("STORAGE DELETION");
            System.out.println("bucket name: " + bucketName + " filename: " + fileName);

            if (!this.fileStorageUrl.isPresent()) {
                return false;
            }

            target = this.fileStorageUrl.get() + this.fileManagerConfigProperties.getDeleteFileStorageUri() + "/" + bucketName + "/" + fileName;
        }
        else if (path.equals("catalog")){
            if (!fileMetadataUrl.isPresent())
                return false;
            target = fileMetadataUrl.get() + fileManagerConfigProperties.getDeleteCatalogApiUri() +
                    "/" +
                    FileId;
        }

        System.out.println("Target je "+ path);
        System.out.println("Ciljam: "+ target);
        try{
            Response success = httpClient
                    .target(target)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("uniqueRequestId", requestUniqueID)
                    .delete();

            if (success.getStatus() == 200) {
                System.out.println("File deletion success: " + path);
                return true;
            } else {
                System.out.println("File delition failed: " + path);
                return false;
            }
        }catch (WebApplicationException | ProcessingException e) {
            // e.printStackTrace();
            System.out.println("api not reachable: " + path);
            return false;
        }


    }
}
