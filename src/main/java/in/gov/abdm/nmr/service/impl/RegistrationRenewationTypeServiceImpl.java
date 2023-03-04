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

    private RegistrationRenewationTypeDtoMapper registrationRenewationTypeDtoMapper;

    public RegistrationRenewationTypeServiceImpl(RegistrationRenewationTypeRepository registrationRenewationTypeRepository, RegistrationRenewationTypeDtoMapper registrationRenewationTypeDtoMapper) {
        this.registrationRenewationTypeRepository = registrationRenewationTypeRepository;
        this.registrationRenewationTypeDtoMapper = registrationRenewationTypeDtoMapper;
    }

    @Override
    public List<RegistrationRenewationTypeTO> getRegistrationRenewationType() {
        return registrationRenewationTypeDtoMapper.registrationRenewationTypeDataToDto(registrationRenewationTypeRepository.getRegistrationRenewationType());

    }

}
