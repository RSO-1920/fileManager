package si.fri.rso.services;

import org.glassfish.jersey.media.multipart.Boundary;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import si.fri.rso.config.FileManagerConfigProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

@ApplicationScoped
public class RequestSenderBean {

    @Inject
    FileManagerConfigProperties fileManagerConfigProperties;

    String sendFileToUploadOnS3(File newFile){
        MultiPart multiPart = null;
        String serverURL = fileManagerConfigProperties.getFileStorageApiUrl();

        System.out.println("Request url: " + serverURL);
        try {
            Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            WebTarget server = client.target(serverURL);

            multiPart = new MultiPart();
            FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("fileStream", newFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
            multiPart.bodyPart(fileDataBodyPart);

            MediaType contentType = MediaType.MULTIPART_FORM_DATA_TYPE;
            contentType = Boundary.addBoundary(contentType);

            Response response = server.request(MediaType.APPLICATION_JSON_TYPE).post( Entity.entity(multiPart, contentType) );
            if (response.getStatus() == 200) {
                String respnse = response.readEntity(String.class);
                System.out.println("OK: " +  respnse);
                // TODO: UREDI RESPNSE - kaj vrne penca?
                respnse = "KLJUC";
                return respnse;
            } else {

                System.out.println("Response is not ok: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception has occured"+ e.getMessage());
            return null;
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

}
