package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkFlowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IWorkFlowStatusRepository extends JpaRepository<WorkFlowStatus, BigInteger> {

    WorkFlowStatus findWorkFLowStatusById(BigInteger id);

}
