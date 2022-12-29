package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.mapper.SubDistrictDtoMapper;
import in.gov.abdm.nmr.repository.SubDistrictRepository;
import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.service.ISubDistrictService;
import org.springframework.stereotype.Service;

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
//        List<Tuple> districtList = subDistrictRepository.getSubDistrict(districtId);
//        List<SubDistrictTO> subDistrictTOList = new ArrayList<SubDistrictTO>();
//        for (Tuple district : districtList) {
//            SubDistrictTO subDistrictTO = new SubDistrictTO();
//            subDistrictTO.setId(district.get("id", BigInteger.class));
//            subDistrictTO.setName(district.get("name", String.class));
//            subDistrictTOList.add(subDistrictTO);
//        }
//        return subDistrictTOList;
    	return subDistrictDtoMapper.subDistrictDataToDto(subDistrictRepository.getSubDistrict(districtId));
    }
}
