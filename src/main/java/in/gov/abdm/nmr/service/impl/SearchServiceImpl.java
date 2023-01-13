package in.gov.abdm.nmr.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Tuple;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import in.gov.abdm.nmr.dto.HpSearchProfileQualificationTO;
import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.service.IQualificationDetailDaoService;
import in.gov.abdm.nmr.service.IRegistrationDetailDaoService;
import in.gov.abdm.nmr.service.ISearchService;

@Service
public class SearchServiceImpl implements ISearchService {

    private static final Logger LOGGER = LogManager.getLogger();

    private IElasticsearchDaoService elasticsearchDaoService;

    private IHpProfileDaoService hpProfileDaoService;

    private IQualificationDetailDaoService qualificationDetailDaoService;

    private IRegistrationDetailDaoService registrationDetailDaoService;

    private static final List<BigInteger> profileStatusCodes = Arrays.asList(BigInteger.valueOf(2l), BigInteger.valueOf(5l), BigInteger.valueOf(6l));

    public SearchServiceImpl(IElasticsearchDaoService elasticsearchDaoService, IHpProfileDaoService hpProfileDaoService, //
                             IQualificationDetailDaoService qualificationDetailDaoService, IRegistrationDetailDaoService registrationDetailDaoService) {
        this.elasticsearchDaoService = elasticsearchDaoService;
        this.hpProfileDaoService = hpProfileDaoService;
        this.qualificationDetailDaoService = qualificationDetailDaoService;
        this.registrationDetailDaoService = registrationDetailDaoService;
    }

    @Override
    public HpSearchResponseTO searchHP(HpSearchRequestTO hpSearchRequestTO) throws NmrException {
        try {
            if (hpSearchRequestTO != null && hpSearchRequestTO.getProfileStatusId() != null && !profileStatusCodes.contains(hpSearchRequestTO.getProfileStatusId())) {
                throw new NmrException("Invalid profile status code", HttpStatus.BAD_REQUEST);
            }
            SearchResponse<HpSearchResultTO> results = elasticsearchDaoService.searchHP(hpSearchRequestTO);
            return new HpSearchResponseTO(results.hits().hits().stream().map(Hit::source).toList(), results.hits().total().value());

        } catch (ElasticsearchException | IOException e) {
            LOGGER.error("Exception while searching for HP", e);
            throw new NmrException("Exception while searching for HP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public HpSearchProfileTO getHpSearchProfileById(BigInteger profileId) throws NmrException {
        try {
            if (!elasticsearchDaoService.doesHpExists(profileId)) {
                throw new NmrException("No resource found for id", HttpStatus.NOT_FOUND);
            }
        } catch (ElasticsearchException | IOException | NmrException e) {
            if (e instanceof NmrException ne) {
                throw ne;
            }
            LOGGER.error("Exception while retrieving HP profile", e);
            throw new NmrException("Exception while retrieving HP profile", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HpProfile hpprofile = hpProfileDaoService.findbyId(profileId);
        HpSearchProfileTO hpSearchProfileTO = new HpSearchProfileTO();
        hpSearchProfileTO.setFullName(hpprofile.getFullName());
        hpSearchProfileTO.setSalutation(hpprofile.getSalutation());
        hpSearchProfileTO.setFatherHusbandName(StringUtils.isNotBlank(hpprofile.getSpouseName()) ? hpprofile.getSpouseName() : hpprofile.getFatherName());
        hpSearchProfileTO.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").format(hpprofile.getDateOfBirth()));
        hpSearchProfileTO.setMobileNumber(hpprofile.getMobileNumber());
        hpSearchProfileTO.setEmail(hpprofile.getEmailId());
        hpSearchProfileTO.setYearOfInfo(hpprofile.getYearOfInfo());
        hpSearchProfileTO.setNmrId(hpprofile.getNmrId());

        RegistrationDetails registrationDetails = registrationDetailDaoService.findByHpProfileId(profileId);
        hpSearchProfileTO.setStateMedicalCouncil(registrationDetails.getStateMedicalCouncil().getName());
        hpSearchProfileTO.setRegistrationNumber(registrationDetails.getRegistrationNo());
        hpSearchProfileTO.setDateOfRegistration(new SimpleDateFormat("dd-MM-yyyy").format(registrationDetails.getRegistrationDate()));

        List<HpSearchProfileQualificationTO> qualificationTOs = new ArrayList<>();
        for (Tuple qualificationDetail : qualificationDetailDaoService.findSearchQualificationDetailsByHpProfileId(profileId)) {
            HpSearchProfileQualificationTO hpSearchProfileQualificationTO = new HpSearchProfileQualificationTO();
            hpSearchProfileQualificationTO.setQualification(qualificationDetail.get("qualificationName", String.class));
            hpSearchProfileQualificationTO.setQualificationYear(qualificationDetail.get("qualificationYear", String.class));
            hpSearchProfileQualificationTO.setUniversityName(qualificationDetail.get("universityName", String.class));
            qualificationTOs.add(hpSearchProfileQualificationTO);
        }
        hpSearchProfileTO.setQualifications(qualificationTOs);

        return hpSearchProfileTO;
    }
}
