package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.service.IMasterDataService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
public class MasterDataController {

    private IMasterDataService masterDataService;

    public MasterDataController(IMasterDataService masterDataService) {
        super();
        this.masterDataService = masterDataService;
    }

    @GetMapping(path = "/state-medical-councils", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(path = "/countries/{country_id}/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> states(@PathVariable(name = "country_id") BigInteger countryId) {
        return masterDataService.states(countryId);
    }

    @GetMapping(path = "/countries/states/{state_id}/districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> districts(@PathVariable(name = "state_id") BigInteger stateId) {
        return masterDataService.districts(stateId);
    }

    @GetMapping(path = "/countries/states/districts/{district_id}/sub_districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> subDistricts(@PathVariable(name = "district_id") BigInteger districtId) {
        return masterDataService.subDistricts(districtId);
    }

    @GetMapping(path = "/countries/states/districts/sub-districts/{sub_district_id}/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> cities(@PathVariable(name = "sub_district_id") BigInteger subDistrictId) {
        return masterDataService.cities(subDistrictId);
    }

    @GetMapping(path = "/universities", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> universities() {
        return masterDataService.universities();
    }

    @GetMapping(path = "/universities/{university_id}/colleges", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(path = "/renewation-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> registrationRenewationType() {
        return masterDataService.registrationRenewationType();
    }

    @GetMapping(path = "/facility-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> facilityType() {
        return masterDataService.facilityType();
    }

    @GetMapping(path = "/college", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollegeMasterResponseTo> getCollegesByState(@RequestParam(required = false, value = "stateId") BigInteger stateId) {
        return masterDataService.getCollegesByState(stateId);
    }

    @GetMapping(path = "/university", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UniversityMasterResponseTo> getUniversitiesByCollege(@RequestParam(required = false, value = "collegeId") BigInteger collegeId) {
        return masterDataService.getUniversitiesByCollege(collegeId);
    }

    @GetMapping(path = "/colleges", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollegeMasterResponseTo> getCollegesByUniversity(@RequestParam(required = false, value = "universityId") BigInteger universityId) {
        return masterDataService.getCollegesByUniversity(universityId);
    }

    @GetMapping(path = "/universitiesByStateId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UniversityMasterResponseTo> getUniversitiesByState(@RequestParam(required = false, value = "stateId") BigInteger stateId) {
        return masterDataService.getUniversitiesByState(stateId);
    }
}
