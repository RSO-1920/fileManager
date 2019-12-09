package si.fri.rso.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import si.fri.rso.config.AppProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class AWSClient {

    @Inject
    AppProperties appProperties;

    AmazonRekognition rekognitionClient;

    @PostConstruct
    void init() {
        AWSCredentials credentials;
        try {
            credentials = new BasicAWSCredentials(
                    appProperties.getAmazonRekognitionAccessKey(),
                    appProperties.getAmazonRekognitionSecretKey());
        } catch (Exception e) {
            throw new AmazonClientException("Cannot initialise the credentials.", e);
        }

        rekognitionClient = AmazonRekognitionClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();
    }

    public ArrayList<String> ImageLabels(String imageName, String bucketName){
        System.out.println("BUCKET: " + imageName + " KEY: " + bucketName);
        ArrayList<String> imageLabels = new ArrayList<String>();
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withS3Object(new S3Object().withName(imageName).withBucket(bucketName)))
                .withMaxLabels(10).withMinConfidence(80F);

        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();
            System.out.println("Detected labels for " + imageName + "\n");
            for (Label label : labels) {
                System.out.println("Label: " + label.getName());
                imageLabels.add(label.getName());
            }
            return imageLabels;
        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
