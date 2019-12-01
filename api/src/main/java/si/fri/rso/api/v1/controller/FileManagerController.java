package si.fri.rso.api.v1.controller;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.Histogram;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.glassfish.jersey.media.multipart.*;
import si.fri.rso.lib.responses.CatalogFileMetadata;
import si.fri.rso.lib.responses.ChannelBucketName;
import si.fri.rso.services.FileManagerBean;
import si.fri.rso.services.RequestSenderBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.UUID;

@Log
@ApplicationScoped
@Path("/file")
public class FileManagerController {

    @Inject
    private FileManagerBean fileManagerBean;

    @Inject
    private RequestSenderBean requestSenderBean;

    @Inject
    HttpServletRequest requestheader;

    @Inject
    @Metric(name = "upload_file_histogram")
    Histogram uploadFilehistogram;

    @POST
    @Timed(name = "file_manager_time_upload")
    @Counted(name = "file_manager_counted_upload")
    @Metered(name = "file_manager_metered_upload")
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@NotEmpty
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails,
                               @FormDataParam("integerUser") Integer userId,
                               @FormDataParam("integerChannel") Integer channelId) {

        try {
            uploadFilehistogram.update(uploadedInputStream.read());
        } catch (Exception e) {
            System.out.println("histogram failed");
        }

        if (userId == null || channelId == null){
            return Response.status(500).entity("Error uploading file.. user or channel is null").build();
        }
        Integer [] userChannel = new Integer[] {userId, channelId};

        String requestHeader = requestheader.getHeader("uniqueRequestId");

        boolean isSuccess = fileManagerBean.uploadFile(uploadedInputStream, fileDetails, userChannel, requestHeader != null ? requestHeader : UUID.randomUUID().toString());

        if (isSuccess) {
            return Response.ok(isSuccess).build();
        }
        return Response.status(500).entity("Error uploading file.. some of the service unreachable").build();
    }

    @DELETE
    @Timed(name = "file_manager_time_delete")
    @Counted(name = "file_manager_counted_delete")
    @Metered(name = "file_manager_metered_delete")
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response DeleteFile(@QueryParam("fileId") Integer fileId, @QueryParam("channelId") Integer channelId) {
        System.out.println("FILEID: "+ fileId + "  CHANNELID: " + channelId);
        if (fileId == null || channelId == null){
            return Response.status(444, "File id od channel id was not given! ").build();
        }

        String requestHeader = requestheader.getHeader("uniqueRequestId");
        System.out.println("Deleting file: "+ fileId);
        requestHeader = requestHeader != null ? requestHeader : UUID.randomUUID().toString();

        ChannelBucketName bucket = this.requestSenderBean.getBucketName(channelId, requestHeader);
        CatalogFileMetadata file = this.requestSenderBean.getFileMetadata(fileId, requestHeader);

        if (bucket== null || file == null || bucket.getBucketName() == null || file.getFileName() == null) {
            return Response.status(400, "bucket and file name for given ids not found").build();
        }

        if (fileManagerBean.deleteFile(fileId, "catalog",requestHeader, bucket.getBucketName(), file.getFileName())){
            if (fileManagerBean.deleteFile(fileId, "storage", requestHeader, bucket.getBucketName(), file.getFileName())){
                return Response.ok("File Deleted!").build();
            }
            else{
                return Response.status(400, "Error while deleting file in S3 storage!").build();
            }
        }
        else{
            return Response.status(400, "Error while deleting file metadata!").build();
        }
    }
}



