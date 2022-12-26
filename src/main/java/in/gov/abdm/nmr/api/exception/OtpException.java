package in.gov.abdm.nmr.api.exception;

public class OtpException extends Exception{
    private static final long serialVersionUID =  -5363377902805482437L;

    public OtpException(){
    }
    public OtpException(String message){
        super(message);
    }
    public OtpException(Throwable throwable){
        super(throwable);
    }

}
