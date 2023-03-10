package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class TemplateException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public TemplateException() {
        super(NMRError.TEMPLATE_NOT_FOUND.getCode(), NMRError.TEMPLATE_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.toString());
    }

    public TemplateException(String message) {
        super(NMRError.TEMPLATE_NOT_FOUND.getCode(), message, HttpStatus.NOT_FOUND.toString());
    }

    public TemplateException(String code, String message) {
        super(code, message, HttpStatus.NOT_FOUND.toString());
    }

    public TemplateException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }

    public TemplateException(Throwable throwable) {
        super(String.valueOf(throwable));
    }

}