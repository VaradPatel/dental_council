package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.Predicate;

import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.dto.HpProfileAddResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.dto.AddressTO;
import in.gov.abdm.nmr.entity.AddressType;
import in.gov.abdm.nmr.dto.AddressTypeTO;
import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.entity.Country;
import in.gov.abdm.nmr.repository.CountryRepository;
import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.entity.District;
import in.gov.abdm.nmr.repository.DistrictRepository;
import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.dto.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.dto.HpProfileDetailTO;
import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import in.gov.abdm.nmr.mapper.IHpProfileMapper;
import in.gov.abdm.nmr.dto.IMRDetailsTO;
import in.gov.abdm.nmr.dto.PersonalDetailsTO;
import in.gov.abdm.nmr.dto.SpecialityDetailsTO;
import in.gov.abdm.nmr.dto.WorkDetailsTO;
import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.entity.LanguagesKnown;
import in.gov.abdm.nmr.dto.NationalityTO;
import in.gov.abdm.nmr.dto.OrganizationTypeTO;
import in.gov.abdm.nmr.dto.QualificationDetailRequestTO;
import in.gov.abdm.nmr.dto.QualificationDetailTO;
import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.dto.RegistrationDetailTO;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.entity.Schedule;
import in.gov.abdm.nmr.dto.ScheduleTO;
import in.gov.abdm.nmr.entity.State;
import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.StateMedicalCouncilStatus;
import in.gov.abdm.nmr.entity.SubDistrict;
import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.entity.SuperSpeciality;
import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import in.gov.abdm.nmr.dto.UniversityTO;
import in.gov.abdm.nmr.entity.Villages;
import in.gov.abdm.nmr.dto.VillagesTO;
import in.gov.abdm.nmr.entity.WorkNature;
import in.gov.abdm.nmr.dto.WorkNatureTO;
import in.gov.abdm.nmr.entity.WorkProfile;
import in.gov.abdm.nmr.entity.WorkStatus;
import in.gov.abdm.nmr.dto.WorkStatusTO;

@Service
public class HpProfileDaoServiceImpl implements IHpProfileDaoService {

	private IHpProfileMapper ihHpProfileMapper;

	private IHpProfileRepository iHpProfileRepository;

	private IAddressRepository iAddressRepository;

	private RegistrationDetailRepository registrationDetailRepository;

	private QualificationDetailRepository qualificationDetailRepository;

	private WorkProfileRepository workProfileRepository;

	private SuperSpecialityRepository superSpecialityRepository;

	private DistrictRepository districtRepository;

	private IStateRepository stateRepository;

	private IAddressTypeRepository iAddressTypeRepository;

	private OrganizationTypeRepository organizationTypeRepository;

	private IStateMedicalCouncilStatusRepository iStateMedicalCouncilStatusRepository;

	private INationalityRepository iNationalityRepository;

	private IScheduleRepository iScheduleRepository;

	private CountryRepository countryRepository;

	private LanguageRepository languageRepository;

	private LanguagesKnownRepository languagesKnownRepository;

	private IStateMedicalCouncilRepository iStateMedicalCouncilRepository;

	private WorkNatureRepository workNatureRepository;

	private WorkStatusRepository workStatusRepository;

	private SubDistrictRepository subDistrictRepository;

	private VillagesRepository villagesRepository;

	public HpProfileDaoServiceImpl(IHpProfileMapper ihHpProfileMapper, IHpProfileRepository iHpProfileRepository,
								   IAddressRepository iAddressRepository, QualificationDetailRepository qualificationDetailRepository,
								   RegistrationDetailRepository registrationDetailRepository, WorkProfileRepository workProfileRepository,
								   SuperSpecialityRepository superSpecialityRepository, DistrictRepository districtRepository,

								   IStateRepository stateRepository, INationalityRepository iNationalityRepository,
								   IStateMedicalCouncilStatusRepository iStateMedicalCouncilStatusRepository,
								   IScheduleRepository iScheduleRepository, CountryRepository countryRepository,
								   LanguageRepository languageRepository, LanguagesKnownRepository languagesKnownRepository,
								   IStateMedicalCouncilRepository iStateMedicalCouncilRepository, WorkNatureRepository workNatureRepository,
								   WorkStatusRepository workStatusRepository, SubDistrictRepository subDistrictRepository,
								   VillagesRepository villagesRepository) {

		super();
		this.ihHpProfileMapper = ihHpProfileMapper;
		this.iHpProfileRepository = iHpProfileRepository;
		this.iAddressRepository = iAddressRepository;
		this.qualificationDetailRepository = qualificationDetailRepository;
		this.registrationDetailRepository = registrationDetailRepository;
		this.workProfileRepository = workProfileRepository;
		this.superSpecialityRepository = superSpecialityRepository;
		this.districtRepository = districtRepository;
		this.stateRepository = stateRepository;
		this.iNationalityRepository = iNationalityRepository;
		this.iStateMedicalCouncilStatusRepository = iStateMedicalCouncilStatusRepository;
		this.iScheduleRepository = iScheduleRepository;
		this.countryRepository = countryRepository;
		this.languageRepository = languageRepository;
		this.languagesKnownRepository = languagesKnownRepository;
		this.iStateMedicalCouncilRepository = iStateMedicalCouncilRepository;
		this.workNatureRepository = workNatureRepository;
		this.workStatusRepository = workStatusRepository;
		this.subDistrictRepository = subDistrictRepository;
		this.villagesRepository = villagesRepository;
	}

	@PersistenceContext
	private EntityManager entityManager;

	public HpSmcDetailTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<HpProfile> criteria = builder.createQuery(HpProfile.class);
//		Root<HpProfile> root = criteria.from(HpProfile.class);
//		Join<Object, Object> registrationDetails = root.join(HpProfile_.REGISTRATION_DETAILS, JoinType.INNER);

		List<Predicate> predicates = new ArrayList<>();
		HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();
		if (StringUtils.isNotBlank(smcRegistrationDetailRequestTO.getRegistrationNumber())) {

			Tuple hpProfile = iHpProfileRepository.fetchSmcRegistrationDetail(
					smcRegistrationDetailRequestTO.getRegistrationNumber(),
					smcRegistrationDetailRequestTO.getCouncilId());

			hpSmcDetailTO.setHpName(hpProfile.get("full_name", String.class));
			hpSmcDetailTO.setCouncilName(hpProfile.get("name", String.class));
//        	hpSmcDetailTO.setRegistrationNumber(hpProfile.get("registration_no", String.class));
			hpSmcDetailTO.setHpProfileId(hpProfile.get("hp_profile_id", BigInteger.class));

		}
		return hpSmcDetailTO;
	}

	public HpProfileDetailTO fetchHpProfileDetail(BigInteger hpProfileId) {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<HpProfile> criteria = builder.createQuery(HpProfile.class);
//		Root<HpProfile> root = criteria.from(HpProfile.class);
//		Join<Object, Object> registrationDetails = root.join(HpProfile_.REGISTRATION_DETAILS, JoinType.INNER);
//
//		List<Predicate> predicates = new ArrayList<>();
		HpProfileDetailTO hpSmcDetailTO = new HpProfileDetailTO();
		RegistrationDetailTO registrationDetailTO = new RegistrationDetailTO();

		//////////// Registration Details Start/////////////////
		Tuple hpProfile = iHpProfileRepository.fetchHpProfileDetail(hpProfileId);

		if (hpProfile == null) {
			return new HpProfileDetailTO();
		}

		StateMedicalCouncil stateMedicalCouncil = iStateMedicalCouncilRepository
				.findById(hpProfile.get("state_medical_council_id", BigInteger.class)).orElse(null);

		registrationDetailTO.setStateMedicalCouncil(stateMedicalCouncil);

//		StateMedicalCouncilStatus stateMedicalCouncilStatus = iStateMedicalCouncilStatusRepository
//				.findById(hpProfile.get("state_medical_council_status_id", BigInteger.class)).orElse(null);
//		registrationDetailTO.setCouncilStatus(stateMedicalCouncilStatus);

		registrationDetailTO.setRegistrationNumber(hpProfile.get("registration_no", String.class));
		registrationDetailTO.setRegistrationDate(hpProfile.get("registration_date", Date.class));
		registrationDetailTO.setIsRenewable(hpProfile.get("is_renewable", String.class));
		registrationDetailTO.setRenewableRegistrationDate(hpProfile.get("renewable_registration_date", Date.class));
		registrationDetailTO.setIsNameChange(hpProfile.get("is_name_change", String.class));

		hpSmcDetailTO.setRegistrationDetail(registrationDetailTO);
		/////////// Registration Details end///////////////////

		//////////// Qualification Details start/////////////////
		List<Tuple> hpQualification = iHpProfileRepository
				.fetchQualificationDetail(hpProfile.get("registration_id", Integer.class));
		List<QualificationDetailTO> qualificationDetailTOList = new ArrayList<QualificationDetailTO>();
		for (Tuple appl : hpQualification) {

			QualificationDetailTO qualificationDetailTO = new QualificationDetailTO();

			CourseTO courseTO = new CourseTO();
			courseTO.setId(appl.get("course_id", BigInteger.class));
			courseTO.setName(appl.get("course_name", String.class));
			qualificationDetailTO.setCourse(courseTO);

			CountryTO countryTO = new CountryTO();
			countryTO.setName(appl.get("country_name", String.class));
			countryTO.setId(appl.get("country_id", BigInteger.class));
			qualificationDetailTO.setCountry(countryTO);

			StateTO stateTO = new StateTO();
			stateTO.setId(appl.get("state_id", BigInteger.class));
			stateTO.setName(appl.get("state_name", String.class));
			qualificationDetailTO.setState(stateTO);

			CollegeTO collegeTO = new CollegeTO();
			collegeTO.setId(appl.get("college_id", BigInteger.class));
			collegeTO.setName(appl.get("college_name", String.class));
			qualificationDetailTO.setCollege(collegeTO);

			UniversityTO universityTO = new UniversityTO();
			universityTO.setId(appl.get("university_id", BigInteger.class));
			universityTO.setName(appl.get("university_name", String.class));
			qualificationDetailTO.setUniversity(universityTO);

			qualificationDetailTO.setIsVerified(appl.get("is_verified", Integer.class));

			qualificationDetailTO.setQualificationMonth(appl.get("qualification_month", String.class));
			qualificationDetailTO.setQualificationYear(appl.get("qualification_year", String.class));
			qualificationDetailTO.setIsNameChange(appl.get("is_name_change", Integer.class));
			qualificationDetailTO.setId(appl.get("qualification_details_id", Integer.class));

			qualificationDetailTOList.add(qualificationDetailTO);
		}

		hpSmcDetailTO.setQualificationDetail(qualificationDetailTOList);

		//////////// Qualification Details end/////////////////

		//////////// Personal Details Start/////////////////////
		PersonalDetailsTO personaldetailsTO = new PersonalDetailsTO();
		personaldetailsTO.setSalutation(hpProfile.get("salutation", String.class));
		personaldetailsTO.setAadhaarToken(hpProfile.get("aadhaar_token", String.class));
		personaldetailsTO.setFirstName(hpProfile.get("first_name", String.class));
		personaldetailsTO.setMiddleName(hpProfile.get("middle_name", String.class));
		personaldetailsTO.setLastName(hpProfile.get("last_name", String.class));
		personaldetailsTO.setFatherName(hpProfile.get("father_name", String.class));
		personaldetailsTO.setMotherName(hpProfile.get("mother_name", String.class));
		personaldetailsTO.setSpouseName(hpProfile.get("spouse_name", String.class));

		NationalityTO nationalityTO = new NationalityTO();
		nationalityTO.setId(hpProfile.get("nationality_id", BigInteger.class));
		nationalityTO.setName(hpProfile.get("nationality_name", String.class));

		personaldetailsTO.setCountryNationality(nationalityTO);

		List<Tuple> languageList = iHpProfileRepository.fetchLanguageDetails(hpProfileId);

		List<LanguageTO> languageTOs = new ArrayList<LanguageTO>();

		for (Tuple language : languageList) {
			LanguageTO languageTO = new LanguageTO();
			languageTO.setName(language.get("language", String.class));
			
			languageTO.setId(language.get("language_id", BigInteger.class));
			languageTOs.add(languageTO);
		}
		personaldetailsTO.setLanguage(languageTOs);

		personaldetailsTO.setDateOfBirth(hpProfile.get("date_of_birth", Date.class));
		personaldetailsTO.setGender(hpProfile.get("gender", String.class));

		ScheduleTO scheduleTO = new ScheduleTO();
		scheduleTO.setId(hpProfile.get("schedule_id", BigInteger.class));
		scheduleTO.setName(hpProfile.get("schedule_name", String.class));

		personaldetailsTO.setSchedule(scheduleTO);

		hpSmcDetailTO.setPersonalDetails(personaldetailsTO);
		//////////// Personal Details Start/////////////////////

		//////////// Communication LanguagesKnown Details Start/////////////////////
		Tuple communicationAddress = iHpProfileRepository.fetchCommunicationAddress(hpProfileId, 4);
		if (communicationAddress != null) {
			AddressTO commAddressTO = new AddressTO();

			commAddressTO.setAddressLine1(communicationAddress.get("address_line1", String.class));

			AddressTypeTO addressTypeTO = new AddressTypeTO();
			addressTypeTO.setId(communicationAddress.get("address_type_id", Integer.class));
			addressTypeTO.setName(communicationAddress.get("address_type_name", String.class));
			commAddressTO.setAddressType(addressTypeTO);

			CountryTO countryTO = new CountryTO();
			countryTO.setName(communicationAddress.get("country_name", String.class));
			countryTO.setId(communicationAddress.get("country_id", BigInteger.class));
			commAddressTO.setCountry(countryTO);

			StateTO stateTO = new StateTO();
			stateTO.setId(communicationAddress.get("state_id", BigInteger.class));
			stateTO.setName(communicationAddress.get("state_name", String.class));
			commAddressTO.setState(stateTO);

			DistrictTO districtTO = new DistrictTO();
			districtTO.setId(communicationAddress.get("district_id", BigInteger.class));
			districtTO.setName(communicationAddress.get("district_name", String.class));
			commAddressTO.setDistrict(districtTO);

			SubDistrictTO subDistrictTO = new SubDistrictTO();
			subDistrictTO.setId(communicationAddress.get("sub_district_id", BigInteger.class));
			subDistrictTO.setName(communicationAddress.get("sub_district_name", String.class));
			commAddressTO.setSubDistrict(subDistrictTO);

			VillagesTO villagesTO = new VillagesTO();
			villagesTO.setId(communicationAddress.get("villages_id", BigInteger.class));
			villagesTO.setName(communicationAddress.get("villages_name", String.class));
			commAddressTO.setVillage(villagesTO);

			commAddressTO.setPincode(communicationAddress.get("pincode", String.class));
//			commAddressTO.setCreatedDate(communicationAddress.get("created_date", String.class));
//			commAddressTO.setUpdatedDate(communicationAddress.get("updated_date", String.class));
			commAddressTO.setEmail(communicationAddress.get("email", String.class));
			commAddressTO.setMobile(communicationAddress.get("mobile", String.class));
			commAddressTO.setFullName(hpProfile.get("full_name", String.class));
			hpSmcDetailTO.setCommunicationAddress(commAddressTO);
		}

		//////////// Communication LanguagesKnown Details end/////////////////////

		//////////// IMR Details start/////////////////////

		IMRDetailsTO imrDetailsTO = new IMRDetailsTO();

		imrDetailsTO.setRegistrationNumber(hpProfile.get("registration_no", String.class));
		imrDetailsTO.setNmrId(hpProfile.get("nmr_id", String.class));
		imrDetailsTO.setYearOfInfo(hpProfile.get("year_of_info", String.class));
		hpSmcDetailTO.setImrDetails(imrDetailsTO);
		//////////// IMR Details end/////////////////////

		//////////// Work profile Details start/////////////////////
		Tuple workProfileDetails = iHpProfileRepository.fetchWorkProfileDetails(hpProfileId);

		if (workProfileDetails != null) {

			BroadSpecialityTO broadSpecialityTO = new BroadSpecialityTO();
			broadSpecialityTO.setId(workProfileDetails.get("broad_speciality_id", BigInteger.class));
			broadSpecialityTO.setName(workProfileDetails.get("broad_speciality_name", String.class));

			List<SuperSpecialityTO> superSpecialityList = new ArrayList<SuperSpecialityTO>();

			List<Tuple> superSpecialityDetails = iHpProfileRepository.fetchSuperSpecialityDetails(hpProfileId);
			for (Tuple superSpeciality : superSpecialityDetails) {
				SuperSpecialityTO superSpecialityTO = new SuperSpecialityTO();
				superSpecialityTO.setId(superSpeciality.get("speciality_id", BigInteger.class));
				superSpecialityTO.setName(superSpeciality.get("speciality_name", String.class));
				superSpecialityList.add(superSpecialityTO);
			}
			SpecialityDetailsTO specialityDetailsTO = new SpecialityDetailsTO();
			specialityDetailsTO.setBroadSpeciality(broadSpecialityTO);
			specialityDetailsTO.setSuperSpeciality(superSpecialityList);

			hpSmcDetailTO.setSpecialityDetails(specialityDetailsTO);

			WorkDetailsTO workDetailsTO = new WorkDetailsTO();

			WorkNatureTO workNatureTO = new WorkNatureTO();
			workNatureTO.setId(workProfileDetails.get("work_nature_id", BigInteger.class));
			workNatureTO.setName(workProfileDetails.get("work_nature_name", String.class));

			WorkStatusTO workStatusTO = new WorkStatusTO();
			workStatusTO.setId(workProfileDetails.get("work_status_id", BigInteger.class));
			workStatusTO.setName(workProfileDetails.get("work_status_name", String.class));

			workDetailsTO.setWorkNature(workNatureTO);
			workDetailsTO.setWorkStatus(workStatusTO);
			workDetailsTO.setIsUserCurrentlyWorking(workProfileDetails.get("is_user_currently_working", Integer.class));

			hpSmcDetailTO.setWorkDetails(workDetailsTO);

			CurrentWorkDetailsTO currentWorkDetailsTO = new CurrentWorkDetailsTO();

			currentWorkDetailsTO.setWorkOrganization(workProfileDetails.get("work_organization", String.class));

//			OrganizationTypeTO organizationTypeTO = new OrganizationTypeTO();
//			organizationTypeTO.setId(workProfileDetails.get("organization_type_id", BigInteger.class));
//			organizationTypeTO.setName(workProfileDetails.get("organization_type_name", String.class));
//			currentWorkDetailsTO.setOrganizationType(organizationTypeTO);

			currentWorkDetailsTO.setUrl(workProfileDetails.get("url", String.class));
			currentWorkDetailsTO.setFacility(workProfileDetails.get("facility", Integer.class));

			Tuple currentAddress = iHpProfileRepository.fetchCurrentAddress(hpProfileId, 2);
			AddressTO currentAddressTO = new AddressTO();

			currentAddressTO.setAddressLine1(currentAddress.get("address_line1", String.class));

			AddressTypeTO currentAddressTypeTO = new AddressTypeTO();
			currentAddressTypeTO.setId(currentAddress.get("address_type_id", Integer.class));
			currentAddressTypeTO.setName(currentAddress.get("address_type_name", String.class));
			currentAddressTO.setAddressType(currentAddressTypeTO);

			CountryTO currentCountryTO = new CountryTO();
			currentCountryTO.setName(currentAddress.get("country_name", String.class));
			currentCountryTO.setId(currentAddress.get("country_id", BigInteger.class));
			currentAddressTO.setCountry(currentCountryTO);

			StateTO currentStateTO = new StateTO();
			currentStateTO.setId(currentAddress.get("state_id", BigInteger.class));
			currentStateTO.setName(currentAddress.get("state_name", String.class));
			currentAddressTO.setState(currentStateTO);

			DistrictTO currentDistrictTO = new DistrictTO();
			currentDistrictTO.setId(currentAddress.get("district_id", BigInteger.class));
			currentDistrictTO.setName(currentAddress.get("district_name", String.class));
			currentAddressTO.setDistrict(currentDistrictTO);

			SubDistrictTO currentSubDistrictTO = new SubDistrictTO();
			currentSubDistrictTO.setId(currentAddress.get("sub_district_id", BigInteger.class));
			currentSubDistrictTO.setName(currentAddress.get("sub_district_name", String.class));
			currentAddressTO.setSubDistrict(currentSubDistrictTO);

			currentAddressTO.setPincode(currentAddress.get("pincode", String.class));
//			currentAddressTO.setCreatedDate(currentAddress.get("created_date", String.class));
//			currentAddressTO.setUpdatedDate(currentAddress.get("updated_date", String.class));
			currentAddressTO.setEmail(currentAddress.get("email", String.class));
			currentAddressTO.setMobile(currentAddress.get("mobile", String.class));
			currentAddressTO.setFullName(hpProfile.get("full_name", String.class));

			currentWorkDetailsTO.setAddress(currentAddressTO);
			hpSmcDetailTO.setCurrentWorkDetails(currentWorkDetailsTO);
		}
		//////////// Work profile Details end/////////////////////

		return hpSmcDetailTO;
	}

	public HpProfileUpdateResponseTO updateHpProfile(BigInteger hpProfileId,
			HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException {
		HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();

		if ((hpProfileUpdateRequest.getPersonalDetails().getSalutation() != null
				&& hpProfileUpdateRequest.getPersonalDetails().getSalutation() != "")
				&& (hpProfileUpdateRequest.getPersonalDetails().getAadhaarToken() != null
						&& hpProfileUpdateRequest.getPersonalDetails().getAadhaarToken() != "")
				&& (hpProfileUpdateRequest.getPersonalDetails().getFirstName() != null
						&& hpProfileUpdateRequest.getPersonalDetails().getFirstName() != "")
				&& (hpProfileUpdateRequest.getPersonalDetails().getCountryNationality() != null)
				&& (hpProfileUpdateRequest.getPersonalDetails().getCountryNationality() != null)
				&& (hpProfileUpdateRequest.getPersonalDetails().getDateOfBirth() != null)
				&& (hpProfileUpdateRequest.getPersonalDetails().getGender() != null
						&& hpProfileUpdateRequest.getPersonalDetails().getGender() != "")
				&& (hpProfileUpdateRequest.getPersonalDetails().getSchedule() != null)
				&& (hpProfileUpdateRequest.getCommunicationAddress().getFullName() != null
						&& hpProfileUpdateRequest.getCommunicationAddress().getFullName() != "")
				&& (hpProfileUpdateRequest.getCommunicationAddress().getAddressLine1() != null
						&& hpProfileUpdateRequest.getCommunicationAddress().getAddressLine1() != "")
				&& (hpProfileUpdateRequest.getCommunicationAddress().getVillage().getId() != null)
				&& (hpProfileUpdateRequest.getCommunicationAddress().getDistrict().getId() != null)
				&& (hpProfileUpdateRequest.getCommunicationAddress().getCountry().getId() != null)
				&& (hpProfileUpdateRequest.getCommunicationAddress().getState().getId() != null)
				&& (hpProfileUpdateRequest.getCommunicationAddress().getPincode() != null)
				&& (hpProfileUpdateRequest.getCommunicationAddress().getEmail() != null)
				&& (hpProfileUpdateRequest.getCommunicationAddress().getMobile() != null)

				&& (hpProfileUpdateRequest.getImrDetails().getNmrId() != null
						&& hpProfileUpdateRequest.getImrDetails().getNmrId() != "")
				&& (hpProfileUpdateRequest.getImrDetails().getRegistrationNumber() != null
						&& hpProfileUpdateRequest.getImrDetails().getRegistrationNumber() != "")
				&& (hpProfileUpdateRequest.getImrDetails().getYearOfInfo() != null
						&& hpProfileUpdateRequest.getImrDetails().getYearOfInfo() != "")

				&& (hpProfileUpdateRequest.getRegistrationDetail().getStateMedicalCouncil() != null)
				&& (hpProfileUpdateRequest.getRegistrationDetail().getRegistrationNumber() != null
						&& hpProfileUpdateRequest.getRegistrationDetail().getRegistrationNumber() != "")
				&& (hpProfileUpdateRequest.getRegistrationDetail().getRegistrationDate() != null)
				&& (hpProfileUpdateRequest.getRegistrationDetail().getIsRenewable() != null)
				&& (hpProfileUpdateRequest.getRegistrationDetail().getIsNameChange() != null)

				&& (hpProfileUpdateRequest.getSpecialityDetails().getBroadSpeciality() != null)

				&& (hpProfileUpdateRequest.getWorkDetails().getIsUserCurrentlyWorking() != null)
				&& (hpProfileUpdateRequest.getWorkDetails().getWorkNature().getId() != null)
				&& (hpProfileUpdateRequest.getWorkDetails().getWorkStatus().getId() != null)
				&& (hpProfileUpdateRequest.getCurrentWorkDetails().getUrl() != null
						&& hpProfileUpdateRequest.getCurrentWorkDetails().getUrl() != "")) {

			if (hpProfileUpdateRequest.getPersonalDetails().getLanguage().size() > 0) {

				String hpId = hpProfileId.toString();
				if (iHpProfileRepository.existsById(hpProfileId) == true) {
					HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);

					hpProfile.setSalutation(hpProfileUpdateRequest.getPersonalDetails().getSalutation());
					hpProfile.setAadhaarToken(hpProfileUpdateRequest.getPersonalDetails().getAadhaarToken());
					hpProfile.setFirstName(hpProfileUpdateRequest.getPersonalDetails().getFirstName());
					hpProfile.setMiddleName(hpProfileUpdateRequest.getPersonalDetails().getMiddleName());
					hpProfile.setLastName(hpProfileUpdateRequest.getPersonalDetails().getLastName());
					hpProfile.setFatherName(hpProfileUpdateRequest.getPersonalDetails().getFatherName());
					hpProfile.setMotherName(hpProfileUpdateRequest.getPersonalDetails().getMotherName());
					hpProfile.setSpouseName(hpProfileUpdateRequest.getPersonalDetails().getSpouseName());
					hpProfile.setGender(hpProfileUpdateRequest.getPersonalDetails().getGender());
					Schedule schedule = iScheduleRepository
							.findById(hpProfileUpdateRequest.getPersonalDetails().getSchedule().getId()).orElse(null);

					hpProfile.setSchedule(schedule);

//					Nationality nationality = iNationalityRepository
//							.findById(hpProfileUpdateRequest.getPersonalDetails().getCountryNationality().getId())
//							.orElse(null);

					Country countryNationality = countryRepository
							.findById(hpProfileUpdateRequest.getPersonalDetails().getCountryNationality().getId())
							.orElse(null);
//					Country setNationalityData = new Country();
//					setNationalityData.setId(countryNationality.getId());
//					setNationalityData.setName(countryNationality.getNationality());

					if (countryNationality != null) {
						hpProfile.setCountryNationality(countryNationality);
					}

//					hpProfile.setNationality(hpProfileUpdateRequest.getPersonalDetails().getNationality());

					hpProfile.setDateOfBirth(hpProfileUpdateRequest.getPersonalDetails().getDateOfBirth());
					hpProfile.setFullName(hpProfileUpdateRequest.getCommunicationAddress().getFullName());
					hpProfile.setNmrId(hpProfileUpdateRequest.getImrDetails().getNmrId());
					hpProfile.setYearOfInfo(hpProfileUpdateRequest.getImrDetails().getYearOfInfo());
					hpProfile.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
					hpProfile.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
					iHpProfileRepository.save(hpProfile);

					List<LanguagesKnown> languagesKnowns = languagesKnownRepository
							.getLanguagesKnownByHpProfileId(hpProfileId);
					languagesKnownRepository.deleteAll(languagesKnowns);

					List<LanguageTO> languages = hpProfileUpdateRequest.getPersonalDetails().getLanguage();
					if (languages.size() > 0) {
						List<LanguagesKnown> newLanguagesKnowns = new ArrayList<LanguagesKnown>();
						for (LanguageTO languageTO : languages) {
							LanguagesKnown languageData = new LanguagesKnown();
							languageData.setHpProfile(hpProfile);
							languageData.setLanguageId(languageTO.getId());
							languageData.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
							languageData.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
							newLanguagesKnowns.add(languageData);
						}
						languagesKnownRepository.saveAll(newLanguagesKnowns);
					}

					if (hpProfileUpdateRequest.getCommunicationAddress() != null) {
						Address addressData = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, 4);
						if (addressData != null) {

							addressData.setAddressLine1(
									hpProfileUpdateRequest.getCommunicationAddress().getAddressLine1());
							addressData.setPincode(hpProfileUpdateRequest.getCommunicationAddress().getPincode());
							addressData.setEmail(hpProfileUpdateRequest.getCommunicationAddress().getEmail());
							addressData.setMobile(hpProfileUpdateRequest.getCommunicationAddress().getMobile());

							Country communicationCountry = new Country();
							communicationCountry
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getCountry().getId());
							addressData.setCountry(communicationCountry);

							State communicationState = new State();
							communicationState
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getState().getId());
							addressData.setState(communicationState);

							District communicationDistrict = new District();
							communicationDistrict
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getDistrict().getId());
							addressData.setDistrict(communicationDistrict);

							SubDistrict communicationSubDistrict = new SubDistrict();
							communicationSubDistrict
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getSubDistrict().getId());
							addressData.setSubDistrict(communicationSubDistrict);

							Villages communicationCity = new Villages();
							communicationCity
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getVillage().getId());
							addressData.setVillage(communicationCity);

							AddressType addressType = new AddressType();
							addressType
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getAddressType().getId());
							addressData.setAddressTypeId(addressType);

							addressData.setHpProfileId(hpProfileId);

							iAddressRepository.save(addressData);

						} else {

							Address addAddressData = new Address();
							addAddressData.setAddressLine1(
									hpProfileUpdateRequest.getCommunicationAddress().getAddressLine1());
							addAddressData.setPincode(hpProfileUpdateRequest.getCommunicationAddress().getPincode());
							addAddressData.setEmail(hpProfileUpdateRequest.getCommunicationAddress().getEmail());
							addAddressData.setMobile(hpProfileUpdateRequest.getCommunicationAddress().getMobile());

							Country communicationCountry = new Country();
							communicationCountry
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getCountry().getId());
							addAddressData.setCountry(communicationCountry);

							State communicationState = new State();
							communicationState
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getState().getId());
							addAddressData.setState(communicationState);

							District communicationDistrict = new District();
							communicationDistrict
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getDistrict().getId());
							addAddressData.setDistrict(communicationDistrict);

							SubDistrict communicationSubDistrict = new SubDistrict();
							communicationSubDistrict
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getSubDistrict().getId());
							addAddressData.setSubDistrict(communicationSubDistrict);

							Villages communicationCity = new Villages();
							communicationCity
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getVillage().getId());
							addAddressData.setVillage(communicationCity);

							AddressType addressType = new AddressType();
							addressType
									.setId(hpProfileUpdateRequest.getCommunicationAddress().getAddressType().getId());
							addAddressData.setAddressTypeId(addressType);

							addAddressData.setHpProfileId(hpProfileId);

							iAddressRepository.save(addAddressData);

						}

					}

					WorkProfile workProfile = workProfileRepository.getWorkProfileByHpProfileId(hpProfileId);
					if (workProfile == null) {
						WorkProfile addWorkProfile = new WorkProfile();
						addWorkProfile.setBroadSpecialityId(
								hpProfileUpdateRequest.getSpecialityDetails().getBroadSpeciality().getId());
						addWorkProfile.setWorkNatureId(hpProfileUpdateRequest.getWorkDetails().getWorkNature().getId());
						addWorkProfile.setWorkStatusId(hpProfileUpdateRequest.getWorkDetails().getWorkStatus().getId());
						addWorkProfile.setIsUserCurrentlyWorking(
								hpProfileUpdateRequest.getWorkDetails().getIsUserCurrentlyWorking());
						addWorkProfile.setFacility(hpProfileUpdateRequest.getCurrentWorkDetails().getFacility());
						addWorkProfile.setUrl(hpProfileUpdateRequest.getCurrentWorkDetails().getUrl());
						addWorkProfile.setWorkOrganization(
								hpProfileUpdateRequest.getCurrentWorkDetails().getWorkOrganization());

						workProfileRepository.save(addWorkProfile);

					} else {
						workProfile.setBroadSpecialityId(
								hpProfileUpdateRequest.getSpecialityDetails().getBroadSpeciality().getId());
						workProfile.setWorkNatureId(hpProfileUpdateRequest.getWorkDetails().getWorkNature().getId());
						workProfile.setWorkStatusId(hpProfileUpdateRequest.getWorkDetails().getWorkStatus().getId());
						workProfile.setIsUserCurrentlyWorking(
								hpProfileUpdateRequest.getWorkDetails().getIsUserCurrentlyWorking());
						workProfile.setFacility(hpProfileUpdateRequest.getCurrentWorkDetails().getFacility());
						workProfile.setUrl(hpProfileUpdateRequest.getCurrentWorkDetails().getUrl());
						workProfile.setWorkOrganization(
								hpProfileUpdateRequest.getCurrentWorkDetails().getWorkOrganization());

						workProfileRepository.save(workProfile);

					}

					List<SuperSpecialityTO> newSuperSpecialities = hpProfileUpdateRequest.getSpecialityDetails()
							.getSuperSpeciality();
					List<SuperSpeciality> superSpecialities = superSpecialityRepository
							.getSuperSpecialityFromHpProfileId(hpProfileId);

					if (superSpecialities.size() == 0) {

						List<SuperSpeciality> superSpecialityList = new ArrayList<SuperSpeciality>();
						for (SuperSpecialityTO speciality : newSuperSpecialities) {

							SuperSpeciality superSpeciality = new SuperSpeciality();
							superSpeciality.setName(speciality.getName());
							superSpeciality.setHpProfileId(hpProfileId);

							superSpecialityList.add(superSpeciality);
						}
						superSpecialityRepository.saveAll(superSpecialityList);
					} else {

						superSpecialityRepository.deleteAll(superSpecialities);

						List<SuperSpeciality> superSpecialityList = new ArrayList<SuperSpeciality>();
						for (SuperSpecialityTO speciality : newSuperSpecialities) {

							SuperSpeciality superSpeciality = new SuperSpeciality();
							superSpeciality.setName(speciality.getName());
							superSpeciality.setHpProfileId(hpProfileId);

							superSpecialityList.add(superSpeciality);
						}
						superSpecialityRepository.saveAll(superSpecialityList);
					}

					RegistrationDetails registrationDetails = registrationDetailRepository
							.getRegistrationDetailsByHpProfileId(hpProfileId);
					if (registrationDetails == null) {

						RegistrationDetails addRegistrationDetail = new RegistrationDetails();
						addRegistrationDetail.setRegistrationDate(
								hpProfileUpdateRequest.getRegistrationDetail().getRegistrationDate());
						addRegistrationDetail
								.setRegistrationNo(hpProfileUpdateRequest.getImrDetails().getRegistrationNumber());
//						addRegistrationDetail
//								.setCouncilName(hpProfileUpdateRequest.getRegistrationDetail().getCouncilName());

						StateMedicalCouncilStatus stateMedicalCouncilStatus = iStateMedicalCouncilStatusRepository
								.findById(hpProfileUpdateRequest.getRegistrationDetail().getCouncilStatus().getId())
								.orElse(null);
						if (stateMedicalCouncilStatus != null) {
							addRegistrationDetail.setCouncilStatus(stateMedicalCouncilStatus);
						}

						addRegistrationDetail
								.setIsRenewable(hpProfileUpdateRequest.getRegistrationDetail().getIsRenewable());
						addRegistrationDetail.setRenewableRegistrationDate(
								hpProfileUpdateRequest.getRegistrationDetail().getRenewableRegistrationDate());
						addRegistrationDetail
								.setIsNameChange(hpProfileUpdateRequest.getRegistrationDetail().getIsNameChange());
						addRegistrationDetail.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
						addRegistrationDetail.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

						registrationDetailRepository.save(addRegistrationDetail);

					} else {
						registrationDetails.setRegistrationDate(
								hpProfileUpdateRequest.getRegistrationDetail().getRegistrationDate());
						registrationDetails
								.setRegistrationNo(hpProfileUpdateRequest.getImrDetails().getRegistrationNumber());

//						StateMedicalCouncilStatus stateMedicalCouncilStatus = iStateMedicalCouncilStatusRepository
//								.findById(hpProfileUpdateRequest.getRegistrationDetail().getCouncilStatus().getId())
//								.orElse(null);
//						if (stateMedicalCouncilStatus != null) {
//							registrationDetails.setCouncilStatus(stateMedicalCouncilStatus);
//						}

						registrationDetails
								.setIsRenewable(hpProfileUpdateRequest.getRegistrationDetail().getIsRenewable());
						registrationDetails.setRenewableRegistrationDate(
								hpProfileUpdateRequest.getRegistrationDetail().getRenewableRegistrationDate());
						registrationDetails
								.setIsNameChange(hpProfileUpdateRequest.getRegistrationDetail().getIsNameChange());
						registrationDetails.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

						registrationDetailRepository.save(registrationDetails);
					}

					List<QualificationDetails> qualificationDetailList = qualificationDetailRepository
							.getQualificationDetailsByRegistrationId(registrationDetails.getId());

					List<QualificationDetailRequestTO> newqualificationDetailTOList = hpProfileUpdateRequest
							.getQualificationDetail();

					if (qualificationDetailList.size() == 0) {
						List<QualificationDetails> qualificationDetails = new ArrayList<QualificationDetails>();
						for (QualificationDetailRequestTO newQualification : newqualificationDetailTOList) {

							QualificationDetails qualification = new QualificationDetails();

//							Country country = new Country();
//							country.setId(newQualification.getCountry().getId());
							qualification.setCountryId(newQualification.getCountry().getId());

//							State state = new State();
//							state.setId(newQualification.getState().getId());
							qualification.setStateId(newQualification.getState().getId());

//							College college = new College();
//							college.setId(newQualification.getCollege().getId());
							qualification.setCollegeId(newQualification.getCollege().getId());

//							University university = new University();
//							university.setId(newQualification.getUniversity().getId());
							qualification.setUniversityId(newQualification.getUniversity().getId());

//							FacilityType course = new FacilityType();
//							course.setId(newQualification.getCourse().getId());
							qualification.setCourseId(newQualification.getCourse().getId());

//							QualificationStatus qualificationStatus = iQualificationStatusRepository
//									.findById(newQualification.getQualificationStatus().getId()).orElse(null);
//							if (qualificationStatus != null) {
//								qualification.setQualificationStatus(qualificationStatus);
//							}

							qualification.setIsVerified(newQualification.getIsVerified());
							qualification.setQualificationYear(newQualification.getQualificationYear());
							qualification.setQualificationMonth(newQualification.getQualificationMonth());
							qualification.setIsNameChange(newQualification.getIsNameChange());
							qualification.setRegistrationDetails(registrationDetails);
							qualification.setHpProfile(hpProfile);

							qualification.setIsVerified(newQualification.getIsVerified());
							qualification.setEndDate(null);
							qualification.setCertificate(null);
							qualification.setName(null);
							qualification.setStartDate(null);
							qualification.setSystemOfMedicine(null);
							qualificationDetails.add(qualification);

						}

						qualificationDetailRepository.saveAll(qualificationDetails);

					} else {
						Integer cnt = 0;
						List<QualificationDetails> qualificationDetails = new ArrayList<QualificationDetails>();
						for (QualificationDetails qualification : qualificationDetailList) {
//							Country country = new Country();
//							country.setId(hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCountry()
//									.getId() == null ? null
//											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCountry()
////													.getId());
							qualification.setCountryId(hpProfileUpdateRequest.getQualificationDetail().get(cnt)
									.getCountry().getId() == null ? null
											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCountry()
													.getId());

//							State state = new State();
//							state.setId(
//									hpProfileUpdateRequest.getQualificationDetail().get(cnt).getState().getId() == null
//											? null
//											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getState()
//													.getId());
							qualification.setStateId(
									hpProfileUpdateRequest.getQualificationDetail().get(cnt).getState().getId() == null
											? null
											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getState()
													.getId());

//							College college = new College();
//							college.setId(hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCollege()
//									.getId() == null ? null
//											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCollege()
//													.getId());
							qualification.setCollegeId(hpProfileUpdateRequest.getQualificationDetail().get(cnt)
									.getCollege().getId() == null ? null
											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCollege()
													.getId());

//							University university = new University();
//							university.setId(hpProfileUpdateRequest.getQualificationDetail().get(cnt).getUniversity()
//									.getId() == null ? null
//											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getUniversity()
//													.getId());
							qualification.setUniversityId(hpProfileUpdateRequest.getQualificationDetail().get(cnt)
									.getUniversity().getId() == null ? null
											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getUniversity()
													.getId());

//							FacilityType course = new FacilityType();
//							course.setId(
//									hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCourse().getId() == null
//											? null
//											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCourse()
//													.getId());
							qualification.setCourseId(
									hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCourse().getId() == null
											? null
											: hpProfileUpdateRequest.getQualificationDetail().get(cnt).getCourse()
													.getId());

							qualification.setQualificationYear(
									hpProfileUpdateRequest.getQualificationDetail().get(cnt).getQualificationYear());
							qualification.setQualificationMonth(
									hpProfileUpdateRequest.getQualificationDetail().get(cnt).getQualificationMonth());
//							qualification.setId(hpProfileUpdateRequest.getQualificationDetail().get(cnt).getId());
							qualification.setIsNameChange(
									hpProfileUpdateRequest.getQualificationDetail().get(cnt).getIsNameChange());

							cnt += 1;
							qualificationDetails.add(qualification);
						}

						qualificationDetailRepository.saveAll(qualificationDetails);

					}

					CurrentWorkDetailsTO currentWorkDetailsTO = hpProfileUpdateRequest.getCurrentWorkDetails();

					if (currentWorkDetailsTO != null) {
						WorkProfile workProfileData = workProfileRepository.getWorkProfileByHpProfileId(hpProfileId);
						if (workProfileData != null) {
							workProfileData.setFacility(currentWorkDetailsTO.getFacility());
							workProfileData.setWorkOrganization(currentWorkDetailsTO.getWorkOrganization());
							workProfileData.setUrl(currentWorkDetailsTO.getUrl());

//							OrganizationType organizationType = organizationTypeRepository.findById(currentWorkDetailsTO.getOrganizationType().getId()).orElse(null);

							workProfileData.setOrganizationType(currentWorkDetailsTO.getOrganizationType() != null
									? currentWorkDetailsTO.getOrganizationType().getId()
									: null);

							workProfileRepository.save(workProfileData);
						} else {
							WorkProfile newWorkProfile = new WorkProfile();
							newWorkProfile.setFacility(currentWorkDetailsTO.getFacility());
							newWorkProfile.setWorkOrganization(currentWorkDetailsTO.getWorkOrganization());
							newWorkProfile.setUrl(currentWorkDetailsTO.getUrl());
//							OrganizationType organizationType = organizationTypeRepository.findById(currentWorkDetailsTO.getOrganizationType().getId()).orElse(null);

//							newWorkProfile.setOrganizationType(organizationType);
							newWorkProfile.setOrganizationType(currentWorkDetailsTO.getOrganizationType().getId());

							newWorkProfile.setHpProfileId(hpProfileId);
						}

					}

					AddressTO newAddress = hpProfileUpdateRequest.getCurrentWorkDetails().getAddress();

					if (newAddress != null) {
						Address workAddressData = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId,
								2);
						if (workAddressData == null) {

							Address addWorkAddress = new Address();

							if (newAddress.getState().getId() != null) {
								State workState = stateRepository.findById(newAddress.getState().getId()).orElse(null);
								addWorkAddress.setState(workState);
							}

							if (newAddress.getDistrict().getId() != null) {
								District workDistrict = districtRepository.findById(newAddress.getDistrict().getId())
										.orElse(null);
								addWorkAddress.setDistrict(workDistrict);

							}

							addWorkAddress.setPincode(newAddress.getPincode());
							addWorkAddress.setAddressLine1(
									newAddress.getAddressLine1() == null || newAddress.getAddressLine1() == "" ? null
											: newAddress.getAddressLine1());

							AddressType addressType = iAddressTypeRepository.findByAddressTypeId(BigInteger.TWO);
							addWorkAddress.setAddressTypeId(addressType);

							iAddressRepository.save(addWorkAddress);
						} else {
							workAddressData.setAddressLine1(newAddress.getAddressLine1());
							workAddressData.setPincode(newAddress.getPincode());

							if (newAddress.getState().getId() != null) {
								State workState = stateRepository.findById(newAddress.getState().getId()).orElse(null);
								workAddressData.setState(workState);
							}

							if (newAddress.getDistrict().getId() != null) {
								District workDistrict = districtRepository.findById(newAddress.getDistrict().getId())
										.orElse(null);
								workAddressData.setDistrict(workDistrict);

							}
							iAddressRepository.save(workAddressData);

						}
					}
					HpProfileUpdateResponseTO hpProfileUpdateResponseTO = new HpProfileUpdateResponseTO(204,
							"Record Updated Successfully!");
					return hpProfileUpdateResponseTO;

				} else {
					throw new InvalidRequestException("Invalid HP profile id!");
				}

			} else {
				throw new InvalidRequestException("Invalid Data!");
			}

		} else {
			throw new InvalidRequestException("Incomplete Data!");
		}

	}

	public HpProfileAddResponseTO addHpProfile(HpProfileAddRequestTO hpProfileAddRequest)
			throws InvalidRequestException {
		HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();

		if ((hpProfileAddRequest.getPersonalDetails().getSalutation() != null
				&& hpProfileAddRequest.getPersonalDetails().getSalutation() != "")
				&& (hpProfileAddRequest.getPersonalDetails().getAadhaarToken() != null
						&& hpProfileAddRequest.getPersonalDetails().getAadhaarToken() != "")
				&& (hpProfileAddRequest.getPersonalDetails().getFirstName() != null
						&& hpProfileAddRequest.getPersonalDetails().getFirstName() != "")
				&& (hpProfileAddRequest.getPersonalDetails().getCountryNationality() != null)
				&& (hpProfileAddRequest.getPersonalDetails().getCountryNationality() != null)
				&& (hpProfileAddRequest.getPersonalDetails().getDateOfBirth() != null)
				&& (hpProfileAddRequest.getPersonalDetails().getGender() != null
						&& hpProfileAddRequest.getPersonalDetails().getGender() != "")
				&& (hpProfileAddRequest.getPersonalDetails().getSchedule() != null)
				&& (hpProfileAddRequest.getCommunicationAddress().getFullName() != null
						&& hpProfileAddRequest.getCommunicationAddress().getFullName() != "")
				&& (hpProfileAddRequest.getCommunicationAddress().getAddressLine1() != null
						&& hpProfileAddRequest.getCommunicationAddress().getAddressLine1() != "")
				&& (hpProfileAddRequest.getCommunicationAddress().getVillage().getId() != null)
				&& (hpProfileAddRequest.getCommunicationAddress().getDistrict().getId() != null)
				&& (hpProfileAddRequest.getCommunicationAddress().getCountry().getId() != null)
				&& (hpProfileAddRequest.getCommunicationAddress().getState().getId() != null)
				&& (hpProfileAddRequest.getCommunicationAddress().getPincode() != null)
				&& (hpProfileAddRequest.getCommunicationAddress().getEmail() != null)
				&& (hpProfileAddRequest.getCommunicationAddress().getMobile() != null)

				&& (hpProfileAddRequest.getImrDetails().getNmrId() != null
						&& hpProfileAddRequest.getImrDetails().getNmrId() != "")
				&& (hpProfileAddRequest.getImrDetails().getRegistrationNumber() != null
						&& hpProfileAddRequest.getImrDetails().getRegistrationNumber() != "")
				&& (hpProfileAddRequest.getImrDetails().getYearOfInfo() != null
						&& hpProfileAddRequest.getImrDetails().getYearOfInfo() != "")

				&& (hpProfileAddRequest.getRegistrationDetail().getStateMedicalCouncil() != null)
				&& (hpProfileAddRequest.getRegistrationDetail().getRegistrationNumber() != null
						&& hpProfileAddRequest.getRegistrationDetail().getRegistrationNumber() != "")
				&& (hpProfileAddRequest.getRegistrationDetail().getRegistrationDate() != null)
				&& (hpProfileAddRequest.getRegistrationDetail().getIsRenewable() != null)
				&& (hpProfileAddRequest.getRegistrationDetail().getIsNameChange() != null)

				&& (hpProfileAddRequest.getSpecialityDetails().getBroadSpeciality() != null)

				&& (hpProfileAddRequest.getWorkDetails().getIsUserCurrentlyWorking() != null)
				&& (hpProfileAddRequest.getWorkDetails().getWorkNature().getId() != null)
				&& (hpProfileAddRequest.getWorkDetails().getWorkStatus().getId() != null)
				&& (hpProfileAddRequest.getCurrentWorkDetails().getUrl() != null
						&& hpProfileAddRequest.getCurrentWorkDetails().getUrl() != "")) {

			///////////////////////// Personal Details end /////////////////////////////////

			HpProfile hpProfile = new HpProfile();

			hpProfile.setSalutation(hpProfileAddRequest.getPersonalDetails().getSalutation());
			hpProfile.setAadhaarToken(hpProfileAddRequest.getPersonalDetails().getAadhaarToken());
			hpProfile.setFirstName(hpProfileAddRequest.getPersonalDetails().getFirstName());
			hpProfile.setMiddleName(hpProfileAddRequest.getPersonalDetails().getMiddleName());
			hpProfile.setLastName(hpProfileAddRequest.getPersonalDetails().getLastName());
			hpProfile.setFatherName(hpProfileAddRequest.getPersonalDetails().getFatherName());
			hpProfile.setMotherName(hpProfileAddRequest.getPersonalDetails().getMotherName());
			hpProfile.setSpouseName(hpProfileAddRequest.getPersonalDetails().getSpouseName());

			Country countryNationality = countryRepository
					.findById(hpProfileAddRequest.getPersonalDetails().getCountryNationality().getId()).orElse(null);
			if (countryNationality == null) {
				throw new InvalidRequestException("Incorrect Nationality!");
			} else {
				hpProfile.setCountryNationality(countryNationality);
			}

			Schedule schedule = iScheduleRepository
					.findById(hpProfileAddRequest.getPersonalDetails().getSchedule().getId()).orElse(null);
			if (schedule == null) {
				throw new InvalidRequestException("Incorrect Schedule!");
			} else {
				hpProfile.setSchedule(schedule);
				;
			}

			hpProfile.setGender(hpProfileAddRequest.getPersonalDetails().getGender());
			hpProfile.setDateOfBirth(hpProfileAddRequest.getPersonalDetails().getDateOfBirth());

			hpProfile.setFullName(hpProfileAddRequest.getCommunicationAddress().getFullName());
			hpProfile.setYearOfInfo(hpProfileAddRequest.getImrDetails().getYearOfInfo());
			hpProfile.setNmrId(hpProfileAddRequest.getImrDetails().getNmrId());

			HpProfile hProfile = iHpProfileRepository.save(hpProfile);

			BigInteger hProfileId = hpProfile.getId();

			List<LanguageTO> newLanguages = hpProfileAddRequest.getPersonalDetails().getLanguage();
			List<LanguagesKnown> languagesKnown = new ArrayList<LanguagesKnown>();
			if (newLanguages.size() > 0) {
				for (LanguageTO languageTo : newLanguages) {
					LanguagesKnown language = new LanguagesKnown();
					language.setLanguageId(languageTo.getId());
					language.setHpProfile(hProfile);
					languagesKnown.add(language);
				}
				languagesKnownRepository.saveAll(languagesKnown);
			}

			/////////////////////////// Personal Details end
			/////////////////////////// /////////////////////////////////

			/////////////////////////// Communication Details start
			/////////////////////////// /////////////////////////////////

			Address addAddressData = new Address();
			addAddressData.setAddressLine1(hpProfileAddRequest.getCommunicationAddress().getAddressLine1());

			Villages communicationVillage = villagesRepository
					.findById(hpProfileAddRequest.getCommunicationAddress().getVillage().getId()).orElse(null);
			if (communicationVillage != null) {
				addAddressData.setVillage(communicationVillage);
			}

			District communicationDistrict = districtRepository
					.findById(hpProfileAddRequest.getCommunicationAddress().getDistrict().getId()).orElse(null);
			if (communicationDistrict != null) {
				addAddressData.setDistrict(communicationDistrict);
			}

			SubDistrict communicationSubDistrict = subDistrictRepository
					.findById(hpProfileAddRequest.getCommunicationAddress().getSubDistrict().getId()).orElse(null);
			if (communicationSubDistrict != null) {
				addAddressData.setSubDistrict(communicationSubDistrict);
			}

			State communicationState = stateRepository
					.findById(hpProfileAddRequest.getCommunicationAddress().getState().getId()).orElse(null);
			if (communicationState != null) {
				addAddressData.setState(communicationState);
			}

//			Country communicationCountry = countryRepository.findById(hpProfileAddRequest.getCommunicationAddress().getCountry().getId()).orElse(null);
//			if (communicationCountry != null) {
//				addAddressData.setCountry(communicationCountry);
//			}

			addAddressData.setPincode(hpProfileAddRequest.getCommunicationAddress().getPincode());
			addAddressData.setEmail(hpProfileAddRequest.getCommunicationAddress().getEmail());
			addAddressData.setMobile(hpProfileAddRequest.getCommunicationAddress().getMobile());

			addAddressData.setHpProfileId(hProfileId);

			AddressType addressType = new AddressType();
			addressType.setId(hpProfileAddRequest.getCommunicationAddress().getAddressType().getId());
			addAddressData.setAddressTypeId(addressType);

			iAddressRepository.save(addAddressData);
			/////////////////////////// Communication Details end
			/////////////////////////// /////////////////////////////////

			/////////////////////////// Registration Number start
			/////////////////////////// /////////////////////////////////

			RegistrationDetails addRegistrationDetail = new RegistrationDetails();
			addRegistrationDetail
					.setRegistrationDate(hpProfileAddRequest.getRegistrationDetail().getRegistrationDate());
			addRegistrationDetail.setRegistrationNo(hpProfileAddRequest.getImrDetails().getRegistrationNumber());

			StateMedicalCouncil stateMedicalCouncil = iStateMedicalCouncilRepository
					.findById(hpProfileAddRequest.getRegistrationDetail().getStateMedicalCouncil().getId())
					.orElse(null);
//			if (stateMedicalCouncil != null) {
//				addRegistrationDetail.setStateMedicalCouncilId(stateMedicalCouncil.getId());
//			}

//			StateMedicalCouncilStatus stateMedicalCouncilStatus = iStateMedicalCouncilStatusRepository
//					.findById(hpProfileAddRequest.getRegistrationDetail().getCouncilStatus().getId())
//					.orElse(null);
//			if (stateMedicalCouncilStatus != null) {
//				addRegistrationDetail.setCouncilStatus(stateMedicalCouncilStatus);
//			}

			addRegistrationDetail.setIsRenewable(hpProfileAddRequest.getRegistrationDetail().getIsRenewable());

			addRegistrationDetail.setRenewableRegistrationDate(
					hpProfileAddRequest.getRegistrationDetail().getRenewableRegistrationDate());

			addRegistrationDetail.setIsNameChange(hpProfileAddRequest.getRegistrationDetail().getIsNameChange());

			addRegistrationDetail.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
			addRegistrationDetail.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
			addRegistrationDetail.setHpProfileId(hProfile);

			RegistrationDetails newRegistrationDetails = registrationDetailRepository.save(addRegistrationDetail);

			/////////////////////////// Registration Number end
			/////////////////////////// /////////////////////////////////

			////////////////////////// Qualification Data start
			////////////////////////// ////////////////////////////////
			List<QualificationDetailRequestTO> newqualificationDetailTOList = hpProfileAddRequest
					.getQualificationDetail();

			if (newqualificationDetailTOList.size() > 0) {
				List<QualificationDetails> qualificationDetails = new ArrayList<QualificationDetails>();
				for (QualificationDetailRequestTO newQualification : newqualificationDetailTOList) {

					QualificationDetails qualification = new QualificationDetails();

//					Country country = new Country();
//					country.setId(newQualification.getCountry().getId());
					qualification.setCountryId(newQualification.getCountry().getId());

//					State state = new State();
//					state.setId(newQualification.getState().getId());
					qualification.setStateId(newQualification.getState().getId());

//					College college = new College();
//					college.setId(newQualification.getCollege().getId());
					qualification.setCollegeId(newQualification.getCollege().getId());

//					University university = new University();
//					university.setId(newQualification.getUniversity().getId());
					qualification.setUniversityId(newQualification.getUniversity().getId());

//					FacilityType course = new FacilityType();
//					course.setId(newQualification.getCourse().getId());
					qualification.setCourseId(newQualification.getCourse().getId());

//					QualificationStatus qualificationStatus = iQualificationStatusRepository
//							.findById(newQualification.getQualificationStatus().getId()).orElse(null);
//					if (qualificationStatus != null) {
//						qualification.setQualificationStatus(qualificationStatus);
//					}

					qualification.setIsVerified(newQualification.getIsVerified());
					qualification.setQualificationYear(newQualification.getQualificationYear());
					qualification.setQualificationMonth(newQualification.getQualificationMonth());
					qualification.setIsNameChange(newQualification.getIsNameChange());
					qualification.setRegistrationDetails(newRegistrationDetails);
					qualification.setHpProfile(hpProfile);

					qualification.setIsVerified(newQualification.getIsVerified());
					qualification.setEndDate(null);
					qualification.setCertificate(null);
					qualification.setName(null);
					qualification.setStartDate(null);
					qualification.setSystemOfMedicine(null);
					qualificationDetails.add(qualification);

				}

				qualificationDetailRepository.saveAll(qualificationDetails);

			} else {

			}
			////////////////////////// Qualifictaion Data end
			////////////////////////// //////////////////////////////////
			////////////////////////// Super Speciality
			////////////////////////// start///////////////////////////////////
			List<SuperSpecialityTO> newSuperSpecialities = hpProfileAddRequest.getSpecialityDetails()
					.getSuperSpeciality();

			List<SuperSpeciality> superSpecialityList = new ArrayList<SuperSpeciality>();
			for (SuperSpecialityTO speciality : newSuperSpecialities) {

				SuperSpeciality superSpeciality = new SuperSpeciality();
				superSpeciality.setName(speciality.getName());
				superSpeciality.setHpProfileId(hProfileId);

				superSpecialityList.add(superSpeciality);
			}
			superSpecialityRepository.saveAll(superSpecialityList);
			///////////////////////// Super Speciality
			///////////////////////// ends/////////////////////////////////////

			///////////////////////// Work Details start ///////////////////////////////////

			WorkProfile newworkProfile = new WorkProfile();

			WorkNature newWorkNature = workNatureRepository
					.findById(hpProfileAddRequest.getWorkDetails().getWorkNature().getId()).orElse(null);

			if (newWorkNature != null) {
				newworkProfile.setWorkNatureId(newWorkNature.getId());
			}
			newworkProfile.setIsUserCurrentlyWorking(hpProfileAddRequest.getWorkDetails().getIsUserCurrentlyWorking());

			WorkStatus newWorkStatus = workStatusRepository
					.findById(hpProfileAddRequest.getWorkDetails().getWorkStatus().getId()).orElse(null);

			if (newWorkStatus != null) {
				newworkProfile.setWorkStatusId(newWorkStatus.getId());
			}

			newworkProfile.setHpProfileId(hProfileId);

			///////////////////////// Current Work Details start
			///////////////////////// ///////////////////////////////////

			AddressTO currentWorkAddress = hpProfileAddRequest.getCurrentWorkDetails().getAddress();

			Address newCurrentWorkAddress = new Address();

			State currentWorkState = stateRepository.findById(currentWorkAddress.getState().getId()).orElse(null);

			if (currentWorkState != null) {
				newCurrentWorkAddress.setState(currentWorkState);

			}

			District currentWorkDistrict = districtRepository.findById(currentWorkAddress.getDistrict().getId())
					.orElse(null);
			if (currentWorkDistrict != null) {
				newCurrentWorkAddress.setDistrict(currentWorkDistrict);

			}
			if (currentWorkAddress.getAddressLine1() != null && currentWorkAddress.getAddressLine1() != "") {
				newCurrentWorkAddress.setAddressLine1(currentWorkAddress.getAddressLine1());
			}

			if (currentWorkAddress.getPincode() != null && currentWorkAddress.getPincode() != "") {
				newCurrentWorkAddress.setPincode(currentWorkAddress.getPincode());
			}

			AddressType addressType2 = new AddressType();
			addressType2.setId(2);

			newCurrentWorkAddress.setAddressTypeId(addressType2);
			newCurrentWorkAddress.setHpProfileId(hProfileId);

			iAddressRepository.save(newCurrentWorkAddress);

			newworkProfile.setAddress(newCurrentWorkAddress);

			if (hpProfileAddRequest.getCurrentWorkDetails().getWorkOrganization() != null
					&& hpProfileAddRequest.getCurrentWorkDetails().getWorkOrganization() != "") {
				newworkProfile.setWorkOrganization(hpProfileAddRequest.getCurrentWorkDetails().getWorkOrganization());
			}

			OrganizationTypeTO newOrganizationType = hpProfileAddRequest.getCurrentWorkDetails().getOrganizationType();

			if (newOrganizationType != null) {
				newworkProfile.setOrganizationType(newOrganizationType.getId());
			}

			if (hpProfileAddRequest.getCurrentWorkDetails().getUrl() != null
					&& hpProfileAddRequest.getCurrentWorkDetails().getUrl() != "") {
				newworkProfile.setUrl(hpProfileAddRequest.getCurrentWorkDetails().getUrl());
			}

			newworkProfile.setHpProfileId(hProfileId);
			///////////////////////// Current Work Details end
			///////////////////////// ///////////////////////////////////

			workProfileRepository.save(newworkProfile);

			//////////////////////// Work Details end //////////////////////////////////////
			HpProfileAddResponseTO hpProfileAddResponseTO = new HpProfileAddResponseTO(201, "Record Created!",
					hProfileId);
			return hpProfileAddResponseTO;

		} else {
			throw new InvalidRequestException("Incomplete Data!");
		}

	}

	@Override
	public HpProfile findByUserDetail(BigInteger userDetailId) {
		return iHpProfileRepository.findByUserDetail(userDetailId);
	}
}
