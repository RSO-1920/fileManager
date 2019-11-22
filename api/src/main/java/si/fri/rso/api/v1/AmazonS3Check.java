package si.fri.rso.api.v1;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class AmazonS3Check implements HealthCheck {

    private static final String url = "https://aws.amazon.com/s3/";

    private static final Logger LOG = Logger.getLogger(AmazonS3Check.class.getSimpleName());

    @Override
    public HealthCheckResponse call() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.named(AmazonS3Check.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
        }
        return HealthCheckResponse.named(AmazonS3Check.class.getSimpleName()).down().build();
    }
}
