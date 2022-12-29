package in.gov.abdm.nmr.db.sql.domain.registration_renewation_type;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegistrationRenewationTypeService implements IRegistrationRenewationTypeService {

    public RegistrationRenewationTypeRepository registrationRenewationTypeRepository;

    private RegistrationRenewationTypeDtoMapper registrationRenewationTypeDtoMapper;

    public RegistrationRenewationTypeService( RegistrationRenewationTypeRepository registrationRenewationTypeRepository, RegistrationRenewationTypeDtoMapper registrationRenewationTypeDtoMapper) {
        this.registrationRenewationTypeRepository = registrationRenewationTypeRepository;
        this.registrationRenewationTypeDtoMapper = registrationRenewationTypeDtoMapper;
    }

    @Override
    public List<RegistrationRenewationTypeTO> getRegistrationRenewationType() {
        return registrationRenewationTypeDtoMapper.RegistrationRenewationTypeDataToDto(registrationRenewationTypeRepository.getRegistrationRenewationType());

    }

}
