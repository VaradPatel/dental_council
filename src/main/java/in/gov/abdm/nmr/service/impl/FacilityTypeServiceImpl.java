package in.gov.abdm.nmr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.mapper.FacilityTypeDtoMapper;
import in.gov.abdm.nmr.repository.FacilityTypeRepository;
import in.gov.abdm.nmr.service.IFacilityTypeService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FacilityTypeServiceImpl implements IFacilityTypeService {

    public FacilityTypeRepository facilityTypeRepository;

    private FacilityTypeDtoMapper facilityTypeDtoMapper;

    public FacilityTypeServiceImpl(FacilityTypeRepository facilityTypeRepository, FacilityTypeDtoMapper facilityTypeDtoMapper) {
        this.facilityTypeRepository = facilityTypeRepository;
        this.facilityTypeDtoMapper = facilityTypeDtoMapper;
    }

    @Override
    public List<FacilityTypeTO> getFacilityType() {
        return facilityTypeDtoMapper.FacilityTypeDataToDto(facilityTypeRepository.getFacilityType());

    }

}
