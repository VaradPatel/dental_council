package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.NextGroupTO;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.Group;
import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.entity.WorkFlowAudit;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IWorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class WorkFlowServiceImpl implements IWorkFlowService {

    /**
     * Injecting IWorkFlowRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IWorkFlowRepository iWorkFlowRepository;

    /**
     * Injecting IWorkFlowRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IWorkFlowAuditRepository iWorkFlowAuditRepository;

    /**
     * Injecting INMRWorkFlowConfigurationRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private INMRWorkFlowConfigurationRepository inmrWorkFlowConfigurationRepository;

    @Autowired
    private IApplicationTypeRepository iApplicationTypeRepository;

    @Autowired
    private IGroupRepository iGroupRepository;

    @Autowired
    private IActionRepository iActionRepository;

    @Autowired
    private IHpProfileRepository iHpProfileRepository;

    @Autowired
    private IWorkFlowStatusRepository iWorkFlowStatusRepository;

    @Override
    public void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException {

        INextGroup iNextGroup=inmrWorkFlowConfigurationRepository.getNextGroup(requestTO.getApplicationTypeId(), requestTO.getActorId(), requestTO.getActionId());

        if(iNextGroup != null){
            WorkFlow workFlow=iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
            if(workFlow==null){
                WorkFlow newWorkFlow = buildNewWorkFlow(requestTO, iNextGroup);
                iWorkFlowRepository.save(newWorkFlow);
            }else{
                workFlow.setUpdatedAt(null);
                workFlow.setAction(iActionRepository.findById(requestTO.getActionId()).get());
                workFlow.setPreviousGroup(workFlow.getCurrentGroup());
                workFlow.setCurrentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null) ;
                workFlow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
            }
            iWorkFlowAuditRepository.save(buildNewWorkFlowAudit(requestTO,iNextGroup));
            // Hp Profile status updation needs to be done for terminal operations
        }
        else {
            throw new WorkFlowException("Next Group Not Found", HttpStatus.BAD_REQUEST);
        }

    }

    private WorkFlow buildNewWorkFlow(WorkFlowRequestTO requestTO, INextGroup iNextGroup) {

        Group actorGroup = iGroupRepository.findById(requestTO.getActorId()).get();

        return WorkFlow.builder().requestId(requestTO.getRequestId())
                .applicationType(iApplicationTypeRepository.findById(requestTO.getApplicationTypeId()).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(requestTO.getActionId()).get())
                .hpProfile(iHpProfileRepository.findById(requestTO.getHpProfileId()).get())
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iGroupRepository.findById(iNextGroup.getAssignTo()).get())
                .build();
    }

    private WorkFlowAudit buildNewWorkFlowAudit(WorkFlowRequestTO requestTO, INextGroup iNextGroup) {

        Group actorGroup = iGroupRepository.findById(requestTO.getActorId()).get();
        return WorkFlowAudit.builder().requestId(requestTO.getRequestId())
                .applicationType(iApplicationTypeRepository.findById(requestTO.getApplicationTypeId()).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(requestTO.getActionId()).get())
                .hpProfile(iHpProfileRepository.findById(requestTO.getHpProfileId()).get())
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null)
                .build();
    }

}
