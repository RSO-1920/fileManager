package si.fri.rso.api.v1.controller;

import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import si.fri.rso.config.FileManagerConfigProperties;
import si.fri.rso.services.FileManagerBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

@ApplicationScoped
@Path("/file")

public class FileManagerController {
    @Inject
    private FileManagerConfigProperties fileManagerConfigProperties;
    @Inject
    private FileManagerBean fileManagerBean;

    private Client httpClient;

    private File getfile(){
        //Your local disk path where you want to store the file
        String uploadedFileLocation = "playlist.m3u8" ;
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
        System.out.println("Configuration properties: ");
        System.out.println("Catalog api: " + this.fileManagerConfigProperties.getCatalogApiUrl());
        System.out.println("FileStorage api: " + this.fileManagerConfigProperties.getFileStorageApiUrl());
        System.out.println();
        fileManagerBean.uploadingNewFile();
        System.out.println();

        File newFile = getfile();

    }


    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@NotEmpty
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) {

        boolean isSuccess = fileManagerBean.uploadFile(uploadedInputStream, fileDetails);

        if (isSuccess) {
            return Response.ok("File uploaded = " + fileDetails.getFileName()).build();
        }

        return Response.status(500).entity("Error uploading file.. some of the service unreachable").build();
    }




    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response DeleteFile() {
        Integer FileId = 1;
        System.out.println("Deleting file: "+ FileId);

        //POST KLIC penca
        if (deleteFile(FileId, "storage")){
            //POST KLIC zoro
            if (deleteFile(FileId, "catalog")){
                return Response.ok("File Deleted! ").build();
            }
            else{
                return Response.ok("To ni ok!! \n Zoro ne vrne true").build();
            }
        }
        else{
            return Response.ok("To ni ok!! \n Penca ne vrne true").build();
        }

    }

    private Boolean deleteFile(Integer FileId, String path) {
        String target = "";
        if (path.equals("storage")){
            target = this.fileManagerConfigProperties.getFileStorageApiUrl();
        }
        else if (path.equals("catalog")){
            target = this.fileManagerConfigProperties.getCatalogApiUrl();
        }
        System.out.println("Target je "+ path);
        System.out.println("Ciljam: "+ target);

        try{
            Response success = httpClient
                    .target(target)
                    .request(MediaType.APPLICATION_JSON_TYPE).post( Entity.entity(FileId, MediaType.APPLICATION_JSON_TYPE));

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



