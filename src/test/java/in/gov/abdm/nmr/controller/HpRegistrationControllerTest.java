package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.service.ICouncilService;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IQueriesService;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = HpRegistrationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = HpRegistrationController.class)
@ActiveProfiles(profiles = "local")
@SuppressWarnings("java:S1192")
class HpRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    IHpRegistrationService hpService;

    @MockBean
    IQueriesService queryService;

    @MockBean
    ICouncilService councilService;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;

    SmcRegistrationDetailResponseTO detailResponseTO;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        detailResponseTO = new SmcRegistrationDetailResponseTO();
        detailResponseTO.setHpName(HP_NAME);
        detailResponseTO.setRegistrationNumber(REGISTRATION_NUMBER);
        detailResponseTO.setCouncilName(STATE_MEDICAL_COUNCIL);
        detailResponseTO.setHpProfileId(HP_ID);
        detailResponseTO.setEmailId(EMAIL_ID);
    }


    @Test
    @WithMockUser
    void testFetchSmcRegistrationDetail() throws Exception {
        when(hpService.fetchSmcRegistrationDetail(any(Integer.class), any(String.class)))
                .thenReturn(detailResponseTO);
        mockMvc.perform(get("/health-professional").queryParam("smcId", SMC_ID.toString()).queryParam(REGISTRATION_NUM_IN_LOWER_CASE, REGISTRATION_NUMBER)
                        .with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hp_name").value(HP_NAME))
                .andExpect(jsonPath("$.registration_number").value(REGISTRATION_NUMBER))
                .andExpect(jsonPath("$.council_name").value(STATE_MEDICAL_COUNCIL))
                .andExpect(jsonPath("$.hp_profile_id").value(HP_ID))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID));
    }


    @Test
    @WithMockUser
    void testAddHealthProfessionalPersonalDetail() throws Exception {
        HpPersonalUpdateRequestTO requestTO = new HpPersonalUpdateRequestTO();
        PersonalDetailsTO personalDetails = new PersonalDetailsTO();
        personalDetails.setSalutation(SALUTATION_DR);
        personalDetails.setAadhaarToken(TRANSACTION_ID);
        personalDetails.setFirstName(FIRST_NAME);
        personalDetails.setMiddleName(MIDDLE_NAME);
        personalDetails.setLastName(LAST_NAME);
        personalDetails.setFatherName(MIDDLE_NAME);
        personalDetails.setMotherName(MIDDLE_NAME);
        personalDetails.setSpouseName(FIRST_NAME);
        personalDetails.setCountryNationality(NationalityTO.builder().id(COUNTRY_ID).name(COUNTRY_NAME).build());
        personalDetails.setDateOfBirth(DATE_OF_BIRTH);
        personalDetails.setGender(GENDER);
        personalDetails.setProfilePhoto(PROFILE_PHOTO);
        personalDetails.setFullName(PROFILE_DISPLAY_NAME);
        personalDetails.setIsNew(true);
        personalDetails.setEmail(EMAIL_ID);
        personalDetails.setMobile(MOBILE_NUMBER);
        requestTO.setPersonalDetails(personalDetails);

        CommunicationAddressTO communicationAddress = new CommunicationAddressTO();
        communicationAddress.setId(ID);
        communicationAddress.setCountry(new CountryTO(COUNTRY_ID, COUNTRY_NAME, LOCALITY));
        communicationAddress.setState(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        communicationAddress.setDistrict(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        communicationAddress.setVillage(new VillagesTO(VILLAGE_ID, VILLAGE_NAME));
        communicationAddress.setPincode(PIN_CODE);
        communicationAddress.setAddressLine1(ADDRESS_LINE_1);
        communicationAddress.setHouse(HOUSE);
        communicationAddress.setStreet(STREET);
        communicationAddress.setLocality(LOCALITY);
        communicationAddress.setLandmark(LANDMARK);
        communicationAddress.setIsSameAddress("true");
        communicationAddress.setEmail(EMAIL_ID);
        communicationAddress.setMobile(MOBILE_NUMBER);
        communicationAddress.setAddressType(new AddressTypeTO());
        communicationAddress.setCreatedAt(CURRENT_DATE);
        communicationAddress.setUpdatedAt(CURRENT_DATE);
        communicationAddress.setFullName(FIRST_NAME);
        requestTO.setCommunicationAddress(communicationAddress);
        requestTO.setRequestId(REQUEST_ID);

        HpProfilePersonalResponseTO hpProfilePersonalResponseTO = new HpProfilePersonalResponseTO();

        AddressTO address = new AddressTO();
        address.setId(ID);
        address.setCountry(new CountryTO(COUNTRY_ID, COUNTRY_NAME, LOCALITY));
        address.setState(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        address.setDistrict(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        address.setVillage(new VillagesTO(VILLAGE_ID, VILLAGE_NAME));
        address.setSubDistrict(new SubDistrictTO(SUB_DISTRICT_ID, SUB_DISTRICT_NAME, SUB_DISTRICT_CODE));
        address.setPincode(PIN_CODE);
        address.setAddressLine1(ADDRESS_LINE_1);
        address.setHouse(HOUSE);
        address.setStreet(STREET);
        address.setLocality(LOCALITY);
        address.setLandmark(LANDMARK);
        address.setIsSameAddress("true");
        address.setEmail(NMRConstants.EMAIL_ID);
        address.setMobile(MOBILE_NUMBER);
        address.setAddressType(new AddressTypeTO(ADDRESS_TYPE_ID, ADDRESS_TYPE_NAME));
        address.setCreatedAt(CURRENT_DATE);
        address.setUpdatedAt(CURRENT_DATE);
        address.setFullName(PROFILE_DISPLAY_NAME);

        hpProfilePersonalResponseTO.setPersonalDetails(personalDetails);
        hpProfilePersonalResponseTO.setCommunicationAddress(address);
        hpProfilePersonalResponseTO.setKycAddress(address);
        hpProfilePersonalResponseTO.setHpProfileId(HP_ID);
        hpProfilePersonalResponseTO.setRequestId(REQUEST_ID);
        hpProfilePersonalResponseTO.setApplicationTypeId(ID);
        hpProfilePersonalResponseTO.setNmrId(NMR_ID);
        hpProfilePersonalResponseTO.setHpProfileStatusId(ID);
        hpProfilePersonalResponseTO.setWorkFlowStatusId(ID);
        hpProfilePersonalResponseTO.setEmailVerified(true);
        when(hpService.addOrUpdateHpPersonalDetail(nullable(BigInteger.class), any(HpPersonalUpdateRequestTO.class))).thenReturn(hpProfilePersonalResponseTO);
        mockMvc.perform(post("/health-professional/personal")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personal_details.salutation").value(SALUTATION_DR))
                .andExpect(jsonPath("$.personal_details.aadhaar_token").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.personal_details.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.personal_details.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.personal_details.father_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.mother_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.spouse_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.personal_details.country_nationality.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.personal_details.country_nationality.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.personal_details.gender").value(GENDER))
                .andExpect(jsonPath("$.personal_details.schedule.id").value(CommonTestData.SCHEDULE_ID))
                .andExpect(jsonPath("$.personal_details.schedule.name").value(SCHEDULE_NAME))
                .andExpect(jsonPath("$.personal_details.profile_photo").value(PROFILE_PHOTO))
                .andExpect(jsonPath("$.personal_details.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.personal_details.email").value(CommonTestData.EMAIL_ID))
                .andExpect(jsonPath("$.personal_details.mobile").value(MOBILE_NUMBER))
                .andExpect(jsonPath("$.communication_address.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.communication_address.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.communication_address.country.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.communication_address.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.communication_address.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.communication_address.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.communication_address.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.communication_address.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.communication_address.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.communication_address.village.name").value(VILLAGE_NAME))
                .andExpect(jsonPath("$.communication_address.sub_district.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.communication_address.sub_district.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.communication_address.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.communication_address.address_type.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.communication_address.address_type.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.communication_address.created_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communication_address.updated_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communication_address.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.communication_address.house").value(HOUSE))
                .andExpect(jsonPath("$.communication_address.street").value(STREET))
                .andExpect(jsonPath("$.communication_address.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.communication_address.locality").value(LOCALITY))
                .andExpect(jsonPath("$.communication_address.is_same_address").value("true"))
                .andExpect(jsonPath("$.kyc_address.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.kyc_address.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.kyc_address.country.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.kyc_address.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.kyc_address.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.kyc_address.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.kyc_address.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.kyc_address.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.kyc_address.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.kyc_address.village.name").value(VILLAGE_NAME))
                .andExpect(jsonPath("$.kyc_address.sub_district.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.kyc_address.sub_district.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.kyc_address.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.kyc_address.address_type.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.kyc_address.address_type.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.kyc_address.created_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.kyc_address.updated_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.kyc_address.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.kyc_address.house").value(HOUSE))
                .andExpect(jsonPath("$.kyc_address.street").value(STREET))
                .andExpect(jsonPath("$.kyc_address.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.kyc_address.locality").value(LOCALITY))
                .andExpect(jsonPath("$.kyc_address.is_same_address").value("true"))
                .andExpect(jsonPath("$.hp_profile_id").value(HP_ID))
                .andExpect(jsonPath("$.request_id").value(CommonTestData.REQUEST_ID))
                .andExpect(jsonPath("$.application_type_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.hp_profile_status_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.work_flow_status_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.email_verified").value(true));

    }


    @Test
    @WithMockUser
    void testUpdateHealthProfessionalPersonalDetail() throws Exception {
        HpPersonalUpdateRequestTO requestTO = new HpPersonalUpdateRequestTO();
        PersonalDetailsTO personalDetails = new PersonalDetailsTO();
        personalDetails.setSalutation(SALUTATION_DR);
        personalDetails.setAadhaarToken(TRANSACTION_ID);
        personalDetails.setFirstName(FIRST_NAME);
        personalDetails.setMiddleName(MIDDLE_NAME);
        personalDetails.setLastName(LAST_NAME);
        personalDetails.setFatherName(MIDDLE_NAME);
        personalDetails.setMotherName(MIDDLE_NAME);
        personalDetails.setSpouseName(FIRST_NAME);
        personalDetails.setCountryNationality(NationalityTO.builder().id(COUNTRY_ID).name(COUNTRY_NAME).build());
        personalDetails.setDateOfBirth(DATE_OF_BIRTH);
        personalDetails.setGender(GENDER);

        personalDetails.setProfilePhoto(PROFILE_PHOTO);
        personalDetails.setFullName(PROFILE_DISPLAY_NAME);
        personalDetails.setIsNew(true);
        personalDetails.setEmail(EMAIL_ID);
        personalDetails.setMobile(MOBILE_NUMBER);
        requestTO.setPersonalDetails(personalDetails);

        CommunicationAddressTO communicationAddress = new CommunicationAddressTO();
        communicationAddress.setId(ID);
        communicationAddress.setCountry(new CountryTO(COUNTRY_ID, COUNTRY_NAME, LOCALITY));
        communicationAddress.setState(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        communicationAddress.setDistrict(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        communicationAddress.setVillage(new VillagesTO(VILLAGE_ID, VILLAGE_NAME));
        communicationAddress.setPincode(PIN_CODE);
        communicationAddress.setAddressLine1(ADDRESS_LINE_1);
        communicationAddress.setHouse(HOUSE);
        communicationAddress.setStreet(STREET);
        communicationAddress.setLocality(LOCALITY);
        communicationAddress.setLandmark(LANDMARK);
        communicationAddress.setIsSameAddress("true");
        communicationAddress.setEmail(EMAIL_ID);
        communicationAddress.setMobile(MOBILE_NUMBER);
        communicationAddress.setAddressType(new AddressTypeTO());
        communicationAddress.setCreatedAt(CURRENT_DATE);
        communicationAddress.setUpdatedAt(CURRENT_DATE);
        communicationAddress.setFullName(FIRST_NAME);
        requestTO.setCommunicationAddress(communicationAddress);
        requestTO.setRequestId(REQUEST_ID);

        HpProfilePersonalResponseTO responseTO = new HpProfilePersonalResponseTO();

        AddressTO address = new AddressTO();
        address.setId(ID);
        address.setCountry(new CountryTO(COUNTRY_ID, COUNTRY_NAME, LOCALITY));
        address.setState(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        address.setDistrict(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        address.setVillage(new VillagesTO(VILLAGE_ID, VILLAGE_NAME));
        address.setSubDistrict(new SubDistrictTO(SUB_DISTRICT_ID, SUB_DISTRICT_NAME, SUB_DISTRICT_CODE));
        address.setPincode(PIN_CODE);
        address.setAddressLine1(ADDRESS_LINE_1);
        address.setHouse(HOUSE);
        address.setStreet(STREET);
        address.setLocality(LOCALITY);
        address.setLandmark(LANDMARK);
        address.setIsSameAddress("true");
        address.setEmail(NMRConstants.EMAIL_ID);
        address.setMobile(MOBILE_NUMBER);
        address.setAddressType(new AddressTypeTO(ADDRESS_TYPE_ID, ADDRESS_TYPE_NAME));
        address.setCreatedAt(CURRENT_DATE);
        address.setUpdatedAt(CURRENT_DATE);
        address.setFullName(PROFILE_DISPLAY_NAME);

        responseTO.setPersonalDetails(personalDetails);
        responseTO.setCommunicationAddress(address);
        responseTO.setKycAddress(address);
        responseTO.setHpProfileId(HP_ID);
        responseTO.setRequestId(REQUEST_ID);
        responseTO.setApplicationTypeId(ID);
        responseTO.setNmrId(NMR_ID);
        responseTO.setHpProfileStatusId(ID);
        responseTO.setWorkFlowStatusId(ID);
        responseTO.setEmailVerified(true);
        when(hpService.addOrUpdateHpPersonalDetail(any(BigInteger.class), any(HpPersonalUpdateRequestTO.class))).thenReturn(responseTO);
        mockMvc.perform(put("/health-professional/1/personal")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personal_details.salutation").value(SALUTATION_DR))
                .andExpect(jsonPath("$.personal_details.aadhaar_token").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.personal_details.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.personal_details.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.personal_details.father_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.mother_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.spouse_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.personal_details.country_nationality.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.personal_details.country_nationality.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.personal_details.gender").value(GENDER))
                .andExpect(jsonPath("$.personal_details.schedule.id").value(CommonTestData.SCHEDULE_ID))
                .andExpect(jsonPath("$.personal_details.schedule.name").value(SCHEDULE_NAME))
                .andExpect(jsonPath("$.personal_details.profile_photo").value(PROFILE_PHOTO))
                .andExpect(jsonPath("$.personal_details.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.personal_details.email").value(CommonTestData.EMAIL_ID))
                .andExpect(jsonPath("$.personal_details.mobile").value(MOBILE_NUMBER))

                .andExpect(jsonPath("$.communication_address.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.communication_address.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.communication_address.country.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.communication_address.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.communication_address.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.communication_address.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.communication_address.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.communication_address.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.communication_address.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.communication_address.village.name").value(VILLAGE_NAME))
                .andExpect(jsonPath("$.communication_address.sub_district.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.communication_address.sub_district.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.communication_address.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.communication_address.address_type.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.communication_address.address_type.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.communication_address.created_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communication_address.updated_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communication_address.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.communication_address.house").value(HOUSE))
                .andExpect(jsonPath("$.communication_address.street").value(STREET))
                .andExpect(jsonPath("$.communication_address.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.communication_address.locality").value(LOCALITY))
                .andExpect(jsonPath("$.communication_address.is_same_address").value("true"))

                .andExpect(jsonPath("$.kyc_address.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.kyc_address.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.kyc_address.country.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.kyc_address.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.kyc_address.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.kyc_address.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.kyc_address.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.kyc_address.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.kyc_address.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.kyc_address.village.name").value(VILLAGE_NAME))
                .andExpect(jsonPath("$.kyc_address.sub_district.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.kyc_address.sub_district.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.kyc_address.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.kyc_address.address_type.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.kyc_address.address_type.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.kyc_address.created_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.kyc_address.updated_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.kyc_address.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.kyc_address.house").value(HOUSE))
                .andExpect(jsonPath("$.kyc_address.street").value(STREET))
                .andExpect(jsonPath("$.kyc_address.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.kyc_address.locality").value(LOCALITY))
                .andExpect(jsonPath("$.kyc_address.is_same_address").value("true"))

                .andExpect(jsonPath("$.hp_profile_id").value(HP_ID))
                .andExpect(jsonPath("$.request_id").value(CommonTestData.REQUEST_ID))
                .andExpect(jsonPath("$.application_type_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.hp_profile_status_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.work_flow_status_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.email_verified").value(true));
    }


    @Test
    @WithMockUser
    void testGetHealthProfessionalRegistrationDetail() throws Exception {
        HpProfilePersonalResponseTO responseTO = new HpProfilePersonalResponseTO();
        PersonalDetailsTO personalDetails = new PersonalDetailsTO();
        personalDetails.setSalutation(SALUTATION_DR);
        personalDetails.setAadhaarToken(TRANSACTION_ID);
        personalDetails.setFirstName(FIRST_NAME);
        personalDetails.setMiddleName(MIDDLE_NAME);
        personalDetails.setLastName(LAST_NAME);
        personalDetails.setFatherName(MIDDLE_NAME);
        personalDetails.setMotherName(MIDDLE_NAME);
        personalDetails.setSpouseName(FIRST_NAME);
        personalDetails.setCountryNationality(NationalityTO.builder().id(COUNTRY_ID).name(COUNTRY_NAME).build());
        personalDetails.setDateOfBirth(DATE_OF_BIRTH);
        personalDetails.setGender(GENDER);
        personalDetails.setProfilePhoto(PROFILE_PHOTO);
        personalDetails.setFullName(PROFILE_DISPLAY_NAME);
        personalDetails.setIsNew(true);
        personalDetails.setEmail(EMAIL_ID);
        personalDetails.setMobile(MOBILE_NUMBER);

        CommunicationAddressTO communicationAddress = new CommunicationAddressTO();
        communicationAddress.setId(ID);
        communicationAddress.setCountry(new CountryTO(COUNTRY_ID, COUNTRY_NAME, LOCALITY));
        communicationAddress.setState(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        communicationAddress.setDistrict(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        communicationAddress.setVillage(new VillagesTO(VILLAGE_ID, VILLAGE_NAME));
        communicationAddress.setPincode(PIN_CODE);
        communicationAddress.setAddressLine1(ADDRESS_LINE_1);
        communicationAddress.setHouse(HOUSE);
        communicationAddress.setStreet(STREET);
        communicationAddress.setLocality(LOCALITY);
        communicationAddress.setLandmark(LANDMARK);
        communicationAddress.setIsSameAddress("true");
        communicationAddress.setEmail(EMAIL_ID);
        communicationAddress.setMobile(MOBILE_NUMBER);
        communicationAddress.setAddressType(new AddressTypeTO());
        communicationAddress.setCreatedAt(CURRENT_DATE);
        communicationAddress.setUpdatedAt(CURRENT_DATE);
        communicationAddress.setFullName(FIRST_NAME);

        AddressTO address = new AddressTO();
        address.setId(ID);
        address.setCountry(new CountryTO(COUNTRY_ID, COUNTRY_NAME, LOCALITY));
        address.setState(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        address.setDistrict(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        address.setVillage(new VillagesTO(VILLAGE_ID, VILLAGE_NAME));
        address.setSubDistrict(new SubDistrictTO(SUB_DISTRICT_ID, SUB_DISTRICT_NAME, SUB_DISTRICT_CODE));
        address.setPincode(PIN_CODE);
        address.setAddressLine1(ADDRESS_LINE_1);
        address.setHouse(HOUSE);
        address.setStreet(STREET);
        address.setLocality(LOCALITY);
        address.setLandmark(LANDMARK);
        address.setIsSameAddress("true");
        address.setEmail(NMRConstants.EMAIL_ID);
        address.setMobile(MOBILE_NUMBER);
        address.setAddressType(new AddressTypeTO(ADDRESS_TYPE_ID, ADDRESS_TYPE_NAME));
        address.setCreatedAt(CURRENT_DATE);
        address.setUpdatedAt(CURRENT_DATE);
        address.setFullName(PROFILE_DISPLAY_NAME);

        responseTO.setPersonalDetails(personalDetails);
        responseTO.setCommunicationAddress(address);
        responseTO.setKycAddress(address);
        responseTO.setHpProfileId(HP_ID);
        responseTO.setRequestId(REQUEST_ID);
        responseTO.setApplicationTypeId(ID);
        responseTO.setNmrId(NMR_ID);
        responseTO.setHpProfileStatusId(ID);
        responseTO.setWorkFlowStatusId(ID);
        responseTO.setEmailVerified(true);
        when(hpService.getHealthProfessionalPersonalDetail(any(BigInteger.class))).thenReturn(responseTO);
        mockMvc.perform(get("/health-professional/1/personal")
                        .with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personal_details.salutation").value(SALUTATION_DR))
                .andExpect(jsonPath("$.personal_details.aadhaar_token").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.personal_details.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.personal_details.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.personal_details.father_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.mother_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personal_details.spouse_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.personal_details.country_nationality.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.personal_details.country_nationality.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.personal_details.gender").value(GENDER))
                .andExpect(jsonPath("$.personal_details.schedule.id").value(CommonTestData.SCHEDULE_ID))
                .andExpect(jsonPath("$.personal_details.schedule.name").value(SCHEDULE_NAME))
                .andExpect(jsonPath("$.personal_details.profile_photo").value(PROFILE_PHOTO))
                .andExpect(jsonPath("$.personal_details.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.personal_details.email").value(CommonTestData.EMAIL_ID))
                .andExpect(jsonPath("$.personal_details.mobile").value(MOBILE_NUMBER))

                .andExpect(jsonPath("$.communication_address.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.communication_address.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.communication_address.country.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.communication_address.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.communication_address.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.communication_address.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.communication_address.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.communication_address.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.communication_address.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.communication_address.village.name").value(VILLAGE_NAME))
                .andExpect(jsonPath("$.communication_address.sub_district.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.communication_address.sub_district.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.communication_address.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.communication_address.address_type.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.communication_address.address_type.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.communication_address.created_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communication_address.updated_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communication_address.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.communication_address.house").value(HOUSE))
                .andExpect(jsonPath("$.communication_address.street").value(STREET))
                .andExpect(jsonPath("$.communication_address.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.communication_address.locality").value(LOCALITY))
                .andExpect(jsonPath("$.communication_address.is_same_address").value("true"))

                .andExpect(jsonPath("$.kyc_address.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.kyc_address.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.kyc_address.country.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.kyc_address.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.kyc_address.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.kyc_address.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.kyc_address.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.kyc_address.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.kyc_address.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.kyc_address.village.name").value(VILLAGE_NAME))
                .andExpect(jsonPath("$.kyc_address.sub_district.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.kyc_address.sub_district.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.kyc_address.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.kyc_address.address_type.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.kyc_address.address_type.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.kyc_address.created_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.kyc_address.updated_at").value(CURRENT_DATE))
                .andExpect(jsonPath("$.kyc_address.full_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.kyc_address.house").value(HOUSE))
                .andExpect(jsonPath("$.kyc_address.street").value(STREET))
                .andExpect(jsonPath("$.kyc_address.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.kyc_address.locality").value(LOCALITY))
                .andExpect(jsonPath("$.kyc_address.is_same_address").value("true"))

                .andExpect(jsonPath("$.hp_profile_id").value(HP_ID))
                .andExpect(jsonPath("$.request_id").value(CommonTestData.REQUEST_ID))
                .andExpect(jsonPath("$.application_type_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.hp_profile_status_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.work_flow_status_id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.email_verified").value(true));

    }

    @Test
    void testGetHealthProfessionalRegistrationDetailShouldGetHealthProfessionalRegistrationDetailsBaseOnHealthProfessionalId() throws Exception {
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = new HpProfileRegistrationResponseTO();
        hpProfileRegistrationResponseTO.setRequestId(REQUEST_ID);
        when(hpService.getHealthProfessionalRegistrationDetail(any(BigInteger.class))).thenReturn(hpProfileRegistrationResponseTO);
        mockMvc.perform(get("/health-professional/1/registration")
                        .with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.request_id").value(CommonTestData.REQUEST_ID));
    }

    @Test
    void testGetHealthProfessionalWorkDetailShouldGetHealthProfessionalWorkDetailsBaseOnHealthProfessionalId() throws Exception {
        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = new HpProfileWorkDetailsResponseTO();
        hpProfileWorkDetailsResponseTO.setRequestId(REQUEST_ID);

        when(hpService.getHealthProfessionalWorkDetail(any(BigInteger.class))).thenReturn(hpProfileWorkDetailsResponseTO);
        mockMvc.perform(get("/health-professional/1/work-profile")
                        .with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.request_id").value(CommonTestData.REQUEST_ID));
    }



}