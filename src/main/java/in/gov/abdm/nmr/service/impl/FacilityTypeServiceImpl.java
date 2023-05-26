package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.mapper.FacilityTypeDtoMapper;
import in.gov.abdm.nmr.repository.FacilityTypeRepository;
import in.gov.abdm.nmr.service.IFacilityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FacilityTypeServiceImpl implements IFacilityTypeService {

    @Autowired
    FacilityTypeRepository facilityTypeRepository;


    @Override
    public List<FacilityTypeTO> getFacilityType() {
        return FacilityTypeDtoMapper.FACILITY_TYPE_DTO_MAPPER.facilityTypeDataToDto(facilityTypeRepository.getFacilityType());
    }

}
