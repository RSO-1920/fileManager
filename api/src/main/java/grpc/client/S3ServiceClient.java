package grpc.client;

import com.kumuluz.ee.grpc.client.GrpcChannelConfig;
import com.kumuluz.ee.grpc.client.GrpcChannels;
import com.kumuluz.ee.grpc.client.GrpcClient;
import grpc.S3Grpc;
import grpc.S3Service;
import io.grpc.stub.StreamObserver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class S3ServiceClient {
    private S3Grpc.S3BlockingStub s3Stub;
    private final static Logger logger = Logger.getLogger(S3ServiceClient.class.getName());

    @PostConstruct
    public void init() {
        try {
            GrpcChannels clientPool = GrpcChannels.getInstance();
            GrpcChannelConfig config = clientPool.getGrpcClientConfig("client1");
            System.out.println("CONFIG GRPC");
            System.out.println(config.getAddress());
            System.out.println(config.getPort());
            GrpcClient client = new GrpcClient(config);
            s3Stub = S3Grpc.newBlockingStub(client.getChannel());
        } catch (SSLException e) {
            logger.warning(e.getMessage());
        }
    }

    public boolean deleteFile(String bucketName, String fileName){
        System.out.println("GRPC request: " + bucketName + "/" + fileName);
        S3Service.S3DeleteFileOnBucketRequest request = S3Service.S3DeleteFileOnBucketRequest.newBuilder()
                .setBucketname(bucketName)
                .setFilename(fileName)
                .build();
        try {
            S3Service.S3DeleteFileOnBucketResponse rs = s3Stub.deleteFileOnBucket(request);
            System.out.println("GRPC response");
            logger.info(rs.getResponsemsg());
            logger.info(String.valueOf(rs.getResponsestatus()));
            return rs.getResponsestatus();

        } catch (Exception e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return false;
        }

    }

}
