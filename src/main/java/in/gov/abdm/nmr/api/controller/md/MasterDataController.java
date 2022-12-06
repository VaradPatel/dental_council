package in.gov.abdm.nmr.api.controller.md;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.api.controller.md.to.MasterDataTO;

@RestController
@RequestMapping("/md")
public class MasterDataController {

    private IMasterDataService masterDataService;

    public MasterDataController(IMasterDataService masterDataService) {
        super();
        this.masterDataService = masterDataService;
    }

    @GetMapping(path = "/smcs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> smcs() {
        return masterDataService.smcs();
    }
    
    @GetMapping(path = "/specialities", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> specialities() {
        return masterDataService.specialities();
    }
    
    @GetMapping(path = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> countries() {
        return masterDataService.countries();
    }
    
    @GetMapping(path = "country/{country_id}/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> states(@PathVariable(name = "country_id") Long countryId) {
        return masterDataService.states(countryId);
    }
    
    @GetMapping(path = "state/{state_id}/districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> districts(@PathVariable(name = "state_id") Long stateId) {
        return masterDataService.districts(stateId);
    }
    
    @GetMapping(path = "district/{district_id}/sub_districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> subDistricts(@PathVariable(name = "district_id") Long districtId) {
        return masterDataService.subDistricts(districtId);
    }
    
    @GetMapping(path = "sub_district/{sub_district_id}/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> cities(@PathVariable(name = "sub_district_id") Long subDistrictId) {
        return masterDataService.cities(subDistrictId);
    }
    
    @GetMapping(path = "/universities", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> universities() {
        return masterDataService.universities();
    }
    
    @GetMapping(path = "university/{university_id}/colleges", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> colleges(@PathVariable(name = "university_id") Long universityId) {
        return masterDataService.colleges(universityId);
    }
    
    @GetMapping(path = "/languages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> languages() {
        return masterDataService.languages();
    }
}
