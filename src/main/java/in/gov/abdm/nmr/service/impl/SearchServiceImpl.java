package in.gov.abdm.nmr.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import in.gov.abdm.nmr.client.LGDServiceFClient;
import in.gov.abdm.nmr.dto.HpSearchProfileQualificationTO;
import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.jpa.entity.ForeignQualificationDetailsMaster;
import in.gov.abdm.nmr.jpa.entity.HpProfileMaster;
import in.gov.abdm.nmr.jpa.entity.QualificationDetailsMaster;
import in.gov.abdm.nmr.jpa.entity.RegistrationDetailsMaster;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.jpa.repository.IForeignQualificationDetailMasterRepository;
import in.gov.abdm.nmr.jpa.repository.IHpProfileMasterRepository;
import in.gov.abdm.nmr.jpa.repository.IQualificationDetailMasterRepository;
import in.gov.abdm.nmr.jpa.repository.RegistrationDetailMasterRepository;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.ISearchService;

@Service
public class SearchServiceImpl implements ISearchService {

    private static final Logger LOGGER = LogManager.getLogger();

    private IElasticsearchDaoService elasticsearchDaoService;

    private IHpProfileMasterRepository iHpProfileMasterRepository;

    private RegistrationDetailMasterRepository registrationDetailMasterRepository;

    private IQualificationDetailMasterRepository qualificationDetailMasterRepository;

    private IForeignQualificationDetailMasterRepository foreignQualificationDetailMasterRepository;

    @Autowired
    private LGDServiceFClient lgdServiceFClient;

    private static final List<BigInteger> PROFILE_STATUS_CODES = Arrays.asList(BigInteger.valueOf(2l), BigInteger.valueOf(5l), BigInteger.valueOf(6l));

    public SearchServiceImpl(IElasticsearchDaoService elasticsearchDaoService, IHpProfileMasterRepository iHpProfileMasterRepository, RegistrationDetailMasterRepository registrationDetailMasterRepository, IQualificationDetailMasterRepository qualificationDetailMasterRepository, IForeignQualificationDetailMasterRepository foreignQualificationDetailMasterRepository) {
        this.elasticsearchDaoService = elasticsearchDaoService;
        this.iHpProfileMasterRepository = iHpProfileMasterRepository;
        this.registrationDetailMasterRepository = registrationDetailMasterRepository;
        this.qualificationDetailMasterRepository = qualificationDetailMasterRepository;
        this.foreignQualificationDetailMasterRepository = foreignQualificationDetailMasterRepository;
    }

    @Override
    public HpSearchResponseTO searchHP(HpSearchRequestTO hpSearchRequestTO, Pageable pageable) throws NmrException {
        try {
            if (hpSearchRequestTO != null && hpSearchRequestTO.getProfileStatusId() != null && !PROFILE_STATUS_CODES.contains(hpSearchRequestTO.getProfileStatusId())) {
                throw new NmrException("Invalid profile status code", HttpStatus.BAD_REQUEST);
            }
            SearchResponse<HpSearchResultTO> results = elasticsearchDaoService.searchHP(hpSearchRequestTO, pageable);
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

        HpProfileMaster hpProfileMaster = iHpProfileMasterRepository.findById(profileId).get();
        HpSearchProfileTO hpSearchProfileTO = new HpSearchProfileTO();
        hpSearchProfileTO.setFullName(hpProfileMaster.getFullName());
        hpSearchProfileTO.setSalutation(hpProfileMaster.getSalutation());
        hpSearchProfileTO.setFatherHusbandName(StringUtils.isNotBlank(hpProfileMaster.getSpouseName()) ? hpProfileMaster.getSpouseName() : hpProfileMaster.getFatherName());
        hpSearchProfileTO.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").format(hpProfileMaster.getDateOfBirth()));
        hpSearchProfileTO.setProfilePhoto(hpProfileMaster.getProfilePhoto());

        String mobileNumber = hpProfileMaster.getMobileNumber();
        if (mobileNumber != null && !mobileNumber.isBlank()) {
            mobileNumber = mobileNumber.replaceAll(mobileNumber.substring(0, 10 - 4), "xxxxxx");
        }
        hpSearchProfileTO.setMobileNumber(mobileNumber);

        String email = hpProfileMaster.getEmailId();
        if (email != null && !email.isBlank()) {
            String idPart = email.substring(0, email.lastIndexOf("@"));
            String domain = email.substring(email.lastIndexOf("@"), email.length());
            email = "x".repeat(idPart.length()) + domain;
        }
        hpSearchProfileTO.setEmail(email);
        
        hpSearchProfileTO.setYearOfInfo(hpProfileMaster.getYearOfInfo());
        hpSearchProfileTO.setNmrId(hpProfileMaster.getNmrId());

        RegistrationDetailsMaster registrationDetailsMaster = registrationDetailMasterRepository.getRegistrationDetailsByHpProfileId(profileId);
        hpSearchProfileTO.setStateMedicalCouncil(registrationDetailsMaster.getStateMedicalCouncil().getName());
        hpSearchProfileTO.setRegistrationNumber(registrationDetailsMaster.getRegistrationNo());
        hpSearchProfileTO.setDateOfRegistration(new SimpleDateFormat("dd-MM-yyyy").format(registrationDetailsMaster.getRegistrationDate()));
        hpSearchProfileTO.setRegistrationYear(new SimpleDateFormat("yyyy").format(registrationDetailsMaster.getRegistrationDate()));

        List<HpSearchProfileQualificationTO> qualificationTOs = new ArrayList<>();
        List<QualificationDetailsMaster> indianQualifications = qualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(profileId);
        qualificationTOs.addAll(indianQualifications.stream().map(indianQualification -> {
            HpSearchProfileQualificationTO hpSearchProfileQualificationTO = new HpSearchProfileQualificationTO();
            hpSearchProfileQualificationTO.setQualification(indianQualification.getCourse() != null ? indianQualification.getCourse().getCourseName() : null);
            hpSearchProfileQualificationTO.setQualificationYear(indianQualification.getQualificationYear());
            if (Objects.nonNull(indianQualification.getUniversity())) {
                hpSearchProfileQualificationTO.setUniversityName(indianQualification.getUniversity().getName());
            }
            return hpSearchProfileQualificationTO;
        }).toList());
        List<ForeignQualificationDetailsMaster> internationalQualifications = foreignQualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(profileId);
        qualificationTOs.addAll(internationalQualifications.stream().map(internationalQualification -> {
            HpSearchProfileQualificationTO hpSearchProfileQualificationTO = new HpSearchProfileQualificationTO();
            hpSearchProfileQualificationTO.setQualification(internationalQualification.getCourse());
            hpSearchProfileQualificationTO.setQualificationYear(internationalQualification.getQualificationYear());
            hpSearchProfileQualificationTO.setUniversityName(internationalQualification.getUniversity());
            return hpSearchProfileQualificationTO;
        }).toList());
        hpSearchProfileTO.setQualifications(qualificationTOs);
        return hpSearchProfileTO;
    }

    @Override
    public List fetchAddressByPinCodeFromLGD(String pinCode, String view) {
        return lgdServiceFClient.fetchAddressByPinCodeFromLGD(pinCode, view);
    }
}
