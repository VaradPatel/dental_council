package in.gov.abdm.nmr.controller;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.service.IMasterDataService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<MasterDataTO> states(@PathVariable(name = "country_id") BigInteger countryId) {
        return masterDataService.states(countryId);
    }
    
    @GetMapping(path = "state/{state_id}/districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> districts(@PathVariable(name = "state_id") BigInteger stateId) {
        return masterDataService.districts(stateId);
    }
    
    @GetMapping(path = "district/{district_id}/sub_districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> subDistricts(@PathVariable(name = "district_id") BigInteger districtId) {
        return masterDataService.subDistricts(districtId);
    }
    
    @GetMapping(path = "sub_district/{sub_district_id}/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> cities(@PathVariable(name = "sub_district_id") BigInteger subDistrictId) {
        return masterDataService.cities(subDistrictId);
    }
    
    @GetMapping(path = "/universities", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> universities() {
        return masterDataService.universities();
    }
    
    @GetMapping(path = "university/{university_id}/colleges", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> colleges(@PathVariable(name = "university_id") BigInteger universityId) {
        return masterDataService.colleges(universityId);
    }
    
    @GetMapping(path = "/languages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> languages() {
        return masterDataService.languages();
    }
    
    @GetMapping(path = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> courses() {
        return masterDataService.courses();
    }
    
    @GetMapping(path = "/registration_renewation_type", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> registrationRenewationType() {
        return masterDataService.registrationRenewationType();
    }
    
    @GetMapping(path = "/facility_type", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> facilityType() {
        return masterDataService.facilityType();
    }
}
