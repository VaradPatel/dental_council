package in.gov.abdm.nmr.service.impl;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.ForeignQualificationDetailsMaster;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.entity.QualificationDetailsMaster;
import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.repository.IForeignQualificationDetailMasterRepository;
import in.gov.abdm.nmr.repository.IHpProfileMasterRepository;
import in.gov.abdm.nmr.repository.IQualificationDetailMasterRepository;
import in.gov.abdm.nmr.repository.RegistrationDetailMasterRepository;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.ISearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {

    private static final Logger LOGGER = LogManager.getLogger();
    
    private IElasticsearchDaoService elasticsearchDaoService;
    
    private IHpProfileMasterRepository iHpProfileMasterRepository;
    
    private RegistrationDetailMasterRepository registrationDetailMasterRepository;
    
    private IQualificationDetailMasterRepository qualificationDetailMasterRepository;
    
    private IForeignQualificationDetailMasterRepository foreignQualificationDetailMasterRepository;

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
        hpSearchProfileTO.setMobileNumber(hpProfileMaster.getMobileNumber());
        hpSearchProfileTO.setEmail(hpProfileMaster.getEmailId());
        hpSearchProfileTO.setYearOfInfo(hpProfileMaster.getYearOfInfo());
        hpSearchProfileTO.setNmrId(hpProfileMaster.getNmrId());

        RegistrationDetailsMaster registrationDetailsMaster = registrationDetailMasterRepository.getRegistrationDetailsByHpProfileId(profileId);
        hpSearchProfileTO.setStateMedicalCouncil(registrationDetailsMaster.getStateMedicalCouncil().getName());
        hpSearchProfileTO.setRegistrationNumber(registrationDetailsMaster.getRegistrationNo());
        hpSearchProfileTO.setDateOfRegistration(new SimpleDateFormat("dd-MM-yyyy").format(registrationDetailsMaster.getRegistrationDate()));

        List<HpSearchProfileQualificationTO> qualificationTOs = new ArrayList<>();
        List<QualificationDetailsMaster> indianQualifications = qualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(profileId);
        qualificationTOs.addAll(indianQualifications.stream().map(indianQualification ->{
            HpSearchProfileQualificationTO hpSearchProfileQualificationTO = new HpSearchProfileQualificationTO();
            hpSearchProfileQualificationTO.setQualification(indianQualification.getName());
            hpSearchProfileQualificationTO.setQualificationYear(indianQualification.getQualificationYear());
            hpSearchProfileQualificationTO.setUniversityName(indianQualification.getUniversity().getName());
            return hpSearchProfileQualificationTO;
        }).toList());
        List<ForeignQualificationDetailsMaster> internationalQualifications = foreignQualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(profileId);
        qualificationTOs.addAll(internationalQualifications.stream().map(internationalQualification ->{
            HpSearchProfileQualificationTO hpSearchProfileQualificationTO = new HpSearchProfileQualificationTO();
            hpSearchProfileQualificationTO.setQualification(internationalQualification.getName());
            hpSearchProfileQualificationTO.setQualificationYear(internationalQualification.getQualificationYear());
            hpSearchProfileQualificationTO.setUniversityName(internationalQualification.getUniversity());
            return hpSearchProfileQualificationTO;
        }).toList());
        hpSearchProfileTO.setQualifications(qualificationTOs);
        return hpSearchProfileTO;
    }
}
