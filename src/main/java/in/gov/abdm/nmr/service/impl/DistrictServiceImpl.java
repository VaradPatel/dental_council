package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.service.IDistrictService;
import in.gov.abdm.nmr.mapper.DistrictDtoMapper;
import in.gov.abdm.nmr.repository.DistrictRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DistrictServiceImpl implements IDistrictService {

    public DistrictRepository districtRepository;

    private DistrictDtoMapper districtDtoMapper;

    public DistrictServiceImpl(DistrictRepository districtRepository, DistrictDtoMapper districtDtoMapper) {
        this.districtRepository = districtRepository;
        this.districtDtoMapper = districtDtoMapper;
    }

    @Override
    public List<DistrictTO> getDistrictData(BigInteger stateId) {
    	return districtDtoMapper.districtDataToDto(districtRepository.getDistrict(stateId));
    }
}
