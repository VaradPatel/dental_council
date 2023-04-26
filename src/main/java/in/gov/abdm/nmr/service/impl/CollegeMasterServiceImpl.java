package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CollegeMasterTo;
import in.gov.abdm.nmr.mapper.ICollegeMasterMapper;
import in.gov.abdm.nmr.repository.ICollegeMasterRepository;
import in.gov.abdm.nmr.service.ICollegeMasterService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
public class CollegeMasterServiceImpl implements ICollegeMasterService {

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired
    private IRequestCounterService requestCounterService;
    @Autowired
    private ICollegeMasterRepository collegeMaster;
    @Autowired
    private ICollegeMasterMapper collegeMasterMapper;

    @Override
    public List<CollegeMasterTo> getCollegesByStateId(BigInteger stateId) {
        return collegeMasterMapper.collegeMasterTo(stateId != null ? collegeMaster.getCollegesByStateId(stateId) : collegeMaster.getColleges());
    }
}
