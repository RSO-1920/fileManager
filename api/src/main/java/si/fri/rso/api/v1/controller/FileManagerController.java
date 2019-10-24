package si.fri.rso.api.v1.controller;


import java.io.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;

@ApplicationScoped
@Path("/upload")

// prebereš file, pošlješ penci, pošlješ njegov info zorotu
public class FileManagerController {

    public File getfile(){

        //Your local disk path where you want to store the file
        String uploadedFileLocation = "C:\\Users\\Jan\\Downloads\\SU_63150214.pdf" ;
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
        File newFile = getfile();
        MultiPart multiPart = null;

        //TODO: PREBERI SERVER URL IZ CONFIGA
        String serverURL = "http://127.0.0.1:8088/v1/upload/getter";
        //TODO: KLIČI SERVICE S TRY_CATCH
        try {
            Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            WebTarget server = client.target(serverURL);
            multiPart = new MultiPart();

            FileDataBodyPart zipBodyPart = new FileDataBodyPart("File", newFile,
                    MediaType.APPLICATION_OCTET_STREAM_TYPE);

            multiPart.bodyPart(zipBodyPart);

            Response response = server.request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));
            if (response.getStatus() == 200) {
                String respnse = response.readEntity(String.class);
                System.out.println(respnse);
            } else {

                System.out.println("Response is not ok");
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

    @GET
    @Path("getter")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFile(
        @FormDataParam("file") InputStream uploadedInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetails) {

        System.out.println(fileDetails.getFileName());

        String uploadedFileLocation = "C:\\Users\\Jan\\Documents\\fileManager\\izpis.pdf";

        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);

        String output = "File uploaded to : " + uploadedFileLocation;
        return Response.status(Response.Status.OK).entity("OKEEEJ").build();
    }

// save uploaded file to new location




}



