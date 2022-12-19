package in.gov.abdm.nmr.api.controller.college;

import java.math.BigInteger;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.api.security.common.ProtectedPaths;

@RestController
public class CollegeController {

    private ICollegeService collegeService;
    
    public CollegeController(ICollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping(path = "college", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeProfileTo registerCollege(@RequestBody CollegeRegistrationRequestTo collegeRegistrationRequestTo){
        return collegeService.registerCollege(collegeRegistrationRequestTo);
    }

    @PostMapping(path = ProtectedPaths.PATH_REGISTER_COLLEGE_REGISTRAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeRegistrarProfileTo registerRegistrar(@RequestBody CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo){
        return collegeService.registerRegistrar(collegeRegistrarCreationRequestTo);
    }

    @PostMapping(path = ProtectedPaths.PATH_REGISTER_COLLEGE_DEAN, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollegeDeanProfileTo registerDean(@RequestBody CollegeDeanCreationRequestTo collegeDeanCreationRequestTo){
        return collegeService.registerDean(collegeDeanCreationRequestTo);
    }

    @GetMapping(path = "college/{id}")
    public CollegeProfileTo retrieveCollegeProfile(@PathVariable(name = "id") BigInteger collegeId){
        return collegeService.retrieveCollegeProfile(collegeId);
    }

    @GetMapping(path = "college/registrar/{id}")
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(@PathVariable(name = "id") BigInteger registrarId){
        return collegeService.retrieveRegistrarProfile(registrarId);
    }

    @GetMapping(path = "college/dean/{id}")
    public CollegeDeanProfileTo retrieveDeanProfile(@PathVariable(name = "id") BigInteger id){
        return collegeService.retrieveDeanProfile(id);
    }
}
