package in.gov.abdm.nmr.controller;

import java.math.BigInteger;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.service.ISearchService;

@RestController
@RequestMapping("/health-professional")
public class SearchController {

    private ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpSearchResponseTO searchHP(@RequestBody HpSearchRequestTO hpSearchRequestTO) throws NmrException {
        return searchService.searchHP(hpSearchRequestTO);
    }

    @GetMapping(path = "/{health-professionalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpSearchProfileTO getHpSearchProfileById(@PathVariable("health-professionalId") BigInteger profileId) throws NmrException {
        return searchService.getHpSearchProfileById(profileId);
    }
}
