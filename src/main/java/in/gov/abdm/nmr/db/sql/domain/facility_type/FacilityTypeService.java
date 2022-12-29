package in.gov.abdm.nmr.db.sql.domain.facility_type;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class FacilityTypeService implements IFacilityTypeService {

    public FacilityTypeRepository facilityTypeRepository;

    private FacilityTypeDtoMapper facilityTypeDtoMapper;

    public FacilityTypeService( FacilityTypeRepository facilityTypeRepository, FacilityTypeDtoMapper facilityTypeDtoMapper) {
        this.facilityTypeRepository = facilityTypeRepository;
        this.facilityTypeDtoMapper = facilityTypeDtoMapper;
    }

    @Override
    public List<FacilityTypeTO> getFacilityType() {
        return facilityTypeDtoMapper.FacilityTypeDataToDto(facilityTypeRepository.getFacilityType());

    }

}
