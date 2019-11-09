package si.fri.rso.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import si.fri.rso.api.v1.controller.FileManagerController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@RegisterService(value = "rso1920-file-manager")
@ApplicationPath("/v1")
public class FileManager extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(MultiPartFeature.class);
        resources.add(FileManagerController.class);
        return resources;
    }
}
