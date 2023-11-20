package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.service.ISearchService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@CrossOrigin
@RequestMapping("/health-professional")
public class SearchController {

    ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpSearchResponseTO searchHP(HpSearchRequestTO hpSearchRequestTO, @PageableDefault(page = 0, size = 10) Pageable pageable) throws NmrException, InvalidRequestException {
        return searchService.searchHP(hpSearchRequestTO, pageable);
    }

    @GetMapping(path = "/{healthProfessionalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpSearchProfileTO getHpSearchProfileById(@PathVariable("healthProfessionalId") BigInteger healthProfessionalId) throws NmrException, InvalidIdException {
        return searchService.getHpSearchProfileById(healthProfessionalId);
    }
}
