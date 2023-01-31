package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
import in.gov.abdm.nmr.dto.FetchTrackApplicationRequestTO;
import in.gov.abdm.nmr.dto.FetchTrackApplicationResponseTO;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.FetchTrackApplicationMapper;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetailsMapper;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsRepository;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@Service
public class FetchSpecificDetailsServiceImpl implements IFetchSpecificDetailsService {

    /**
     * Injecting IFetchSpecificDetailsRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchSpecificDetailsRepository iFetchSpecificDetailsRepository;

    /**
     * Mapper Interface to transform the IFetchSpecificDetails Bean
     * to the FetchSpecificDetailsResponseTO Bean transferring its contents
     */
    @Autowired
    private IFetchSpecificDetailsMapper iFetchSpecificDetailsMapper;

    @Autowired
    private FetchTrackApplicationMapper fetchTrackApplicationMapper;

    @Override
    public List<FetchSpecificDetailsResponseTO> fetchSpecificDetails(String groupName, String applicationType, String workFlowStatus) throws InvalidRequestException {
        validateGroupName(groupName);
        validateApplicationType(applicationType);
        validateWorkFlowStatus(workFlowStatus);

        return fetchDetailsForListingByStatus(groupName, applicationType, workFlowStatus)
                .stream()
                .map(fetchSpecificDetails-> {
                    FetchSpecificDetailsResponseTO fetchSpecificDetailsResponseTO=iFetchSpecificDetailsMapper.toFetchSpecificDetailsResponseTO(fetchSpecificDetails);

                    if(Group.COLLEGE_ADMIN.getDescription().equals(fetchSpecificDetails.getGroupName()) ||
                            Group.COLLEGE_DEAN.getDescription().equals(fetchSpecificDetails.getGroupName()) ||
                                    Group.COLLEGE_REGISTRAR.getDescription().equals(fetchSpecificDetails.getGroupName())){
                        fetchSpecificDetailsResponseTO.setCollegeVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if(Group.SMC.getDescription().equals(fetchSpecificDetails.getGroupName())){
                        fetchSpecificDetailsResponseTO.setCouncilVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if(Group.NMC.getDescription().equals(fetchSpecificDetails.getGroupName())){
                        fetchSpecificDetailsResponseTO.setNMCVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if(fetchSpecificDetails.getDateOfSubmission()!=null) {
                        long diffInMillis = Math.abs(new Date().getTime() - fetchSpecificDetails.getDateOfSubmission().getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                        fetchSpecificDetailsResponseTO.setPendency(BigInteger.valueOf(diff));
                    }
                    return fetchSpecificDetailsResponseTO;
                })
                .toList();
    }

    //new impl
    @Override
    public List<FetchTrackApplicationResponseTO> fetchTrackApplicationDetails(FetchTrackApplicationRequestTO requestTO) {
        List<FetchTrackApplicationResponseTO> list = new ArrayList<>();
        Pageable pagination = PageRequest.of(
                requestTO.getPage(),
                requestTO.getSize()> 500 ? 500 : requestTO.getSize(),
                Sort.by(requestTO.getSortBy()));
        List<Tuple> queryResult = iFetchSpecificDetailsRepository.fetchTrackApplicationDetails(iFetchSpecificDetailsRepository.getHpProfileIds(requestTO.getHpId()), pagination);
        FetchTrackApplicationResponseTO response;
        for (Tuple tuple : queryResult) {
            response = new FetchTrackApplicationResponseTO();
            response.setRequestId(tuple.get("request_id", String.class));
            response.setApplicationTypeId(tuple.get("application_type_id", BigInteger.class));
            response.setCreatedAt(tuple.get("created_at", Date.class));
            response.setWorkFlowStatusId(tuple.get("work_flow_status_id", BigInteger.class));
            response.setCurrentGroupId(tuple.get("current_group_id", BigInteger.class));
            response.setPendencyDays(tuple.get("pendency_days", BigDecimal.class));
            list.add(response);
        }
        return list;
    }

    private List<IFetchSpecificDetails> fetchDetailsForListingByStatus(String groupName, String applicationType, String workFlowStatus){
        if(WorkflowStatus.APPROVED.getDescription().equals(workFlowStatus)){
            return iFetchSpecificDetailsRepository.fetchDetailsWithApprovedStatusForListing(groupName,applicationType,workFlowStatus);
        } else if (WorkflowStatus.PENDING.getDescription().equals(workFlowStatus)) {
            return iFetchSpecificDetailsRepository.fetchDetailsWithPendingStatusForListing(groupName,applicationType,workFlowStatus);
        }
        return iFetchSpecificDetailsRepository.fetchDetailsForListing(groupName,applicationType,workFlowStatus);
    }
    private void validateGroupName(String groupName) throws InvalidRequestException {
        if(groupName==null || Arrays.stream(Group.values()).noneMatch(t -> t.getDescription().equals(groupName))){
            throw new InvalidRequestException(INVALID_GROUP);
        }
    }

    private void validateApplicationType(String applicationType) throws InvalidRequestException {
        if(applicationType==null || Arrays.stream(ApplicationType.values()).noneMatch(t -> t.getDescription().equals(applicationType))){
            throw new InvalidRequestException(INVALID_APPLICATION_TYPE);
        }
    }

    private void validateWorkFlowStatus(String workFlowStatus) throws InvalidRequestException {
        if(workFlowStatus==null || Arrays.stream(WorkflowStatus.values()).noneMatch(t -> t.getDescription().equals(workFlowStatus))){
            throw new InvalidRequestException(INVALID_WORK_FLOW_STATUS);
        }
    }


}
