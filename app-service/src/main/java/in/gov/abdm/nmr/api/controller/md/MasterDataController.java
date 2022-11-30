package in.gov.abdm.nmr.api.controller.md;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;

@RestController
@RequestMapping("/master")
public class MasterDataController {

    private IMasterDataService masterDataService;

    public MasterDataController(IMasterDataService masterDataService) {
        super();
        this.masterDataService = masterDataService;
    }

    @GetMapping(name = "/smcs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> smcs() {
        return masterDataService.smcs();
    }
}
