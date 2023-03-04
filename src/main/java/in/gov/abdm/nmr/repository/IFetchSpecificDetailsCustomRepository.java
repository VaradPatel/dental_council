package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import org.springframework.data.domain.Pageable;

/**
 * An interface for the customized repository of Dashboard Fetch Specific Details repository
 */
public interface IFetchSpecificDetailsCustomRepository {
    DashboardResponseTO fetchDashboardData(DashboardRequestParamsTO dashboardRequestParamsTO, Pageable pagination);
}
