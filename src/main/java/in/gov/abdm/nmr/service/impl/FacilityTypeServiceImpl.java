package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.mapper.FacilityTypeDtoMapper;
import in.gov.abdm.nmr.repository.FacilityTypeRepository;
import in.gov.abdm.nmr.service.IFacilityTypeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FacilityTypeServiceImpl implements IFacilityTypeService {

    private FacilityTypeRepository facilityTypeRepository;

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
