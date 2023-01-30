package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardRequestTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;

public interface IFetchSpecificDetailsCustomRepository {
    DashboardResponseTO fetchDashboardData(DashboardRequestParamsTO dashboardRequestParamsTO, Pageable pagination);
}
