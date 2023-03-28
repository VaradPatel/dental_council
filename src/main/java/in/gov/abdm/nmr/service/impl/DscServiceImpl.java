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
        dscDocument.setIntegratorName(NMRConstants.DCS_INTEGRATOR_NAME);
        dscDocument.setSigningPlace(dscRequestTo.getSigningPlace());
        dscDocument.setTemplateId(TemplateEnum.TEMPLATE_1.toString()); // template_1.->manager, template_2.->User

        dscDocument.setDocumentDetails(dscRequestTo.getDocumentDetailsTO());
        dscDocumentTo.setDscDocument(dscDocument);
        return dscDocumentTo;
    }
}

