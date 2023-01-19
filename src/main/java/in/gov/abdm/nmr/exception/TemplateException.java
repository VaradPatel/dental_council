package in.gov.abdm.nmr.exception;

public class TemplateException extends Exception{
    private static final long serialVersionUID =  -5363377902805482437L;

    public TemplateException(){
    }
    public TemplateException(String message){
        super(message);
    }
    public TemplateException(Throwable throwable){
        super(throwable);
    }

}
