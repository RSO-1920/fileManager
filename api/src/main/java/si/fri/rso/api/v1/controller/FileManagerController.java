package si.fri.rso.api.v1.controller;


import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

@ApplicationScoped
@Path("/upload")

// prebereš file, pošlješ penci, pošlješ njegov info zorotu
public class FileManagerController {

    public File getfile(){

        //Your local disk path where you want to store the file
        String uploadedFileLocation = "C:\\Users\\urosz\\Downloads\\playlist.m3u8" ;
        System.out.println(uploadedFileLocation);
        // save it
        File  objFile=new File(uploadedFileLocation);

        if(objFile.exists())
        {
            return objFile;
        }
        return null;
    }



    @POST
    @Path("postFile")
    public void postFile() {
        // String contentType = "multipart/mixed";
        File newFile = getfile();
        MultiPart multiPart = null;

        //TODO: PREBERI SERVER URL IZ CONFIGA
        String serverURL = "http://localhost:8088/v1/upload/getter";
        //TODO: KLIČI SERVICE S TRY_CATCH
        try {
            Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            WebTarget server = client.target(serverURL);

            multiPart = new MultiPart();
            FileDataBodyPart zipBodyPart = new FileDataBodyPart("file", newFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
            multiPart.bodyPart(zipBodyPart);

            MediaType contentType = MediaType.MULTIPART_FORM_DATA_TYPE;
            contentType = Boundary.addBoundary(contentType);

            Response response = server.request(MediaType.APPLICATION_JSON_TYPE)
                    .post( Entity.entity(multiPart, contentType) );
            if (response.getStatus() == 200) {
                String respnse = response.readEntity(String.class);
                System.out.println(respnse);
            } else {

                System.out.println("Response is not ok: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("Exception has occured"+ e.getMessage());
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


    @POST
    @Path("getter")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@NotEmpty
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) {

        System.out.println("\n\n..CandidateServlet.uploadImage()");
        System.out.println(fileDetails.getFileName());

        writeToFile(uploadedInputStream, fileDetails.getFileName());

        String output = "File uploaded to : " + fileDetails.getFileName();

        return Response.ok("File uploaded = " + fileDetails.getFileName()).build();
    }


    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {
        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



