package in.gov.abdm.nmr.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.client.HPRFClient;
import in.gov.abdm.nmr.client.HPRIDFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.mapper.NMRToHPRMapper;
import in.gov.abdm.nmr.service.IHPRRegisterProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
@Slf4j
public class HPRRegisterProfessionalServiceImpl implements IHPRRegisterProfessionalService {
    @Value("${session.clientId}")
    private String clientId;
    @Value("${session.clientSecret}")
    private String clientSecret;
    @Autowired
    HPRIDFClient hprIdClient;
    @Autowired
    GatewayFClient gatewayFClient;
    @Autowired
    NMRToHPRMapper nmrToHPRMapper;
    @Autowired
    HPRFClient hprClient;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Async
    public HPRRequestTo createRequestPayloadForHPRProfileCreation(HpProfile transactionHpProfile, HpProfileMaster masterHpProfileDetails,
                                                                  RegistrationDetailsMaster registrationMaster, AddressMaster addressMaster,
                                                                  List<QualificationDetailsMaster> qualificationDetailsMasterList,
                                                                  List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList) {
        HPRRequestTo hprRequestTo = null;
        try {
            SessionResponseTo sessionResponseTo = getSessionToken();
            if (sessionResponseTo.getAccessToken() != null) {
                String authorization = BEARER + sessionResponseTo.getAccessToken();
                HPRIdTokenResponseTO responseTO = getTokensByHprId(authorization, transactionHpProfile);

                if (responseTO != null) {
                    PractitionerRequestTO practitionerRequestTO = nmrToHPRMapper.convertNmrDataToHprRequestTo(responseTO, masterHpProfileDetails, registrationMaster, addressMaster, qualificationDetailsMasterList, foreignQualificationDetailsMasterList);
                    hprRequestTo = new HPRRequestTo();
                    hprRequestTo.setAuthorization(authorization);
                    hprRequestTo.setPractitionerRequestTO(practitionerRequestTO);

                    log.info("Processing register Health Professional. Request JSON: " + objectMapper.writeValueAsString(hprRequestTo.getPractitionerRequestTO()));
                    hprClient.registerHealthProfessional(hprRequestTo.getAuthorization(), hprRequestTo.getPractitionerRequestTO());
                    log.info(HPR_REGISTER_SUCCESS);
                }
            }
        } catch (FeignException.UnprocessableEntity e) {
            log.info("An error occurred while trying to create a profile in the HPR system.", e);
            log.error(HPR_REGISTER_MISSING_VALUES + e.getMessage());
        } catch (Exception e) {
            log.info("An error occurred while trying to create a profile in the HPR system.", e);
            log.error(HPR_REGISTER_FAILED + e.getMessage());
        }
        return hprRequestTo;
    }

    private SessionResponseTo getSessionToken() {
        SessionRequestTo sessionRequestTo = SessionRequestTo.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
        return gatewayFClient.sessions(sessionRequestTo);
    }

    private HPRIdTokenResponseTO getTokensByHprId(String authorization, HpProfile transactionHpProfile) {
        HPRIdTokenRequestTO hprIdTokenRequestTO = HPRIdTokenRequestTO.builder()
                .idType(HPR_ID)
                .domainName(DOMAIN_NAME)
                .hprId(transactionHpProfile.getUser().getHprIdNumber())
                .build();
        return hprIdClient.getTokensByHprId(authorization, hprIdTokenRequestTO);

    }
}
