package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.Language;
import in.gov.abdm.nmr.entity.LanguagesKnown;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.util.List;

@UtilityClass
public final class HpPersonalDetailMapper {

    public static HpProfilePersonalResponseTO convertEntitiesToPersonalResponseTo(HpProfile hpProfile, Address address, List<LanguagesKnown> languageKnowns, List<Language> languages, BigInteger applicationTypeId){
        HpProfilePersonalResponseTO hpProfilePersonalResponseTO = new HpProfilePersonalResponseTO();
        PersonalDetailsTO personalDetailsTO = new PersonalDetailsTO();
        AddressTO addressTO =  new AddressTO();
        IMRDetailsTO imrDetailsTO = new IMRDetailsTO();

        personalDetailsTO.setAadhaarToken(hpProfile.getAadhaarToken());
        personalDetailsTO.setCountryNationality(NationalityTO.builder().id(hpProfile.getCountryNationality().getId()).name(hpProfile.getCountryNationality().getName()).build());
        personalDetailsTO.setDateOfBirth(hpProfile.getDateOfBirth());
        personalDetailsTO.setProfilePhoto(hpProfile.getProfilePhoto());
        personalDetailsTO.setFatherName(hpProfile.getFatherName());
        personalDetailsTO.setGender(hpProfile.getGender());
        personalDetailsTO.setLanguage(convertLanguageEntityToLanguageDto(languageKnowns,languages));
        personalDetailsTO.setFirstName(hpProfile.getFirstName());
        personalDetailsTO.setMiddleName(hpProfile.getMiddleName());
        personalDetailsTO.setLastName(hpProfile.getLastName());
        personalDetailsTO.setSpouseName(hpProfile.getSpouseName());
        personalDetailsTO.setFullName(hpProfile.getFullName());
        personalDetailsTO.setMotherName(hpProfile.getMotherName());
        personalDetailsTO.setSalutation(hpProfile.getSalutation());
        personalDetailsTO.setSchedule(ScheduleTO.builder().id(hpProfile.getSchedule().getId()).name(hpProfile.getSchedule().getName()).build());

        addressTO.setAddressLine1(address.getAddressLine1());
        addressTO.setCountry(CountryTO.builder().id(address.getCountry().getId()).name(address.getCountry().getName()).nationality(address.getCountry().getNationality()).build());
        addressTO.setDistrict(DistrictTO.builder().id(address.getDistrict().getId()).name(address.getDistrict().getName()).isoCode(address.getDistrict().getIsoCode()).build());
        addressTO.setSubDistrict(SubDistrictTO.builder().id(address.getSubDistrict().getId()).name(address.getSubDistrict().getName()).isoCode(address.getSubDistrict().getIsoCode()).build());
        addressTO.setState(StateTO.builder().id(address.getState().getId()).name(address.getState().getName()).build());
        addressTO.setVillage(VillagesTO.builder().id(address.getVillage().getId()).name(address.getVillage().getName()).build());
        addressTO.setEmail(address.getEmail());
        addressTO.setMobile(address.getMobile());
        addressTO.setId(address.getId());
        addressTO.setPincode(address.getPincode());
        addressTO.setHouse(address.getHouse());
        addressTO.setStreet(address.getStreet());
        addressTO.setLocality(address.getLocality());
        addressTO.setLandmark(address.getLandmark());
        addressTO.setIsSameAddress(hpProfile.getIsSameAddress());


        imrDetailsTO.setRegistrationNumber(hpProfile.getRegistrationId());
        imrDetailsTO.setYearOfInfo(hpProfile.getYearOfInfo());
        imrDetailsTO.setNmrId(hpProfile.getNmrId());

        hpProfilePersonalResponseTO.setHpProfileId(hpProfile.getId());
        hpProfilePersonalResponseTO.setPersonalDetails(personalDetailsTO);
        hpProfilePersonalResponseTO.setCommunicationAddress(addressTO);
        hpProfilePersonalResponseTO.setRequestId(hpProfile.getRequestId());
        hpProfilePersonalResponseTO.setApplicationTypeId(applicationTypeId);

        return hpProfilePersonalResponseTO;
    }

    private List<LanguageTO> convertLanguageEntityToLanguageDto(List<LanguagesKnown> languagesKnowns, List<Language> languages) {
        return languagesKnowns.stream().map(language -> LanguageTO.builder().id(language.getLanguageId()).name(languages.stream().filter(l->l.getId().equals(language.getLanguageId())).findFirst().get().getName()).build()).toList();
    }

}
