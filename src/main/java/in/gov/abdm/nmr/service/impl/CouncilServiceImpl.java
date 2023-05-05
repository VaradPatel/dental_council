package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.repository.ICouncilRepository;
import in.gov.abdm.nmr.service.ICouncilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouncilServiceImpl implements ICouncilService {

    @Autowired
    ICouncilRepository councilRepository;

    @Override
    public List<Council> getCouncilByRegistrationNumberAndCouncilName(String registrationNumber, String councilName) {
        return councilRepository.findCouncilByRegistrationNumberAndCouncilName(registrationNumber, councilName);
    }
}
