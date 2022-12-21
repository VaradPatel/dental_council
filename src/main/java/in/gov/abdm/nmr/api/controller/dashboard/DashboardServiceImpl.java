package in.gov.abdm.nmr.api.controller.dashboard;

import in.gov.abdm.nmr.api.controller.dashboard.to.IStatusWiseCountMapper;
import in.gov.abdm.nmr.api.controller.dashboard.to.ResponseTO;
import in.gov.abdm.nmr.api.controller.dashboard.to.StatusWiseCountTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static in.gov.abdm.nmr.api.constant.NMRConstants.TOTAL_REGISTRATION_REQUESTS;
import static in.gov.abdm.nmr.api.constant.NMRConstants.TOTAL_UPDATION_REQUESTS;


@Service
public class DashboardServiceImpl implements DashboardService{

    /**
     * Injecting a Dashboard Repository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private DashboardRepository dashboardRepository;

    /**
     * Mapper Interface to transform the StatusWiseCount Bean
     * to the StatusWiseCountTO Bean transferring its contents
     */
    @Autowired
    private IStatusWiseCountMapper iStatusWiseCountMapper;

    @Override
    public ResponseTO fetchCountOnCard() {

        List<StatusWiseCountTO> registrationRequests=dashboardRepository.fetchCountOnCard(1)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        registrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_REGISTRATION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.findByIsNewApplication(1).size()))
                .build());

        List<StatusWiseCountTO> updationRequests=dashboardRepository.fetchCountOnCard(0)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        updationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_UPDATION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.findByIsNewApplication(0).size()))
                .build());

        return ResponseTO.builder()
                .registrationRequests(registrationRequests)
                .updationRequests(updationRequests)
                .build();
    }
}
