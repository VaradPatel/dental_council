package in.gov.abdm.nmr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.mapper.BroadSpecialityDtoMapper;
import in.gov.abdm.nmr.repository.BroadSpecialityRepository;
import in.gov.abdm.nmr.service.IBroadSpecialityService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BroadSpecialityServiceImpl implements IBroadSpecialityService {

    public BroadSpecialityRepository broadSpecialityRepository;

    private BroadSpecialityDtoMapper broadSpecialityDtoMapper;

    public BroadSpecialityServiceImpl(BroadSpecialityRepository broadSpecialityRepository, BroadSpecialityDtoMapper broadSpecialityDtoMapper) {
        this.broadSpecialityRepository = broadSpecialityRepository;
        this.broadSpecialityDtoMapper = broadSpecialityDtoMapper;
    }

    @Override
    public List<BroadSpecialityTO> getSpecialityData() {
        return broadSpecialityDtoMapper.SpecialityDataToDto(broadSpecialityRepository.getSpeciality());

    }
}
