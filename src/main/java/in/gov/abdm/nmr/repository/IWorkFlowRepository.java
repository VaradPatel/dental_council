package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import in.gov.abdm.nmr.entity.WorkFlow;

public interface IWorkFlowRepository extends JpaRepository<WorkFlow, BigInteger> {

    WorkFlow findByRequestId(String requestId);

}
