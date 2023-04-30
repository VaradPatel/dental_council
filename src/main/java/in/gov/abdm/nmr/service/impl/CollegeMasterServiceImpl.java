package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CollegeMasterTo;
import in.gov.abdm.nmr.repository.ICollegeMasterRepository;
import in.gov.abdm.nmr.service.ICollegeMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.mapper.ICollegeMasterMapper.COLLEGE_MASTER_MAPPER;

@Service
@Slf4j
public class CollegeMasterServiceImpl implements ICollegeMasterService {

    @Autowired
    ICollegeMasterRepository collegeMaster;

    @Override
    public List<CollegeMasterTo> getCollegesByStateId(BigInteger stateId) {
        return COLLEGE_MASTER_MAPPER.collegeMasterTo(stateId != null ? collegeMaster.getCollegesByStateId(stateId) : collegeMaster.getColleges());
    }
}
