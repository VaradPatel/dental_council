package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.mapper.DistrictDtoMapper;
import in.gov.abdm.nmr.repository.DistrictRepository;
import in.gov.abdm.nmr.service.IDistrictService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class DistrictServiceImpl implements IDistrictService {

    private DistrictRepository districtRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Override
    public List<DistrictTO> getDistrictData(BigInteger stateId) {
    	return DistrictDtoMapper.DISTRICT_DTO_MAPPER.districtDataToDto(districtRepository.getDistrict(stateId));
    }
}
