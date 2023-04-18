package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.mapper.ICollegeMasterMapper;
import in.gov.abdm.nmr.repository.ICollegeMasterRepository;
import in.gov.abdm.nmr.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CollegeServiceImpl implements ICollegeService, ICollegeMasterService {

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

    @Override
    public List<CollegeMasterTo> getCollegesByUniversity(BigInteger universityId) {
        return collegeMasterMapper.collegeMasterTo(collegeMaster.getCollegesByUniversity(universityId));
    }
}
