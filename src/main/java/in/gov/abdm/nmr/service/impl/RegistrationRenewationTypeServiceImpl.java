package in.gov.abdm.nmr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.mapper.RegistrationRenewationTypeDtoMapper;
import in.gov.abdm.nmr.repository.RegistrationRenewationTypeRepository;
import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.service.IRegistrationRenewationTypeService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegistrationRenewationTypeServiceImpl implements IRegistrationRenewationTypeService {

    public RegistrationRenewationTypeRepository registrationRenewationTypeRepository;

    private RegistrationRenewationTypeDtoMapper registrationRenewationTypeDtoMapper;

    public RegistrationRenewationTypeServiceImpl(RegistrationRenewationTypeRepository registrationRenewationTypeRepository, RegistrationRenewationTypeDtoMapper registrationRenewationTypeDtoMapper) {
        this.registrationRenewationTypeRepository = registrationRenewationTypeRepository;
        this.registrationRenewationTypeDtoMapper = registrationRenewationTypeDtoMapper;
    }

    @Override
    public List<RegistrationRenewationTypeTO> getRegistrationRenewationType() {
        return registrationRenewationTypeDtoMapper.RegistrationRenewationTypeDataToDto(registrationRenewationTypeRepository.getRegistrationRenewationType());

    }

}
