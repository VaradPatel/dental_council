package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.ActionRequestTo;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IActionService;
import org.springframework.web.bind.annotation.*;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@RestController
@RequestMapping(ACTION_REQUEST_URL)
public class ActionController {

    private IActionService iActionService;

    public ActionController(IActionService iActionService) {
        this.iActionService = iActionService;
    }

    @PostMapping(SUSPENSION_REQUEST_URL)
    public String suspensionHealthProfessional(@RequestBody ActionRequestTo actionRequestTo) throws WorkFlowException {
        return iActionService.suspendRequest(actionRequestTo);
    }

    @PostMapping(REACTIVATE_REQUEST_URL)
    public String reactivateHealthProfessional(@RequestBody ActionRequestTo actionRequestTo) throws WorkFlowException {
        return iActionService.reactiveRequest(actionRequestTo);
    }
}
