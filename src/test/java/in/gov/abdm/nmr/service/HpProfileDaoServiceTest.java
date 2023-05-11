package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.NoDataFoundException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.HpProfileDaoServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HpProfileDaoServiceTest {
    @InjectMocks
    HpProfileDaoServiceImpl hpProfileDaoService;

    @Mock
    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    @Mock
    private ICouncilService councilService;
    @Mock
    private IHpProfileRepository iHpProfileRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private IAddressRepository iAddressRepository;
    @Mock
    private DistrictRepository districtRepository;
    @Mock
    private IStateRepository stateRepository;
    @Mock
    private SubDistrictRepository subDistrictRepository;
    @Mock
    private VillagesRepository villagesRepository;
    @Mock
    IRegistrationDetailRepository iRegistrationDetailRepository;

    /*   @Test
        void testUpdateHpRegistrationDetails() throws NmrException, InvalidRequestException {
            final MultipartFile mockFile = mock(MultipartFile.class);
            hpProfileDaoService.updateHpRegistrationDetails(ID,getHpRegistrationUpdateRequestTo(),mockFile,mockFile);
        }*/
    @Test
    void testFindById() {
        when(iHpProfileRepository.findById(ID)).thenReturn(Optional.of(getHpProfile()));
        HpProfile hpProfile = hpProfileDaoService.findById(ID);
        assertEquals(ID, hpProfile.getId());
        assertEquals(EMAIL_ID, hpProfile.getEmailId());
    }

    @Test
    void testFindLatestEntryByUserid() {
        when(iHpProfileRepository.findLatestEntryByUserid(ID)).thenReturn(getHpProfile());
        HpProfile hpProfile = hpProfileDaoService.findLatestEntryByUserid(ID);
        assertEquals(ID, hpProfile.getId());
        assertEquals(EMAIL_ID, hpProfile.getEmailId());
    }

    @Test
    void testFetchSmcRegistrationDetailShouldReturnMatchingHealthProfessionalDetails() throws NmrException, NoDataFoundException {
        when(stateMedicalCouncilRepository.findStateMedicalCouncilById(any(BigInteger.class))).thenReturn(CommonTestData.getStateMedicalCouncil());
        when(councilService.getCouncilByRegistrationNumberAndCouncilName(anyString(), anyString())).thenReturn(List.of(CommonTestData.getImrCouncilDetails()));

        HpSmcDetailTO hpSmcDetailTO = hpProfileDaoService.fetchSmcRegistrationDetail(1, REGISTRATION_NUMBER);
        assertEquals(PROFILE_DISPLAY_NAME, hpSmcDetailTO.getHpName());
        assertEquals(REGISTRATION_NUMBER, hpSmcDetailTO.getRegistrationNumber());
        assertEquals(STATE_MEDICAL_COUNCIL, hpSmcDetailTO.getCouncilName());
    }

    @Test
    void testUpdateHpPersonalDetailsWithExistingProfileWithPendingHpId() {
        when(iHpProfileRepository.findById(ID)).thenReturn(Optional.of(getHpProfileInApprovedStatus()));
        when(iHpProfileRepository.findLatestHpProfileByRegistrationId(REGISTRATION_NUMBER)).thenReturn(getHpProfile());
        assertThrows(InvalidRequestException.class, () -> hpProfileDaoService.updateHpPersonalDetails(ID, getHpPersonalUpdateRequestTo()));
    }

    @Test
    void testUpdateHpPersonalDetailsWithExistingProfile() throws InvalidRequestException, WorkFlowException {
        when(iHpProfileRepository.findById(ID)).thenReturn(Optional.of(getHpProfileInApprovedStatus()));
        when(iHpProfileRepository.findLatestHpProfileByRegistrationId(REGISTRATION_NUMBER)).thenReturn(getHpProfileInApprovedStatus());
        when(countryRepository.findById(ID)).thenReturn(Optional.of(getCountry()));
        when(iHpProfileRepository.save(ArgumentMatchers.any())).thenReturn(getHpProfileInApprovedStatus());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(ID, 4)).thenReturn(getCommunicationAddress());
        Address newAddress = getCommunicationAddress();
        when(countryRepository.findById(newAddress.getCountry().getId())).thenReturn(Optional.of(getCountry()));
        when(stateRepository.findById(newAddress.getState().getId())).thenReturn(Optional.of(getState()));
        when(districtRepository.findById(newAddress.getDistrict().getId())).thenReturn(Optional.of(getDistrict()));
        when(subDistrictRepository.findById(newAddress.getSubDistrict().getId())).thenReturn(Optional.of(getSubDistrict()));
        when(villagesRepository.findById(newAddress.getVillage().getId())).thenReturn(Optional.of(getVillage()));
        when(iAddressRepository.save(ArgumentMatchers.any())).thenReturn(newAddress);
        when(iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(ID)).thenReturn(new RegistrationDetails());
        when(iRegistrationDetailRepository.save(ArgumentMatchers.any())).thenReturn(new RegistrationDetails());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(ID, 5)).thenReturn(getKYCAddress());
        when(iAddressRepository.save(ArgumentMatchers.any())).thenReturn(getKYCAddress());
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpPersonalDetails(ID, getHpPersonalUpdateRequestTo());
        assertEquals(ID, hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Test
    void testUpdateHpPersonalDetails() throws InvalidRequestException, WorkFlowException {
        when(iHpProfileRepository.findById(ID)).thenReturn(Optional.of(getHpProfile()));
        when(countryRepository.findById(ID)).thenReturn(Optional.of(getCountry()));
        when(iAddressRepository.getCommunicationAddressByHpProfileId(ID, 4)).thenReturn(getCommunicationAddress());
        Address newAddress = getCommunicationAddress();
        when(countryRepository.findById(newAddress.getCountry().getId())).thenReturn(Optional.of(getCountry()));
        when(stateRepository.findById(newAddress.getState().getId())).thenReturn(Optional.of(getState()));
        when(districtRepository.findById(newAddress.getDistrict().getId())).thenReturn(Optional.of(getDistrict()));
        when(subDistrictRepository.findById(newAddress.getSubDistrict().getId())).thenReturn(Optional.of(getSubDistrict()));
        when(villagesRepository.findById(newAddress.getVillage().getId())).thenReturn(Optional.of(getVillage()));
        when(iAddressRepository.save(ArgumentMatchers.any())).thenReturn(newAddress);
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpPersonalDetails(ID, getHpPersonalUpdateRequestTo());
        assertEquals(ID, hpProfileUpdateResponseTO.getHpProfileId());
    }
}
