package docshare.com.service.exception;

public class AwsException extends RuntimeException{
    public AwsException(String message) {
        super("Error While Interacting With Cloud and File");
        System.out.println(message);
    }
}
