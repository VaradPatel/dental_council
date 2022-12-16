package in.gov.abdm.nmr.db.sql.domain.hp_profile;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.hp.to.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.api.exception.InvalidRequestException;
import in.gov.abdm.nmr.db.sql.domain.address.AddressTO;
import in.gov.abdm.nmr.db.sql.domain.address.IAddressRepository;
import in.gov.abdm.nmr.db.sql.domain.address_type.AddressTypeTO;
import in.gov.abdm.nmr.db.sql.domain.address_type.IAddressTypeRepository;
import in.gov.abdm.nmr.db.sql.domain.broad_speciality.BroadSpecialityTO;
import in.gov.abdm.nmr.db.sql.domain.college.CollegeTO;
import in.gov.abdm.nmr.db.sql.domain.country.CountryTO;
import in.gov.abdm.nmr.db.sql.domain.course.CourseTO;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictRepository;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.HpProfileDetailTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.HpSmcDetailTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.IHpProfileMapper;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.IMRDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.PersonalDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.SpecialityDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.WorkDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.language.LanguageTO;
import in.gov.abdm.nmr.db.sql.domain.organization_type.OrganizationTypeRepository;
import in.gov.abdm.nmr.db.sql.domain.qualification_detail.QualificationDetailRepository;
import in.gov.abdm.nmr.db.sql.domain.qualification_detail.QualificationDetailTO;
import in.gov.abdm.nmr.db.sql.domain.registration_detail.RegistrationDetailRepository;
import in.gov.abdm.nmr.db.sql.domain.registration_detail.RegistrationDetailTO;
import in.gov.abdm.nmr.db.sql.domain.state.StateRepository;
import in.gov.abdm.nmr.db.sql.domain.state.StateTO;
import in.gov.abdm.nmr.db.sql.domain.sub_district.SubDistrictTO;
import in.gov.abdm.nmr.db.sql.domain.super_speciality.SuperSpecialityRepository;
import in.gov.abdm.nmr.db.sql.domain.super_speciality.SuperSpecialityTO;
import in.gov.abdm.nmr.db.sql.domain.university.UniversityTO;
import in.gov.abdm.nmr.db.sql.domain.work_nature.WorkNatureTO;
import in.gov.abdm.nmr.db.sql.domain.work_profile.WorkProfileRepository;
import in.gov.abdm.nmr.db.sql.domain.work_status.WorkStatusTO;

@Service
public class HpProfileService implements IHpProfileService {

	private IHpProfileMapper ihHpProfileMapper;

	private IHpProfileRepository iHpProfileRepository;

	private IAddressRepository iAddressRepository;

	private RegistrationDetailRepository registrationDetailRepository;

	private QualificationDetailRepository qualificationDetailRepository;

	private WorkProfileRepository workProfileRepository;

	private SuperSpecialityRepository superSpecialityRepository;

	private DistrictRepository districtRepository;

	private StateRepository stateRepository;

	private IAddressTypeRepository iAddressTypeRepository;

	private OrganizationTypeRepository organizationTypeRepository;

	public HpProfileService(IHpProfileMapper ihHpProfileMapper, IHpProfileRepository iHpProfileRepository,
			IAddressRepository iAddressRepository, QualificationDetailRepository qualificationDetailRepository,
			RegistrationDetailRepository registrationDetailRepository, WorkProfileRepository workProfileRepository,
			SuperSpecialityRepository superSpecialityRepository, DistrictRepository districtRepository,
			StateRepository stateRepository) {
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
	}

	@PersistenceContext
	private EntityManager entityManager;

	public HpSmcDetailTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<HpProfile> criteria = builder.createQuery(HpProfile.class);
		Root<HpProfile> root = criteria.from(HpProfile.class);
		Join<Object, Object> registrationDetails = root.join(HpProfile_.REGISTRATION_DETAILS, JoinType.INNER);

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
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<HpProfile> criteria = builder.createQuery(HpProfile.class);
		Root<HpProfile> root = criteria.from(HpProfile.class);
		Join<Object, Object> registrationDetails = root.join(HpProfile_.REGISTRATION_DETAILS, JoinType.INNER);

		List<Predicate> predicates = new ArrayList<>();
		HpProfileDetailTO hpSmcDetailTO = new HpProfileDetailTO();
		RegistrationDetailTO registrationDetailTO = new RegistrationDetailTO();

		//////////// Registration Details Start/////////////////
		Tuple hpProfile = iHpProfileRepository.fetchHpProfileDetail(hpProfileId);
		if (hpProfile == null) {
			return new HpProfileDetailTO();
		}
		registrationDetailTO.setCouncilName(hpProfile.get("name", String.class));
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

			qualificationDetailTO.setQualificationMonth(appl.get("qualification_month", String.class));
			qualificationDetailTO.setQualificationYear(appl.get("qualification_year", String.class));
			qualificationDetailTO.setIs_name_change(appl.get("is_name_change", Integer.class));
			qualificationDetailTO.setId(appl.get("qualification_details_id", Integer.class));
			qualificationDetailTOList.add(qualificationDetailTO);
		}

		hpSmcDetailTO.setQualificationDetail(qualificationDetailTOList);

		//////////// Qualification Details end/////////////////

		//////////// Personal Details Start/////////////////////
		PersonalDetailsTO personaldetailsTO = new PersonalDetailsTO();
		personaldetailsTO.setSalutation(hpProfile.get("salutation", String.class));
		personaldetailsTO.setAadharNumber(hpProfile.get("aadhar_number", String.class));
		personaldetailsTO.setFirstName(hpProfile.get("first_name", String.class));
		personaldetailsTO.setMiddleName(hpProfile.get("middle_name", String.class));
		personaldetailsTO.setLastName(hpProfile.get("last_name", String.class));
		personaldetailsTO.setFatherName(hpProfile.get("father_name", String.class));
		personaldetailsTO.setMotherName(hpProfile.get("mother_name", String.class));
		personaldetailsTO.setSpouseName(hpProfile.get("spouse_name", String.class));
		personaldetailsTO.setNationality(hpProfile.get("nationality", String.class));

		List<LanguageTO> languageTOs = new ArrayList<LanguageTO>();
		for (LanguageTO language : languageTOs) {
			LanguageTO languageTO = new LanguageTO();
			languageTO.setName("");

			languageTOs.add(languageTO);
		}
		personaldetailsTO.setLanguage(languageTOs);

		personaldetailsTO.setDateOfBirth(hpProfile.get("date_of_birth", Date.class));
		personaldetailsTO.setGender(hpProfile.get("gender", String.class));
		personaldetailsTO.setSchedule("");
		hpSmcDetailTO.setPersonalDetails(personaldetailsTO);
		//////////// Personal Details Start/////////////////////

		//////////// Communication Address Details Start/////////////////////
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

			commAddressTO.setPincode(communicationAddress.get("pincode", String.class));
			commAddressTO.setCreatedDate(communicationAddress.get("created_date", String.class));
			commAddressTO.setUpdatedDate(communicationAddress.get("updated_date", String.class));
			commAddressTO.setEmail(communicationAddress.get("email", String.class));
			commAddressTO.setMobile(communicationAddress.get("mobile", String.class));
			commAddressTO.setFullName(hpProfile.get("full_name", String.class));
			hpSmcDetailTO.setCommunicationAddress(commAddressTO);
		}

		//////////// Communication Address Details end/////////////////////

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
			workNatureTO.setId(workProfileDetails.get("work_nature_id", BigInteger.class) == null ? BigInteger.ZERO
					: hpProfile.get("work_nature_id", BigInteger.class));
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
			currentAddressTO.setCreatedDate(currentAddress.get("created_date", String.class));
			currentAddressTO.setUpdatedDate(currentAddress.get("updated_date", String.class));
			currentAddressTO.setEmail(currentAddress.get("email", String.class));
			currentAddressTO.setMobile(currentAddress.get("mobile", String.class));
			currentAddressTO.setFullName(hpProfile.get("full_name", String.class));

			currentWorkDetailsTO.setAddress(currentAddressTO);
			hpSmcDetailTO.setCurrentWorkDetails(currentWorkDetailsTO);
		}
		//////////// Work profile Details end/////////////////////

		return hpSmcDetailTO;
	}

	public HpSmcDetailTO updateHpProfile(BigInteger hpProfileId, HpProfileUpdateRequestTO hpProfileUpdateRequest)
			throws InvalidRequestException {
		HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();
		return null;

	}
}
