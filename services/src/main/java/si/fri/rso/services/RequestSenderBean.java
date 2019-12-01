package si.fri.rso.services;

import com.google.gson.Gson;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.glassfish.jersey.media.multipart.Boundary;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import si.fri.rso.config.FileManagerConfigProperties;
import si.fri.rso.lib.responses.ChannelBucketName;
import si.fri.rso.lib.responses.DTO;
import si.fri.rso.lib.responses.NewFileMetadata;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@ApplicationScoped
public class RequestSenderBean {

    @Inject
    FileManagerConfigProperties fileManagerConfigProperties;

    private Client httpClient;

    @Inject
    @DiscoverService(value = "rso1920-catalog")
    private Optional<String> fileMetadataUrl;

    @Inject
    @DiscoverService(value = "rso1920-channels")
    private Optional<String> channelUrl;

    @Inject
    @DiscoverService(value = "rso1920-fileStorage")
    private Optional<String> fileStorageUrl;

    @PostConstruct
    private void init(){
        this.httpClient = ClientBuilder.newClient();
    }

    public boolean sendFileToUploadOnS3(File newFile, String bucketName, String fileName, String requestId){
        if (!fileStorageUrl.isPresent()) {
            System.out.println("file storage url not present");
            return false;
        }

        MultiPart multiPart = null;
        String serverURL = fileStorageUrl.get() + fileManagerConfigProperties.getFileStorageApiUri() + "/" + bucketName + "/" + fileName;

        System.out.println("Request url: " + serverURL);
        try {
            Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            WebTarget server = client.target(serverURL);

            multiPart = new MultiPart();
            FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("fileStream", newFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
            multiPart.bodyPart(fileDataBodyPart);

            MediaType contentType = MediaType.MULTIPART_FORM_DATA_TYPE;
            contentType = Boundary.addBoundary(contentType);

            Response response = server
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("uniqueRequestId", requestId)
                    .post( Entity.entity(multiPart, contentType) );
            if (response.getStatus() == 200) {
                String responseBody = response.readEntity(String.class);
                System.out.println("RESPONSE BODY: " +  responseBody);
                return true;
            } else {
                System.out.println("Response is not ok: " + response.getStatus());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception has occured"+ e.getMessage());
            return false;
        } finally {
            if (null != multiPart) {
                try {
                    multiPart.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean saveMetadata(NewFileMetadata newFile, String requestUniqueID){
        if (!fileMetadataUrl.isPresent())
            return false;
        System.out.println("Saving metadata: " + fileMetadataUrl.get() + fileManagerConfigProperties.getCatalogApiUri());
        System.out.println("REQUEST: " + requestUniqueID);
        try{
            Response success = httpClient
                    .target(fileMetadataUrl.get() + fileManagerConfigProperties.getCatalogApiUri())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("uniqueRequestId", requestUniqueID)
                    .post( Entity.entity(newFile, MediaType.APPLICATION_JSON_TYPE));

            if (success.getStatus() == 200) {
                System.out.println("Meta data about file was uploaded");
                return true;
            } else {
                System.out.println("Meta data about file was NOT uploaded");
                return false;
            }
        }catch (WebApplicationException | ProcessingException e) {
            // e.printStackTrace();
            System.out.println("api for metadata upload failed miserably");
            return false;
        }
    }

    public ChannelBucketName getBucketName(Integer channelId, String requestID) {
        if (!this.channelUrl.isPresent()) {
            return null;
        }

        System.out.println("GET bucket name url: " + this.channelUrl.get() +  fileManagerConfigProperties.getChannelUri() + "/" + channelId);

        try{
            Response success = httpClient
                    .target(this.channelUrl.get() +  fileManagerConfigProperties.getChannelUri() + "/" + channelId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("uniqueRequestId", requestID)
                    .get();

            if (success.getStatus() == 200) {

                Gson gson = new Gson();
                ChannelBucketName  bucketData = (ChannelBucketName) gson.fromJson(success.readEntity(String.class), DTO.class).getData();
                System.out.println(bucketData.getBucketName());

                return bucketData;
            } else {
                System.out.println("bucket with given id not found");
                return null;
            }
        }catch (WebApplicationException | ProcessingException e) {
            // e.printStackTrace();
            System.out.println("getting channel bucket id failed miserably");
            return null;
        }
    }

}
