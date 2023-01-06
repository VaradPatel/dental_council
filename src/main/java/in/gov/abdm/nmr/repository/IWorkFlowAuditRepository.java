package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import in.gov.abdm.nmr.entity.WorkFlowAudit;

public interface IWorkFlowAuditRepository extends JpaRepository<WorkFlowAudit, BigInteger>{

}
