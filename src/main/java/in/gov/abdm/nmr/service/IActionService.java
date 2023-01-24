package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ActionRequestTo;
import in.gov.abdm.nmr.exception.WorkFlowException;

public interface IActionService {

    String suspendRequest(ActionRequestTo actionRequestTo) throws WorkFlowException;

    String reactiveRequest(ActionRequestTo actionRequestTo) throws WorkFlowException;
}
