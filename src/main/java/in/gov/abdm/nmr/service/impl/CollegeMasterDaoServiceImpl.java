package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.jpa.entity.CollegeMaster;
import in.gov.abdm.nmr.jpa.repository.ICollegeMasterRepository;
import in.gov.abdm.nmr.service.ICollegeMasterDaoService;

@Service
@Transactional
public class CollegeMasterDaoServiceImpl implements ICollegeMasterDaoService {

    private ICollegeMasterRepository collegeMasterRepository;

    public CollegeMasterDaoServiceImpl(ICollegeMasterRepository collegeMasterRepository) {
        this.collegeMasterRepository = collegeMasterRepository;
    }

    @Override
    public List<CollegeMaster> getAllColleges() {
        return collegeMasterRepository.findAll();
    }

    @Override
    public CollegeMaster findById(BigInteger id) {
        return collegeMasterRepository.findById(id).orElse(null);
    }

    @Override
    public CollegeMaster save(CollegeMaster collegeMaster) {
        return collegeMasterRepository.save(collegeMaster);
    }
}
