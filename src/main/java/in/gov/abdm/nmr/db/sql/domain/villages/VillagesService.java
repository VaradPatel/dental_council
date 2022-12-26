package in.gov.abdm.nmr.db.sql.domain.villages;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class VillagesService implements IVillagesService {

    public VillagesRepository villagesRepository;

    private VillagesDtoMapper villagesDtoMapper;

    public VillagesService(VillagesRepository villagesRepository, VillagesDtoMapper villagesDtoMapper) {
        this.villagesRepository = villagesRepository;
        this.villagesDtoMapper = villagesDtoMapper;
    }

    @Override
    public List<VillagesTO> getCityData(BigInteger subdistrictId) {
    	return villagesDtoMapper.villageDataToDto(villagesRepository.getVillage(subdistrictId));
    }
}
