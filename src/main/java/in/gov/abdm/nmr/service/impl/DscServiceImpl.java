package in.gov.abdm.nmr.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.client.DscFClient;
import in.gov.abdm.nmr.dto.dsc.DscDocument;
import in.gov.abdm.nmr.dto.dsc.DscDocumentTo;
import in.gov.abdm.nmr.dto.dsc.DscRequestTo;
import in.gov.abdm.nmr.dto.dsc.DscResponseTo;
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
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(dscFClient.genEspRequest(dscInputData(dscRequestTo)), DscResponseTo.class);
    }

    private DscDocumentTo dscInputData(DscRequestTo dscRequestTo) {
        DscDocumentTo dscDocumentTo = new DscDocumentTo();
        DscDocument dscDocument = new DscDocument();
        dscDocument.setIntegratorName(NMRConstants.DCS_INTEGRATOR_NAME);
        dscDocument.setSigningPlace(dscRequestTo.getSigningPlace());
        if (dscRequestTo.getTemplateId() == null || ("").equals(dscRequestTo.getTemplateId())) {
            dscDocument.setTemplateId(TemplateEnum.TEMPLATE_1.name());
        } else {
            dscDocument.setTemplateId(dscRequestTo.getTemplateId());
        }
        dscDocument.setDocumentDetails(dscRequestTo.getDocumentDetailsTO());
        dscDocument.setAdditionalQualification(dscRequestTo.getAdditionalQualification());
        dscDocumentTo.setDscDocument(dscDocument);
        return dscDocumentTo;
    }
}

