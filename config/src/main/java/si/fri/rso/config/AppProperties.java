package si.fri.rso.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("app-properties")
public class AppProperties {

    @ConfigValue(value = "amazon-rekognition.access-key", watch = true)
    private String amazonRekognitionAccessKey;

    @ConfigValue(value = "amazon-rekognition.secret-key", watch = true)
    private String amazonRekognitionSecretKey;

    public String getAmazonRekognitionSecretKey() {
        return amazonRekognitionSecretKey;
    }

    public void setAmazonRekognitionSecretKey(String amazonRekognitionSecretKey) {
        this.amazonRekognitionSecretKey = amazonRekognitionSecretKey;
    }

    public String getAmazonRekognitionAccessKey() {
        return amazonRekognitionAccessKey;
    }

    public void setAmazonRekognitionAccessKey(String amazonRekognitionAccessKey) {
        this.amazonRekognitionAccessKey = amazonRekognitionAccessKey;
    }
}

