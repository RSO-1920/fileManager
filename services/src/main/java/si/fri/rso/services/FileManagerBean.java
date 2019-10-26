package si.fri.rso.services;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileManagerBean {

    @PostConstruct
    private void init(){
        // TODO init service logic if needed.. called only once
    }

    public void uploadingNewFile() {
        // TODO All the logic for new file upload.
        System.out.println("Call upload file Bean");
    }

}

// TODO maybe good idea to create one service specialized for calling other Microservices
