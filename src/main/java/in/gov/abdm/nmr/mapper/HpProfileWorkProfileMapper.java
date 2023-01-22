package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.SuperSpeciality;
import in.gov.abdm.nmr.entity.WorkProfile;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class HpProfileWorkProfileMapper {
    public static HpProfileWorkDetailsResponseTO convertEntitiesToWorkDetailResponseTo(List<SuperSpeciality> superSpecialities, WorkProfile workProfile) {
        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO =  new HpProfileWorkDetailsResponseTO();
        SpecialityDetailsTO specialityDetailsTO =  new SpecialityDetailsTO();
        WorkDetailsTO workDetailsTO =  new WorkDetailsTO();
        CurrentWorkDetailsTO currentWorkDetailsTO  = new CurrentWorkDetailsTO();
        specialityDetailsTO.setSuperSpeciality(superSpecialities.stream().map(superSpeciality -> SuperSpecialityTO.builder().id(superSpeciality.getId()).name(superSpeciality.getName()).build()).toList());
        specialityDetailsTO.setBroadSpeciality(BroadSpecialityTO.builder().name(workProfile.getBroadSpeciality().getName()).id(workProfile.getBroadSpeciality().getId()).build());
        workDetailsTO.setWorkNature(WorkNatureTO.builder().id(workProfile.getWorkNature().getId()).name(workProfile.getWorkNature().getName()).build());
        workDetailsTO.setIsUserCurrentlyWorking(workDetailsTO.getIsUserCurrentlyWorking());
        workDetailsTO.setWorkStatus(WorkStatusTO.builder().id(workProfile.getWorkStatus().getId()).name(workProfile.getWorkStatus().getName()).build());

        currentWorkDetailsTO.setWorkOrganization(workProfile.getWorkOrganization());
        currentWorkDetailsTO.setFacility(workProfile.getFacility());
        currentWorkDetailsTO.setUrl(workProfile.getUrl());
        currentWorkDetailsTO.setOrganizationType(OrganizationTypeTO.builder().id(workProfile.getOrganizationType()).build());

        hpProfileWorkDetailsResponseTO.setWorkDetails(workDetailsTO);
        hpProfileWorkDetailsResponseTO.setSpecialityDetails(specialityDetailsTO);
        hpProfileWorkDetailsResponseTO.setCurrentWorkDetails(currentWorkDetailsTO);
        return hpProfileWorkDetailsResponseTO;
    }
}
