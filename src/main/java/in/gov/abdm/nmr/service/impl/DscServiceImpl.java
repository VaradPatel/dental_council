package in.gov.abdm.nmr.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.client.DscFClient;
import in.gov.abdm.nmr.dto.dsc.*;
import in.gov.abdm.nmr.enums.TemplateEnum;
import in.gov.abdm.nmr.service.IDscService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DscServiceImpl implements IDscService {

    @Autowired
    DscFClient dscFClient;

    @Override
    public DscResponseTo invokeDSCGenEspRequest(DscRequestTo dscRequestTo) throws JsonProcessingException {

        ObjectMapper objectMapper= new ObjectMapper();
        return objectMapper.readValue(dscFClient.genEspRequest(dscInputData(dscRequestTo)),DscResponseTo.class);

    }

    private DscDocumentTo dscInputData(DscRequestTo dscRequestTo) {
        DscDocumentTo dscDocumentTo = new DscDocumentTo();
        DscDocument dscDocument = new DscDocument();
        DocumentDetailsTO documentDetails = new DocumentDetailsTO();
        dscDocument.setIntegratorName(NMRConstants.DCS_INTEGRATOR_NAME);
        dscDocument.setSigningPlace(dscRequestTo.getSigningPlace());

        PersonalDetailTO personalDetailTO = new PersonalDetailTO();
        dscDocument.setTemplateId(TemplateEnum.TEMPLATE_1.toString()); // template_1.->manager, template_2.->User

        documentDetails.setIsRegCerAttached(dscRequestTo.getDocumentDetailsTO().getIsRegCerAttached());
        documentDetails.setIsOtherDocumentAttached(dscRequestTo.getDocumentDetailsTO().getIsOtherDocumentAttached());
        documentDetails.setIsDegreeCardAttached(dscRequestTo.getDocumentDetailsTO().getIsDegreeCardAttached());

        personalDetailTO.setFirstName(dscRequestTo.getDocumentDetailsTO().getPersonalDetail().getFirstName());
        personalDetailTO.setMiddleName(dscRequestTo.getDocumentDetailsTO().getPersonalDetail().getMiddleName());
        personalDetailTO.setLastName(dscRequestTo.getDocumentDetailsTO().getPersonalDetail().getLastName());
        personalDetailTO.setMobileNumber(dscRequestTo.getDocumentDetailsTO().getPersonalDetail().getMobileNumber());
        personalDetailTO.setEmailId(dscRequestTo.getDocumentDetailsTO().getPersonalDetail().getEmailId());
        personalDetailTO.setUserId(dscRequestTo.getDocumentDetailsTO().getPersonalDetail().getUserId());
        personalDetailTO.setQualification(dscRequestTo.getDocumentDetailsTO().getPersonalDetail().getQualification());

        documentDetails.setPersonalDetail(personalDetailTO);

        documentDetails.setPersonalCommunication(dscRequestTo.getDocumentDetailsTO().getPersonalCommunication());
        documentDetails.setOfficeCommunication(dscRequestTo.getDocumentDetailsTO().getOfficeCommunication());

        dscDocument.setDocumentDetails(documentDetails);
        dscDocumentTo.setDscDocument(dscDocument);

        return dscDocumentTo;
    }

}

