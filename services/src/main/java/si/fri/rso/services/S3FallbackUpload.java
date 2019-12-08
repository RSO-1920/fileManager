package si.fri.rso.services;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

public class S3FallbackUpload implements FallbackHandler<Boolean> {
    @Override
    public Boolean handle(ExecutionContext executionContext) {
        System.out.println("S3 storage not available... in fallback");
        return false;
    }
}
