package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.mapper.RegistrationRenewationTypeDtoMapper;
import in.gov.abdm.nmr.repository.RegistrationRenewationTypeRepository;
import in.gov.abdm.nmr.service.IRegistrationRenewationTypeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RegistrationRenewationTypeServiceImpl implements IRegistrationRenewationTypeService {

    private RegistrationRenewationTypeRepository registrationRenewationTypeRepository;

    public RegistrationRenewationTypeServiceImpl(RegistrationRenewationTypeRepository registrationRenewationTypeRepository) {
        this.registrationRenewationTypeRepository = registrationRenewationTypeRepository;
    }

    @Override
    public List<RegistrationRenewationTypeTO> getRegistrationRenewationType() {
        return RegistrationRenewationTypeDtoMapper.REGISTRATION_RENEWATION_TYPE_DTO_MAPPER.registrationRenewationTypeDataToDto(registrationRenewationTypeRepository.getRegistrationRenewationType());

    }

}
