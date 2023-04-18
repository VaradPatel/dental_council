package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.exception.InvalidIDException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.service.ISearchService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@RestController
@RequestMapping("/health-professional")
public class SearchController {

    private ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpSearchResponseTO searchHP(HpSearchRequestTO hpSearchRequestTO, @PageableDefault(page = 0, size = 10) Pageable pageable) throws NmrException, InvalidRequestException {
        return searchService.searchHP(hpSearchRequestTO, pageable);
    }

    @GetMapping(path = "/{healthProfessionalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpSearchProfileTO getHpSearchProfileById(@PathVariable("healthProfessionalId") BigInteger healthProfessionalId) throws NmrException, InvalidIDException {
        return searchService.getHpSearchProfileById(healthProfessionalId);
    }


    /**
     * This method returns LGD Codes related to a particular pinCode, stateCode or districtCode
     * @param pinCode
     * @param view - filter criteria for the returned value
     * @return LGD Codes
     */
    @GetMapping(LGD_SEARCH_URL)
    public List fetchAddressByPinCodeFromLGD(@RequestParam(value = PIN_CODE,required = false) String pinCode,
                                             @RequestParam(value = VIEW,required = false) String view){
        return searchService.fetchAddressByPinCodeFromLGD(pinCode, view);
    }
}
