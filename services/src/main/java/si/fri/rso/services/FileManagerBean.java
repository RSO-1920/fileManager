package si.fri.rso.services;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import sun.security.util.IOUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;

@ApplicationScoped
public class FileManagerBean {

    @Inject RequestSenderBean requestSenderBean;

    @PostConstruct
    private void init(){
    }

    public void uploadingNewFile() {
        // TODO All the logic for new file upload.
        System.out.println("Call upload file Bean");
    }

    public Boolean uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetails) {

        File file = new File(fileDetails.getFileName());

        try {
            FileUtils.copyInputStreamToFile(uploadedInputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean isSuccessUpload =  requestSenderBean.sendFileToUploadOnS3(file);

        return true;
    }

}

// TODO maybe good idea to create one service specialized for calling other Microservices
