package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileUploadException extends ABDMBaseException {
    private static final long serialVersionUID = 1L;

    public InvalidFileUploadException() {
        super(NMRError.INVALID_FILE_UPLOAD.getCode(), NMRError.INVALID_FILE_UPLOAD.getMessage(), HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidFileUploadException(String message) {
        super(NMRError.INVALID_FILE_UPLOAD.getCode(), message, HttpStatus.BAD_REQUEST.toString());
    }

    public InvalidFileUploadException(String message, HttpStatus code) {
        super(code.toString(), message, HttpStatus.BAD_REQUEST.toString());
    }


    public InvalidFileUploadException(String code, String message, String httpStatus) {
        super(code, message, httpStatus);
    }
}