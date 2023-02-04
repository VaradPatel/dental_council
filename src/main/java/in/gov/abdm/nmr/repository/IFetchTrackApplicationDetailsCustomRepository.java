package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestParamsTo;;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import org.springframework.data.domain.Pageable;

public interface IFetchTrackApplicationDetailsCustomRepository {
    HealthProfessionalApplicationResponseTo fetchTrackApplicationDetails(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, Pageable pagination);
}
