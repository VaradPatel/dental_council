package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.mapper.SubDistrictDtoMapper;
import in.gov.abdm.nmr.repository.SubDistrictRepository;
import in.gov.abdm.nmr.service.ISubDistrictService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class SubDistrictServiceImpl implements ISubDistrictService {

    private SubDistrictRepository subDistrictRepository;

    public SubDistrictServiceImpl(SubDistrictRepository subDistrictRepository) {
        this.subDistrictRepository = subDistrictRepository;
    }

    @Override
    public List<SubDistrictTO> getSubDistrictData(BigInteger districtId) {
    	return SubDistrictDtoMapper.SUB_DISTRICT_DTO_MAPPER.subDistrictDataToDto(subDistrictRepository.getSubDistrict(districtId));
    }
}
