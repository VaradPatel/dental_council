package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.service.ICouncilService;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IQueriesService;
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

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = HpRegistrationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = HpRegistrationController.class)
@ActiveProfiles(profiles = "local")
class HpRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IHpRegistrationService hpService;

    @MockBean
    private IQueriesService queryService;

    @MockBean
    private ICouncilService councilService;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    @WithMockUser
    void testFetchSmcRegistrationDetail() throws Exception {
        SmcRegistrationDetailResponseTO detailResponseTO = new SmcRegistrationDetailResponseTO();
        detailResponseTO.setHpName(HP_NAME);
        detailResponseTO.setRegistrationNumber(REGISTRATION_NUMBER);
        detailResponseTO.setCouncilName(STATE_MEDICAL_COUNCIL);
        detailResponseTO.setHpProfileId(HP_ID);
        detailResponseTO.setEmailId(EMAIL_ID);
        when(hpService.fetchSmcRegistrationDetail(any(Integer.class), any(String.class)))
                .thenReturn(detailResponseTO);
        mockMvc.perform(get("/health-professional").queryParam("smcId","13").queryParam("registrationNumber","123")
                        .with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hp_name").value(HP_NAME))
                .andExpect(jsonPath("$.registration_number").value(REGISTRATION_NUMBER))
                .andExpect(jsonPath("$.council_name").value(STATE_MEDICAL_COUNCIL))
                .andExpect(jsonPath("$.hp_profile_id").value(HP_ID))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID));

    }




    /*@Test
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
        personalDetails.setSchedule(ScheduleTO.builder().id(SCHEDULE_ID).name(SCHEDULE_NAME).build());
        personalDetails.setProfilePhoto(PROFILE_PHOTO);
        personalDetails.setFullName(APPLICANT_FULL_NAME_IN_LOWER_CASE);
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
        communicationAddress.setEmail(NMRConstants.EMAIL_ID);
        communicationAddress.setMobile(MOBILE_NUMBER);
        communicationAddress.setAddressType(new AddressTypeTO());
        communicationAddress.setCreatedAt("");
        communicationAddress.setUpdatedAt("");
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
        address.setCreatedAt("");
        address.setUpdatedAt("");
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
        when(hpService.addOrUpdateHpPersonalDetail(nullable(BigInteger.class), any(HpPersonalUpdateRequestTO.class))).thenReturn(responseTO);
        mockMvc.perform(post("/health-professional/personal").with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(requestTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalDetails.salutation").value(SALUTATION_DR))
                .andExpect(jsonPath("$.personalDetails.aadhaarToken").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.personalDetails.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.personalDetails.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personalDetails.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.personalDetails.fatherName").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personalDetails.motherName").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personalDetails.spouseName").value(FIRST_NAME))
                .andExpect(jsonPath("$.personalDetails.countryNationality.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.personalDetails.countryNationality.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.personalDetails.dateOfBirth").value(DATE_OF_BIRTH))
                .andExpect(jsonPath("$.personalDetails.gender").value(GENDER))
                .andExpect(jsonPath("$.personalDetails.schedule.id").value(CommonTestData.SCHEDULE_ID))
                .andExpect(jsonPath("$.personalDetails.schedule.name").value(SCHEDULE_NAME))
                .andExpect(jsonPath("$.personalDetails.fullName").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.personalDetails.isNew").value(true))
                .andExpect(jsonPath("$.personalDetails.email").value(CommonTestData.EMAIL_ID))
                .andExpect(jsonPath("$.personalDetails.email").value("Pending"))

                .andExpect(jsonPath("$.communicationAddress.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.communicationAddress.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.communicationAddress.country.name").value(COURSE_NAME))
                .andExpect(jsonPath("$.communicationAddress.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.communicationAddress.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.communicationAddress.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.communicationAddress.state.isoCode").value(STATE_CODE))
                .andExpect(jsonPath("$.communicationAddress.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.communicationAddress.district.isoCode").value(DISTRICT_CODE))
                .andExpect(jsonPath("$.communicationAddress.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.communicationAddress.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.communicationAddress.village.name").value(VILLAGE_ID))
                .andExpect(jsonPath("$.communicationAddress.subDistrict.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.communicationAddress.subDistrict.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.communicationAddress.subDistrict.isoCode").value(SUB_DISTRICT_CODE))
                .andExpect(jsonPath("$.communicationAddress.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.communicationAddress.addressLine1").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.communicationAddress.email").value(ADDRESS_LINE_2))
                .andExpect(jsonPath("$.communicationAddress.addressType.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.communicationAddress.addressType.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.communicationAddress.createdAt").value(""))
                .andExpect(jsonPath("$.communicationAddress.updatedAt").value(""))
                .andExpect(jsonPath("$.communicationAddress.fullName").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.communicationAddress.house").value(HOUSE))
                .andExpect(jsonPath("$.communicationAddress.street").value(STREET))
                .andExpect(jsonPath("$.communicationAddress.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.communicationAddress.locality").value(LOCALITY))
                .andExpect(jsonPath("$.communicationAddress.isSameAddress").value(true))

                .andExpect(jsonPath("$.kycAddress.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.kycAddress.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.kycAddress.country.name").value(COURSE_NAME))
                .andExpect(jsonPath("$.kycAddress.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.kycAddress.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.kycAddress.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.kycAddress.state.isoCode").value(STATE_CODE))
                .andExpect(jsonPath("$.kycAddress.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.kycAddress.district.isoCode").value(DISTRICT_CODE))
                .andExpect(jsonPath("$.kycAddress.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.kycAddress.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.kycAddress.village.name").value(VILLAGE_ID))
                .andExpect(jsonPath("$.kycAddress.subDistrict.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.kycAddress.subDistrict.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.kycAddress.subDistrict.isoCode").value(SUB_DISTRICT_CODE))
                .andExpect(jsonPath("$.kycAddress.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.kycAddress.addressLine1").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.kycAddress.email").value(ADDRESS_LINE_2))
                .andExpect(jsonPath("$.kycAddress.addressType.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.kycAddress.addressType.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.kycAddress.createdAt").value(""))
                .andExpect(jsonPath("$.kycAddress.updatedAt").value(""))
                .andExpect(jsonPath("$.kycAddress.fullName").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.kycAddress.house").value(HOUSE))
                .andExpect(jsonPath("$.kycAddress.street").value(STREET))
                .andExpect(jsonPath("$.kycAddress.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.kycAddress.locality").value(LOCALITY))
                .andExpect(jsonPath("$.kycAddress.isSameAddress").value(true))

                .andExpect(jsonPath("$.hpProfileId").value(HP_ID))
                .andExpect(jsonPath("$.requestId").value(CommonTestData.REQUEST_ID))
                .andExpect(jsonPath("$.applicationTypeId").value(CommonTestData.ID))
                .andExpect(jsonPath("$.hpProfileStatusId").value(CommonTestData.ID))
                .andExpect(jsonPath("$.workFlowStatusId").value(CommonTestData.ID))
                .andExpect(jsonPath("$.isEmailVerified").value(true));
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
        personalDetails.setSchedule(ScheduleTO.builder().id(SCHEDULE_ID).name(SCHEDULE_NAME).build());
        personalDetails.setProfilePhoto(PROFILE_PHOTO);
        personalDetails.setFullName(APPLICANT_FULL_NAME_IN_LOWER_CASE);
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
        communicationAddress.setEmail(NMRConstants.EMAIL_ID);
        communicationAddress.setMobile(MOBILE_NUMBER);
        communicationAddress.setAddressType(new AddressTypeTO());
        communicationAddress.setCreatedAt("");
        communicationAddress.setUpdatedAt("");
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
                .andExpect(jsonPath("$.personalDetails.salutation").value(SALUTATION_DR))
                .andExpect(jsonPath("$.personalDetails.aadhaarToken").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.personalDetails.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.personalDetails.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personalDetails.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.personalDetails.fatherName").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personalDetails.motherName").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.personalDetails.spouseName").value(FIRST_NAME))
                .andExpect(jsonPath("$.personalDetails.countryNationality.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.personalDetails.countryNationality.name").value(COUNTRY_NAME))
                .andExpect(jsonPath("$.personalDetails.dateOfBirth").value(DATE_OF_BIRTH))
                .andExpect(jsonPath("$.personalDetails.gender").value(GENDER))
                .andExpect(jsonPath("$.personalDetails.schedule.id").value(CommonTestData.SCHEDULE_ID))
                .andExpect(jsonPath("$.personalDetails.schedule.name").value(SCHEDULE_NAME))
                .andExpect(jsonPath("$.personalDetails.fullName").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.personalDetails.isNew").value(true))
                .andExpect(jsonPath("$.personalDetails.email").value(CommonTestData.EMAIL_ID))

                .andExpect(jsonPath("$.communicationAddress.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.communicationAddress.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.communicationAddress.country.name").value(COURSE_NAME))
                .andExpect(jsonPath("$.communicationAddress.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.communicationAddress.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.communicationAddress.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.communicationAddress.state.isoCode").value(STATE_CODE))
                .andExpect(jsonPath("$.communicationAddress.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.communicationAddress.district.isoCode").value(DISTRICT_CODE))
                .andExpect(jsonPath("$.communicationAddress.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.communicationAddress.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.communicationAddress.village.name").value(VILLAGE_ID))
                .andExpect(jsonPath("$.communicationAddress.subDistrict.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.communicationAddress.subDistrict.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.communicationAddress.subDistrict.isoCode").value(SUB_DISTRICT_CODE))
                .andExpect(jsonPath("$.communicationAddress.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.communicationAddress.addressLine1").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.communicationAddress.email").value(ADDRESS_LINE_2))
                .andExpect(jsonPath("$.communicationAddress.addressType.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.communicationAddress.addressType.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.communicationAddress.createdAt").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communicationAddress.updatedAt").value(CURRENT_DATE))
                .andExpect(jsonPath("$.communicationAddress.fullName").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.communicationAddress.house").value(HOUSE))
                .andExpect(jsonPath("$.communicationAddress.street").value(STREET))
                .andExpect(jsonPath("$.communicationAddress.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.communicationAddress.locality").value(LOCALITY))
                .andExpect(jsonPath("$.communicationAddress.isSameAddress").value(true))

                .andExpect(jsonPath("$.kycAddress.id").value(CommonTestData.ID))
                .andExpect(jsonPath("$.kycAddress.country.id").value(COUNTRY_ID))
                .andExpect(jsonPath("$.kycAddress.country.name").value(COURSE_NAME))
                .andExpect(jsonPath("$.kycAddress.country.nationality").value(LOCALITY))
                .andExpect(jsonPath("$.kycAddress.state.id").value(STATE_ID))
                .andExpect(jsonPath("$.kycAddress.state.name").value(STATE_NAME))
                .andExpect(jsonPath("$.kycAddress.state.isoCode").value(STATE_CODE))
                .andExpect(jsonPath("$.kycAddress.district.id").value(DISTRICT_ID))
                .andExpect(jsonPath("$.kycAddress.district.isoCode").value(DISTRICT_CODE))
                .andExpect(jsonPath("$.kycAddress.district.name").value(DISTRICT_NAME))
                .andExpect(jsonPath("$.kycAddress.village.id").value(VILLAGE_ID))
                .andExpect(jsonPath("$.kycAddress.village.name").value(VILLAGE_ID))
                .andExpect(jsonPath("$.kycAddress.subDistrict.id").value(SUB_DISTRICT_ID))
                .andExpect(jsonPath("$.kycAddress.subDistrict.name").value(SUB_DISTRICT_NAME))
                .andExpect(jsonPath("$.kycAddress.subDistrict.isoCode").value(SUB_DISTRICT_CODE))
                .andExpect(jsonPath("$.kycAddress.pincode").value(CommonTestData.PIN_CODE))
                .andExpect(jsonPath("$.kycAddress.addressLine1").value(ADDRESS_LINE_1))
                .andExpect(jsonPath("$.kycAddress.email").value(ADDRESS_LINE_2))
                .andExpect(jsonPath("$.kycAddress.addressType.id").value(ADDRESS_TYPE_ID))
                .andExpect(jsonPath("$.kycAddress.addressType.name").value(ADDRESS_TYPE_NAME))
                .andExpect(jsonPath("$.kycAddress.createdAt").value(""))
                .andExpect(jsonPath("$.kycAddress.updatedAt").value(""))
                .andExpect(jsonPath("$.kycAddress.fullName").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.kycAddress.house").value(HOUSE))
                .andExpect(jsonPath("$.kycAddress.street").value(STREET))
                .andExpect(jsonPath("$.kycAddress.landmark").value(LANDMARK))
                .andExpect(jsonPath("$.kycAddress.locality").value(LOCALITY))
                .andExpect(jsonPath("$.kycAddress.isSameAddress").value(true))

                .andExpect(jsonPath("$.hpProfileId").value(HP_ID))
                .andExpect(jsonPath("$.requestId").value(CommonTestData.REQUEST_ID))
                .andExpect(jsonPath("$.applicationTypeId").value(CommonTestData.ID))
                .andExpect(jsonPath("$.hpProfileStatusId").value(CommonTestData.ID))
                .andExpect(jsonPath("$.workFlowStatusId").value(CommonTestData.ID))
                .andExpect(jsonPath("$.isEmailVerified").value(true));
    }*/
}