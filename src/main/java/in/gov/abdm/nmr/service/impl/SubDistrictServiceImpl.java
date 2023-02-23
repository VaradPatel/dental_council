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

    public SubDistrictRepository subDistrictRepository;

    private SubDistrictDtoMapper subDistrictDtoMapper;

    public SubDistrictServiceImpl(SubDistrictRepository subDistrictRepository, SubDistrictDtoMapper subDistrictDtoMapper) {
        this.subDistrictRepository = subDistrictRepository;
        this.subDistrictDtoMapper = subDistrictDtoMapper;
    }

    @Override
    public List<SubDistrictTO> getSubDistrictData(BigInteger districtId) {
    	return subDistrictDtoMapper.subDistrictDataToDto(subDistrictRepository.getSubDistrict(districtId));
    }
}
