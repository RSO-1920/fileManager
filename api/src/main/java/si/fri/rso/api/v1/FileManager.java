package si.fri.rso.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import com.kumuluz.ee.health.HealthRegistry;
import com.kumuluz.ee.health.enums.HealthCheckType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import si.fri.rso.api.v1.controller.FileManagerController;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@RegisterService(value = "rso1920-file-manager")
@ApplicationPath("/v1")
@OpenAPIDefinition(info = @Info(title = "File manager", version = "v1", contact = @Contact(), license = @License(),
        description = "Manager for file deletion, upload.. Calling other services"), servers = @Server(url ="http://localhost:8089/v1"))
public class FileManager extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(MultiPartFeature.class);
        resources.add(FileManagerController.class);
        HealthRegistry.getInstance().register(AmazonS3Check.class.getSimpleName(), new AmazonS3Check(), HealthCheckType.LIVENESS);
        return resources;
    }
}
