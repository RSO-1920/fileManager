package si.fri.rso.api.v1.controller;

import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import si.fri.rso.config.FileManagerConfigProperties;
import si.fri.rso.lib.responses.NewFileMetadata;
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
    private FileManagerBean fileManagerBean;

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@NotEmpty
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails,
                               @FormDataParam("integerUser") Integer userId,
                               @FormDataParam("integerChannel") Integer channelId) {
        if (userId == null || channelId == null){
            return Response.status(500).entity("Error uploading file.. user or channel is null").build();
        }
        Integer [] userChannel = new Integer[] {userId, channelId};

        boolean isSuccess = fileManagerBean.uploadFile(uploadedInputStream, fileDetails, userChannel);

        if (isSuccess) {
            return Response.ok(isSuccess).build();
        }
        return Response.status(500).entity("Error uploading file.. some of the service unreachable").build();
    }


    @DELETE
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response DeleteFile() {
        Integer FileId = 1;
        System.out.println("Deleting file: "+ FileId);
        //POST KLIC penca
        if (fileManagerBean.deleteFile(FileId, "storage")){
            //POST KLIC zoro
            if (fileManagerBean.deleteFile(FileId, "catalog")){
                return Response.ok("File Deleted! ").build();
            }
            else{
                return Response.status(402, "To ni ok!! \n Zoro ne vrne true").build();
            }
        }
        else{
            return Response.status(402, "To ni ok!! \n Penca ne vrne true").build();
        }
    }
}



