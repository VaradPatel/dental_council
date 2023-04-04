package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.mapper.BroadSpecialityDtoMapper;
import in.gov.abdm.nmr.jpa.repository.BroadSpecialityRepository;
import in.gov.abdm.nmr.service.IBroadSpecialityService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BroadSpecialityServiceImpl implements IBroadSpecialityService {

    private BroadSpecialityRepository broadSpecialityRepository;

    private BroadSpecialityDtoMapper broadSpecialityDtoMapper;

    public BroadSpecialityServiceImpl(BroadSpecialityRepository broadSpecialityRepository, BroadSpecialityDtoMapper broadSpecialityDtoMapper) {
        this.broadSpecialityRepository = broadSpecialityRepository;
        this.broadSpecialityDtoMapper = broadSpecialityDtoMapper;
    }

    @Override
    public List<BroadSpecialityTO> getSpecialityData() {
        return broadSpecialityDtoMapper.specialityDataToDto(broadSpecialityRepository.getSpeciality());
    }
}
