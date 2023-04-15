package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.mapper.BroadSpecialityDtoMapper;
import in.gov.abdm.nmr.repository.BroadSpecialityRepository;
import in.gov.abdm.nmr.service.IBroadSpecialityService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BroadSpecialityServiceImpl implements IBroadSpecialityService {

    private BroadSpecialityRepository broadSpecialityRepository;

    public BroadSpecialityServiceImpl(BroadSpecialityRepository broadSpecialityRepository) {
        this.broadSpecialityRepository = broadSpecialityRepository;
    }

    @Override
    public List<BroadSpecialityTO> getSpecialityData() {
        return BroadSpecialityDtoMapper.BROAD_SPECIALITY_DTO_MAPPER.specialityDataToDto(broadSpecialityRepository.getSpeciality());
    }
}
