package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.dto.UserRequestParamsTO;
import in.gov.abdm.nmr.dto.UserResponseTO;
import org.springframework.data.domain.Pageable;

public interface IFetchUserDetailsCustomRepository {
    UserResponseTO fetchUserData(UserRequestParamsTO userRequestParamsTO, Pageable pageable);
}
