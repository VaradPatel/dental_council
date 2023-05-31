package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.client.HPRIDFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.mapper.NMRToHPRMapper;
import in.gov.abdm.nmr.service.IHPRRegisterProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
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


    @Override
    public HPRRequestTo createRequestPayloadForHPRProfileCreation(HpProfile transactionHpProfile, HpProfileMaster masterHpProfileDetails,
                                                                  RegistrationDetailsMaster registrationMaster, AddressMaster addressMaster,
                                                                  List<QualificationDetailsMaster> qualificationDetailsMasterList,
                                                                  List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList) {
        SessionResponseTo sessionResponseTo = getSessionToken();
        String authorization = BEARER + sessionResponseTo.getAccessToken();
        HPRIdTokenResponseTO responseTO = getTokensByHprId(authorization, transactionHpProfile);

        PractitionerRequestTO practitionerRequestTO = nmrToHPRMapper.convertNmrDataToHprRequestTo(responseTO, masterHpProfileDetails, registrationMaster, addressMaster, qualificationDetailsMasterList, foreignQualificationDetailsMasterList);
        HPRRequestTo hprRequestTo = new HPRRequestTo();
        hprRequestTo.setAuthorization(authorization);
        hprRequestTo.setPractitionerRequestTO(practitionerRequestTO);
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
