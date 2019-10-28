package si.fri.rso.services;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import si.fri.rso.config.FileManagerConfigProperties;
import si.fri.rso.lib.responses.NewFileMetadata;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

@ApplicationScoped
public class FileManagerBean {

    @Inject RequestSenderBean requestSenderBean;

    @Inject FileManagerConfigProperties fileManagerConfigProperties;

    private Client httpClient;

    @PostConstruct
    private void init(){
        this.httpClient = ClientBuilder.newClient();
    }

    public boolean uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetails,
                              Integer [] userChannel) {
        File file = new File(fileDetails.getFileName());

        try {
            FileUtils.copyInputStreamToFile(uploadedInputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath =  requestSenderBean.sendFileToUploadOnS3(file);
        //"c://bla//file.jpg"

        String [] nameType = fileDetails.getFileName().split("\\.");


        String fileType = nameType[nameType.length-1];
        Integer user = userChannel[0];
        Integer channel = userChannel[1];

        boolean isDeleted = file.delete();
        if(!isDeleted){
           System.out.println("File was not deleted!");
        }

        if (filePath != null){
            NewFileMetadata newFile = new NewFileMetadata(filePath, fileDetails.getFileName(), fileType, user, channel);
            boolean isSaved = saveMetadata(newFile);
            return true;
        }
        return false;
    }

    private boolean saveMetadata(NewFileMetadata newFile){

        try{
            Response success = httpClient
                    .target(fileManagerConfigProperties.getCatalogApiUrl() + "v1/file")
                    .request(MediaType.APPLICATION_JSON_TYPE).post( Entity.entity(newFile, MediaType.APPLICATION_JSON_TYPE));

            if (success.getStatus() == 200) {
                System.out.println("Meta data about file was uploaded");
                return true;
            } else {
                System.out.println("Meta data about file was NOT uploaded");
                return false;
            }
        }catch (WebApplicationException | ProcessingException e) {
            e.printStackTrace();
            System.out.println("api for metadata upload failed miserably");
            return false;
        }
    }

    //  1. upload zoro
    //  2. upload penca
    //  3. delete zoro
    //  4. delete penca
    public Boolean deleteFile(Integer FileId, String path) {
        String target = "";
        if (path.equals("storage")){
            target = this.fileManagerConfigProperties.getFileStorageApiUrl() + FileId;
        }
        else if (path.equals("catalog")){
            target = this.fileManagerConfigProperties.getDeletecatalogApiUrl() +
                    "/" +
                    FileId;
        }
        System.out.println("Target je "+ path);
        System.out.println("Ciljam: "+ target);
        try{
            Response success = httpClient
                    .target(target)
                    .request(MediaType.APPLICATION_JSON_TYPE).delete();

            if (success.readEntity(String.class).equals("true")) {
                System.out.println("S3 deleted a file");
                //return Response.ok("File delition success").build();
                return true;
            } else {
                System.out.println("File delition failed");
                //return Response.ok("File delition file").build();
                return false;
            }
        }catch (WebApplicationException | ProcessingException e) {
            e.printStackTrace();
            System.out.println("api for S3 not reachable");
            return false;
        }
    }

}

// TODO maybe good idea to create one service specialized for calling other Microservices
