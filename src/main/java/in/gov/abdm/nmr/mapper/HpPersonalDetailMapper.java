package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.HpProfile;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;

@UtilityClass
public final class HpPersonalDetailMapper {

    public static HpProfilePersonalResponseTO convertEntitiesToPersonalResponseTo(HpProfile hpProfile, Address address, Address kycAddress, BigInteger applicationTypeId) {
        HpProfilePersonalResponseTO hpProfilePersonalResponseTO = new HpProfilePersonalResponseTO();
        PersonalDetailsTO personalDetailsTO = new PersonalDetailsTO();
        AddressTO addressTO = new AddressTO();
        AddressTO kycAddressTo = new AddressTO();
        IMRDetailsTO imrDetailsTO = new IMRDetailsTO();

        personalDetailsTO.setAadhaarToken(hpProfile.getAadhaarToken());
        personalDetailsTO.setCountryNationality(NationalityTO.builder().id(hpProfile.getCountryNationality().getId()).name(hpProfile.getCountryNationality().getName()).build());
        personalDetailsTO.setDateOfBirth(hpProfile.getDateOfBirth());
        personalDetailsTO.setProfilePhoto(hpProfile.getProfilePhoto());
        personalDetailsTO.setFatherName(hpProfile.getFatherName());
        personalDetailsTO.setGender(hpProfile.getGender());
        personalDetailsTO.setFirstName(hpProfile.getFirstName());
        personalDetailsTO.setMiddleName(hpProfile.getMiddleName());
        personalDetailsTO.setLastName(hpProfile.getLastName());
        personalDetailsTO.setSpouseName(hpProfile.getSpouseName());
        personalDetailsTO.setFullName(hpProfile.getFullName());
        personalDetailsTO.setMotherName(hpProfile.getMotherName());
        personalDetailsTO.setSalutation(hpProfile.getSalutation());
//        personalDetailsTO.setSchedule(ScheduleTO.builder().id(hpProfile.getSchedule().getId()).name(hpProfile.getSchedule().getName()).build());

        if (address != null) {
            addressTO.setAddressLine1(address.getAddressLine1());
            addressTO.setCountry(CountryTO.builder().id(address.getCountry().getId()).name(address.getCountry().getName()).nationality(address.getCountry().getNationality()).build());
            addressTO.setDistrict(DistrictTO.builder().id(address.getDistrict().getId()).name(address.getDistrict().getName()).isoCode(address.getDistrict().getIsoCode()).build());
            addressTO.setSubDistrict(SubDistrictTO.builder().id(address.getSubDistrict().getId()).name(address.getSubDistrict().getName()).isoCode(address.getSubDistrict().getIsoCode()).build());
            addressTO.setState(StateTO.builder().id(address.getState().getId()).name(address.getState().getName()).build());
            addressTO.setVillage(VillagesTO.builder().id(address.getVillage().getId()).name(address.getVillage().getName()).build());
            addressTO.setEmail(address.getEmail());
            addressTO.setMobile(hpProfile.getMobileNumber());
            addressTO.setId(address.getId());
            addressTO.setPincode(address.getPincode());
            addressTO.setHouse(address.getHouse());
            addressTO.setStreet(address.getStreet());
            addressTO.setLocality(address.getLocality());
            addressTO.setLandmark(address.getLandmark());
            addressTO.setIsSameAddress(hpProfile.getIsSameAddress());
        }

        if (kycAddress != null) {
            kycAddressTo.setAddressLine1(kycAddress.getAddressLine1());
            if (kycAddress.getCountry() != null) {
                kycAddressTo.setCountry(CountryTO.builder().id(kycAddress.getCountry().getId()).name(kycAddress.getCountry().getName()).nationality(kycAddress.getCountry().getNationality()).build());
            }
            if (kycAddress.getDistrict() != null) {
                kycAddressTo.setDistrict(DistrictTO.builder().id(kycAddress.getDistrict().getId()).name(kycAddress.getDistrict().getName()).isoCode(kycAddress.getDistrict().getIsoCode()).build());
            }
            if (kycAddress.getSubDistrict() != null) {
                kycAddressTo.setSubDistrict(SubDistrictTO.builder().id(kycAddress.getSubDistrict().getId()).name(kycAddress.getSubDistrict().getName()).isoCode(kycAddress.getSubDistrict().getIsoCode()).build());
            }
            if (kycAddress.getState() != null) {
                kycAddressTo.setState(StateTO.builder().id(kycAddress.getState().getId()).name(kycAddress.getState().getName()).build());
            }
            if (kycAddress.getVillage() != null) {
                kycAddressTo.setVillage(VillagesTO.builder().id(kycAddress.getVillage().getId()).name(kycAddress.getVillage().getName()).build());
            }
            kycAddressTo.setEmail(kycAddress.getEmail());
            kycAddressTo.setMobile(kycAddress.getMobile());
            kycAddressTo.setId(kycAddress.getId());
            kycAddressTo.setPincode(kycAddress.getPincode());
            kycAddressTo.setHouse(kycAddress.getHouse());
            kycAddressTo.setStreet(kycAddress.getStreet());
            kycAddressTo.setLocality(kycAddress.getLocality());
            kycAddressTo.setLandmark(kycAddress.getLandmark());
        }

        imrDetailsTO.setRegistrationNumber(hpProfile.getRegistrationId());
        imrDetailsTO.setYearOfInfo(hpProfile.getYearOfInfo());
        imrDetailsTO.setNmrId(hpProfile.getNmrId());

        hpProfilePersonalResponseTO.setHpProfileId(hpProfile.getId());
        hpProfilePersonalResponseTO.setPersonalDetails(personalDetailsTO);
        hpProfilePersonalResponseTO.setCommunicationAddress(addressTO);
        hpProfilePersonalResponseTO.setKycAddress(kycAddressTo);
        hpProfilePersonalResponseTO.setRequestId(hpProfile.getRequestId());
        hpProfilePersonalResponseTO.setApplicationTypeId(applicationTypeId);
        hpProfilePersonalResponseTO.setNmrId(hpProfile.getNmrId());
        hpProfilePersonalResponseTO.setHpProfileStatusId(hpProfile.getHpProfileStatus().getId());

        return hpProfilePersonalResponseTO;
    }


}
