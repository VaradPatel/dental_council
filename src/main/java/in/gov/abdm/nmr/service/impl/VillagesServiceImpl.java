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

    public VillagesRepository villagesRepository;

    private VillagesDtoMapper villagesDtoMapper;

    public VillagesServiceImpl(VillagesRepository villagesRepository, VillagesDtoMapper villagesDtoMapper) {
        this.villagesRepository = villagesRepository;
        this.villagesDtoMapper = villagesDtoMapper;
    }

    @Override
    public List<VillagesTO> getCityData(BigInteger subdistrictId) {
    	return villagesDtoMapper.villageDataToDto(villagesRepository.getVillage(subdistrictId));
    }
}
