package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.repository.ICouncilRepository;
import in.gov.abdm.nmr.service.ICouncilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CouncilServiceImpl implements ICouncilService {

    @Autowired
    private ICouncilRepository councilRepository;

    @Override
    public List<Council> getCouncilByRegistrationNumberAndCouncilName(String registrationNumber, String councilName) {

        Pageable pageable = PageRequest.of(0,1, Sort.by("_id").descending());
                return councilRepository.findCouncilByRegistrationNumberAndCouncilName(registrationNumber,councilName);
    }
}
