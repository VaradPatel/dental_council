package in.gov.abdm.nmr.api.controller.md;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;

@Service
public class MasterDataService implements IMasterDataService {

    @Override
    public List<MasterDataTO> smcs() {
        return Collections.emptyList();
    }
}
