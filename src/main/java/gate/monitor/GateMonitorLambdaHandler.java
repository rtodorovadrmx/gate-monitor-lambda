package gate.monitor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import gate.monitor.dagger.DaggerDependenciesComponent;
import gate.monitor.dagger.DependenciesComponent;
import gate.monitor.domain.FicsMessageEvent;
import gate.monitor.domain.LambdaResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;

import java.util.Set;


@AllArgsConstructor
public class GateMonitorLambdaHandler implements RequestHandler<FicsMessageEvent, LambdaResponse> {
    private static final String AWS_S3_BUCKET_NAME = "training-task-bucket";

    private static final DependenciesComponent dependenciesComponent = DaggerDependenciesComponent.create();

    private final Validator validator;

    public GateMonitorLambdaHandler(){
        this.validator = dependenciesComponent.getValidator();
    }
    @Override
    public LambdaResponse handleRequest(FicsMessageEvent ficsMessageEvent, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Message: " + ficsMessageEvent.toString());

        Set<ConstraintViolation<FicsMessageEvent>> violations = validator.validate(ficsMessageEvent);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            violations.forEach(violation -> {
                stringBuilder.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append('\n');
            });
            throw new ValidationException("Invalid data: " + stringBuilder.toString());
        }

        logger.log("Getting S3 client...");
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        logger.log("Writing event to S3 bucket...");
        PutObjectResult putObjectResult = s3Client.putObject(AWS_S3_BUCKET_NAME, ficsMessageEvent.getMessageId(), ficsMessageEvent.toString());
        logger.log("Event successfully stored in S3 bucket " + AWS_S3_BUCKET_NAME + ": " + putObjectResult.toString());

        return new LambdaResponse(ficsMessageEvent.getMessageId());
    }
}
