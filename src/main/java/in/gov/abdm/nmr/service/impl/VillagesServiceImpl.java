package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.VillagesTO;
import in.gov.abdm.nmr.mapper.VillagesDtoMapper;
import in.gov.abdm.nmr.repository.VillagesRepository;
import in.gov.abdm.nmr.service.IVillagesService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class VillagesServiceImpl implements IVillagesService {

    private VillagesRepository villagesRepository;


    public VillagesServiceImpl(VillagesRepository villagesRepository) {
        this.villagesRepository = villagesRepository;
    }

    @Override
    public List<VillagesTO> getCityData(BigInteger subdistrictId) {
    	return VillagesDtoMapper.VILLAGES_DTO_MAPPER.villageDataToDto(villagesRepository.getVillage(subdistrictId));
    }
}
