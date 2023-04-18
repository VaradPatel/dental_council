package in.gov.abdm.nmr.exception;

import org.springframework.http.HttpStatus;

public class WorkFlowException extends ABDMBaseException {
    private static final long serialVersionUID = -5363377902805482437L;

    public WorkFlowException() {
        super(NMRError.WORK_FLOW_EXCEPTION.getCode(), NMRError.WORK_FLOW_EXCEPTION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public WorkFlowException(String message) {
        super(NMRError.WORK_FLOW_EXCEPTION.getCode(), message, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    public WorkFlowException(String code, HttpStatus message) {
        super(code, message.toString(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}