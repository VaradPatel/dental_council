package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.WorkFlowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IWorkFlowStatusRepository extends JpaRepository<WorkFlowStatus, BigInteger> {

    WorkFlowStatus findWorkFLowStatusById(BigInteger id);

}
