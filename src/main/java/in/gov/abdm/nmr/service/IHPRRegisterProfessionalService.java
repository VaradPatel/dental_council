package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.*;

import java.util.List;

public interface IHPRRegisterProfessionalService {
    HPRRequestTo createRequestPayloadForHPRProfileCreation(HpProfile transactionHpProfile, HpProfileMaster masterHpProfileDetails, RegistrationDetailsMaster registrationMaster, AddressMaster addressMaster, List<QualificationDetailsMaster> qualificationDetailsMasterList, List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList);
}
