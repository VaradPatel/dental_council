package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.WorkProfile;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;


@UtilityClass
public final class HpProfileWorkProfileMapper {

    public static HpProfileWorkDetailsResponseTO convertEntitiesToWorkDetailResponseTo(List<WorkProfile> workProfile) {
        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = new HpProfileWorkDetailsResponseTO();
        List<CurrentWorkDetailsTO> currentWorkDetailsTOList = new ArrayList<>();
        WorkDetailsTO workDetailsTO = new WorkDetailsTO();

        if (workProfile.get(0).getWorkNature() != null) {
            workDetailsTO.setWorkNature(WorkNatureTO.builder().id(workProfile.get(0).getWorkNature().getId()).name(workProfile.get(0).getWorkNature().getName()).build());
        } else {
            workDetailsTO.setWorkNature(null);
        }
        workDetailsTO.setIsUserCurrentlyWorking(workProfile.get(0).getIsUserCurrentlyWorking());
        if (workProfile.get(0).getWorkStatus() != null) {
            workDetailsTO.setWorkStatus(WorkStatusTO.builder().id(workProfile.get(0).getWorkStatus().getId()).name(workProfile.get(0).getWorkStatus().getName()).build());
        } else {
            workDetailsTO.setWorkStatus(null);
        }

        if(workProfile.get(0)!=null){
            workDetailsTO.setReason(workProfile.get(0).getReason());
            workDetailsTO.setRemark(workProfile.get(0).getRemark());
        }
        workDetailsTO.setExperienceInYears(workProfile.get(0).getExperienceInYears());
        workProfile.stream().filter(wp -> wp.getIsUserCurrentlyWorking() != 1).forEach(workProfileObj -> {
            CurrentWorkDetailsTO currentWorkDetailsTO = new CurrentWorkDetailsTO();
            currentWorkDetailsTO.setWorkOrganization(workProfileObj.getWorkOrganization());
            currentWorkDetailsTO.setFacilityId(workProfileObj.getFacilityId());
            currentWorkDetailsTO.setUrl(workProfileObj.getUrl());
            currentWorkDetailsTO.setOrganizationType(workProfileObj.getOrganizationType());
            currentWorkDetailsTO.setFacilityTypeId(workProfileObj.getFacilityTypeId());
            StateTO state;
            DistrictTO district;
            AddressTO address;
            if (workProfileObj.getState() != null) {
                state = StateTO.builder().id(workProfileObj.getState().getId()).name(workProfileObj.getState().getName()).build();
            } else {
                state = null;
            }
            if (workProfileObj.getDistrict() != null) {
                district = DistrictTO.builder().id(workProfileObj.getDistrict().getId()).name(workProfileObj.getDistrict().getName()).build();
            } else {
                district = null;
            }
            if (workProfileObj.getAddress() != null) {
                address = AddressTO.builder().addressLine1(workProfileObj.getAddress()).district(district).state(state).pincode(workProfileObj.getPincode()).build();
            } else {
                address = null;
            }
            currentWorkDetailsTO.setAddress(address);
            currentWorkDetailsTO.setRegistrationNo(workProfileObj.getRegistrationNo());
            currentWorkDetailsTO.setSystemOfMedicine(workProfileObj.getSystemOfMedicine());
            currentWorkDetailsTO.setDesignation(workProfileObj.getDesignation());
            currentWorkDetailsTO.setDepartment(workProfileObj.getDepartment());
            currentWorkDetailsTOList.add(currentWorkDetailsTO);
        });
        hpProfileWorkDetailsResponseTO.setWorkDetails(workDetailsTO);
        hpProfileWorkDetailsResponseTO.setCurrentWorkDetails(currentWorkDetailsTOList);
        hpProfileWorkDetailsResponseTO.setRequestId(workProfile.get(0).getRequestId());
        hpProfileWorkDetailsResponseTO.setHpProfileId(workProfile.get(0).getHpProfileId());




        return hpProfileWorkDetailsResponseTO;
    }
}
