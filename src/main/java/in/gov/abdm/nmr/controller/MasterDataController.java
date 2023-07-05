package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.CollegeMasterResponseTo;
import in.gov.abdm.nmr.dto.QuerySuggestionsTo;
import in.gov.abdm.nmr.dto.UniversityMasterResponseTo;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IMasterDataService;
import in.gov.abdm.nmr.util.NMRConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import static in.gov.abdm.nmr.util.NMRConstants.*;

@RestController
public class MasterDataController {

    private IMasterDataService masterDataService;

    public MasterDataController(IMasterDataService masterDataService) {
        super();
        this.masterDataService = masterDataService;
    }

    @GetMapping(path = NMRConstants.STATE_MEDICAL_COUNCIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> getStateMedicalCouncils() {
        return masterDataService.smcs();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.SPECIALITIES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> specialities() {
        return masterDataService.specialities();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.COUNTRIES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> countries() {
        return masterDataService.countries();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.STATES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> states(@PathVariable(name = "country_id") BigInteger countryId) {
        return masterDataService.states(countryId);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.DISTRICTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> districts(@PathVariable(name = "state_id") BigInteger stateId) {
        return masterDataService.districts(stateId);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.SUB_DISTRICTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> subDistricts(@PathVariable(name = "district_id") BigInteger districtId) {
        return masterDataService.subDistricts(districtId);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.CITIES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> cities(@PathVariable(name = "sub_district_id") BigInteger subDistrictId) {
        return masterDataService.cities(subDistrictId);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.LANGUAGES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> languages() {
        return masterDataService.languages();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.COURSES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> courses() {
        return masterDataService.courses();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.RENEW_TYPES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> registrationRenewationType() {
        return masterDataService.registrationRenewationType();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.FACILITY_TYPES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MasterDataTO> facilityType() {
        return masterDataService.facilityType();
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.COLLEGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollegeMasterResponseTo> getCollegesByState(@RequestParam(required = false, value = "stateId") BigInteger stateId) {
        return masterDataService.getCollegesByState(stateId);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.UNIVERSITY, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UniversityMasterResponseTo> getUniversitiesByCollege(@RequestParam(required = false, value = "collegeId") BigInteger collegeId) {
        return masterDataService.getUniversitiesByCollege(collegeId);
    }

    /**
     * Get suggestions for queries
     * @return returns list of queries
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = ProtectedPaths.QUERY_SUGGESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<QuerySuggestionsTo> getQuerySuggestions() {
        return masterDataService.getQuerySuggestions();
    }
}
