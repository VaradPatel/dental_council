package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestParamsTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.ITrackApplicationService;
import in.gov.abdm.nmr.repository.IFetchTrackApplicationDetailsCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrackApplicationServiceImpl implements ITrackApplicationService {

    @Value("${max.data.size}")
    private Integer maxSize;
    @Value("${sort.order}")
    private String defaultSortOrder;

    @Autowired
    private IFetchTrackApplicationDetailsCustomRepository iFetchTrackApplicationDetailsCustomRepository;

    @Autowired
    private IUserRepository userDetailRepository;

    @Autowired
    private IHpProfileRepository hpProfileRepository;


    @Override
    public HealthProfessionalApplicationResponseTo fetchApplicationDetails(HealthProfessionalApplicationRequestTo healthProfessionalApplicationRequestTo) {

        HealthProfessionalApplicationRequestParamsTo applicationRequestParamsTo = new HealthProfessionalApplicationRequestParamsTo();
        applicationRequestParamsTo.setSmcId(healthProfessionalApplicationRequestTo.getSmcId());
        applicationRequestParamsTo.setRegistrationNo(healthProfessionalApplicationRequestTo.getRegistrationNo());
        applicationRequestParamsTo.setWorkFlowStatusId(healthProfessionalApplicationRequestTo.getWorkFlowStatusId());
        applicationRequestParamsTo.setApplicationTypeId(healthProfessionalApplicationRequestTo.getApplicationTypeId());

        String sortOrder = healthProfessionalApplicationRequestTo.getSortOrder();
        String column = getColumnToSort(healthProfessionalApplicationRequestTo.getSortBy());
        int size = healthProfessionalApplicationRequestTo.getSize();
        int pageNo = healthProfessionalApplicationRequestTo.getPageNo();

        final String sortingOrder = sortOrder == null ? defaultSortOrder : sortOrder;
        applicationRequestParamsTo.setSortOrder(sortingOrder);
        final int dataLimit = maxSize < size ? maxSize : size;
        Pageable pageable = PageRequest.of(pageNo, dataLimit);
        applicationRequestParamsTo.setSize(size);
        applicationRequestParamsTo.setPageNo(pageNo);
        applicationRequestParamsTo.setSortBy(column);
        return iFetchTrackApplicationDetailsCustomRepository.fetchTrackApplicationDetails(applicationRequestParamsTo, pageable);
    }

    @Override
    public HealthProfessionalApplicationResponseTo fetchApplicationDetailsForHealthProfessional(HealthProfessionalApplicationRequestTo healthProfessionalApplicationRequestTo) {
        if(healthProfessionalApplicationRequestTo.getSmcId() == null && healthProfessionalApplicationRequestTo.getRegistrationNo() == null){
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            User userDetail = userDetailRepository.findByUsername(userName);
            HpProfile hpProfile = hpProfileRepository.findByUserDetail(userDetail.getId());
            healthProfessionalApplicationRequestTo.setRegistrationNo(hpProfile.getRegistrationId().toString());
        }
        return fetchApplicationDetails(healthProfessionalApplicationRequestTo);
    }

    private String getColumnToSort(String columnToSort) {
        Map<String, String> columns;
        if (columnToSort.length() > 0) {
            columns = mapColumnToTable();
            if (columns.containsKey(columnToSort)) {
                return columns.get(columnToSort);
            } else {
                return "Invalid column Name to sort";
            }
        } else {
            return " rd.created_at ";
        }
    }

    private Map<String, String> mapColumnToTable() {
        Map<String, String> columnToSortMap = new HashMap<>();
        columnToSortMap.put("doctorStatus", " doctor_status");
        columnToSortMap.put("smcStatus", " smc_status");
        columnToSortMap.put("collegeDeanStatus", " college_dean_status");
        columnToSortMap.put("collegeRegistrarStatus", " college_registrar_status");
        columnToSortMap.put("nmcStatus", " nmc_status");
        columnToSortMap.put("nbeStatus", " nbe_status");
        columnToSortMap.put("hpProfileId", " calculate.hp_profile_id");
        columnToSortMap.put("requestId", " calculate.request_id");
        columnToSortMap.put("registrationNo", " rd.registration_no");
        columnToSortMap.put("createdAt", " rd.created_at");
        columnToSortMap.put("councilName", " stmc.name");
        columnToSortMap.put("applicantFullName", " hp.full_name");
        columnToSortMap.put("applicationTypeId", " application_type_id");
        return columnToSortMap;
    }
}
