package in.gov.abdm.nmr.exception;

public class ABDMBaseException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String code;
    private final String message;
    private final String httpStatus;
    
    public ABDMBaseException() {
        super();
        this.message = null;
        this.code = null;
        this.httpStatus = null;
    }
    public ABDMBaseException(String message) {
        super(message);
        this.message = message;
        this.code = null;
        this.httpStatus = null;
    }
    public ABDMBaseException(String code, String message) {
        super(code + message);
        this.code = code;
        this.message = message;
        this.httpStatus = null;
    }
    public ABDMBaseException(String code, String message, String httpStatus) {
        super(code + message);
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    
    public String getCode(){
        return this.code;
    }
    @Override
    public String getMessage(){
        return this.message;
    }
    public String getHttpStatus(){
        return this.httpStatus;
    }
}
