package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import in.gov.abdm.nmr.entity.WorkFlowStatus;

public interface IWorkFlowStatusRepository extends JpaRepository<WorkFlowStatus, BigInteger> {

    WorkFlowStatus findWorkFLowStatusById(BigInteger id);

}
