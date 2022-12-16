package in.gov.abdm.nmr.db.sql.domain.hp_profile;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IHpProfileRepository extends JpaRepository<HpProfile, BigInteger> {
	
	@Query(value = "select registration_details.hp_profile_id, registration_no, state_medical_council.name, full_name "
			+ " from hp_profile INNER JOIN registration_details on hp_profile_id = hp_profile.id"
			+ " INNER JOIN state_medical_council ON state_medical_council.id = council_name"
			+ " where registration_no = :registrationNo and council_name = :councilName", nativeQuery = true)
	Tuple fetchSmcRegistrationDetail(String registrationNo, Integer councilName);

	@Query(value = "select registration_details.id as registration_id, registration_details.hp_profile_id, full_name, nmr_id, year_of_info, registration_no, registration_date, state_medical_council.name, state_medical_council.id, first_name, father_name,"
			+ " salutation, nationality, gender, aadhar_number, middle_name, mother_name, last_name, spouse_name, date_of_birth, "
			+ " is_renewable, renewable_registration_date, is_name_change  "
			+ " from hp_profile"
			+ " INNER JOIN registration_details on hp_profile_id = hp_profile.id"
			+ " INNER JOIN state_medical_council ON state_medical_council.id = council_name"
			+ " where registration_details.hp_profile_id = :hpProfileId", nativeQuery = true)
	Tuple fetchHpProfileDetail(BigInteger hpProfileId);
	
	@Query(value = "select course_name, course.id as course_id, country.name as country_name, country.id as country_id, state.id as state_id, state.name as state_name, "
			+ " colleges.name as college_name, colleges.id as college_id, universities.name as university_name, "
			+ " universities.id as university_id, qualification_month, qualification_year, is_name_change, qualification_details.id as qualification_details_id "
			+ " from qualification_details "
			+ " LEFT JOIN country on country.id = country "
			+ " LEFT JOIN state on state.id = state "
			+ " LEFT JOIN colleges on colleges.id = qualification_details.college "
			+ " LEFT JOIN universities on universities.id = qualification_details.university "
			+ " LEFT JOIN course on course.id = course "
			+ " where qualification_details.registration_details_id = :registrationId", nativeQuery = true)
	List<Tuple> fetchQualificationDetail(Integer registrationId);
	
	@Query(value = "select broad_speciality.name as broad_speciality_name, broad_speciality.id as broad_speciality_id, is_user_currently_working,"
			+ " work_nature.id as work_nature_id, work_nature.name as work_nature_name, work_status.id as work_status_id,"
			+ " work_status.name as work_status_name, facility, work_organization, url, pincode,"
			+ " district.name as district_name, district.id as district_id, state.name as state_name, state.id as state_id,"
			+ " organization_type.id as organization_type_id, organization_type.name as organization_type_name "
			+ " from work_profile "
			+ " LEFT JOIN broad_speciality on broad_speciality.id = work_profile.broad_speciality_id"
			+ " LEFT JOIN work_nature on work_nature.id = work_profile.work_nature_id"
			+ " LEFT JOIN work_status on work_status.id = work_profile.work_status_id"
			+ " LEFT JOIN district on work_status.id = work_profile.district_id"
			+ " LEFT JOIN organization_type on organization_type.id = work_profile.organization_type"
			+ " LEFT JOIN state on work_status.id = work_profile.state_id"
			+ " where work_profile.hp_profile_id = :hpProfileId", nativeQuery = true)
	Tuple fetchWorkProfileDetails(BigInteger hpProfileId);
	
	@Query(value = "select address_line1, country.name as country_name, country.id as country_id, state.id as state_id, state.name as state_name, "
			+ " city.name as city_name, city.id as city_id, district.name as district_name, district.id as district_id,  "
			+ " sub_district.id as sub_district_id, sub_district.name as sub_district_name, pincode, address_type.id as address_type_id,"
			+ " address_type.address_type as address_type_name, created_date, updated_date, email, mobile  "
			+ " from address "
			+ " LEFT JOIN address_type on address_type.id = address.address_type "
			+ " LEFT JOIN country on country.id = country "
			+ " LEFT JOIN state on state.id = state "
			+ " LEFT JOIN district on district.id = district "
			+ " LEFT JOIN sub_district on sub_district.id = sub_district "
			+ " LEFT JOIN city on city.id = city "
			+ " where address.hp_profile = :hpProfileId and address_type.id = :addressType", nativeQuery = true)
	Tuple fetchCommunicationAddress(BigInteger hpProfileId, Integer addressType);
	
	@Query(value = "select address_line1, country.name as country_name, country.id as country_id, state.id as state_id, state.name as state_name, "
			+ " city.name as city_name, city.id as city_id, district.name as district_name, district.id as district_id,  "
			+ " sub_district.id as sub_district_id, sub_district.name as sub_district_name, pincode, address_type.id as address_type_id,"
			+ " address_type.address_type as address_type_name, created_date, updated_date, email, mobile  "
			+ " from address "
			+ " LEFT JOIN country on country.id = country "
			+ " LEFT JOIN state on state.id = state "
			+ " LEFT JOIN district on district.id = district "
			+ " LEFT JOIN sub_district on sub_district.id = sub_districts "
			+ " LEFT JOIN city on city.id = city "
			+ " LEFT JOIN address_type on address_type.id = address.address_type "
			+ " where address.hp_profile = :hpProfileId and address_type.id = :addressType", nativeQuery = true)
	Tuple fetchCurrentAddress(BigInteger hpProfileId, Integer addressType);
	
	@Query(value = "select super_speciality.name as speciality_name, super_speciality.id as speciality_id "
			+ " from super_speciality "
			+ " where super_speciality.hp_profile_id = :hpProfileId", nativeQuery = true)
	List<Tuple> fetchSuperSpecialityDetails(BigInteger hpProfileId);
	
	@Query(value = "select * from hp_profile where id = :hpProfileId", nativeQuery = true)
	HpProfile getByHpProfileId(BigInteger hpProfileId);
}
