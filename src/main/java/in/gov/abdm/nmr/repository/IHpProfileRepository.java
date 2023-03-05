package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;

public interface IHpProfileRepository extends JpaRepository<HpProfile, BigInteger> {

    /**
     * Represents a query to fetch the SMC registration details of a healthcare professional.
     *
     * @param registrationNo The registration number of the healthcare professional.
     * @param councilId      The ID of the state medical council.
     * @return A tuple containing the HP profile ID, registration number, name of the state medical council, and full name of the healthcare professional.
     */
    @Query(value = "select registration_details.hp_profile_id, registration_no, state_medical_council.name, full_name "
            + " from hp_profile INNER JOIN registration_details on hp_profile_id = hp_profile.id"
            + " INNER JOIN state_medical_council ON state_medical_council.id = :councilId"
            + " where registration_id = :registrationNo", nativeQuery = true)
    Tuple fetchSmcRegistrationDetail1(BigInteger registrationNo, Integer councilId);

    @Query(value = """
            select hp.id, rd.registration_no , smc."name",hp.full_name,smc.id from main.hp_profile hp 
            inner join main.registration_details rd  on rd.hp_profile_id  = hp.id
            INNER JOIN main.state_medical_council smc ON smc.id = rd.state_medical_council_id
            where rd.registration_no = :registrationNo and smc.id =:councilId order by rd.hp_profile_id desc limit 1""", nativeQuery = true)
    Tuple fetchSmcRegistrationDetail(String registrationNo, Integer councilId);

    @Query(value = "select registration_details.id as registration_id, hp_profile.request_id, registration_details.hp_profile_id, full_name, nmr_id, year_of_info,transaction_id, e_sign_status ,registration_no, registration_date,"
            + " state_medical_council.name as state_medical_council_name, state_medical_council.id as state_medical_council_id, state_medical_council_status.id as state_medical_council_status_id, state_medical_council_status.name as state_medical_council_status_name, first_name, father_name,"
            + " salutation, gender, aadhaar_token, middle_name, mother_name, last_name, spouse_name, date_of_birth, "
            + " is_renewable, renewable_registration_date, is_name_change, country.id as nationality_id, country.nationality as nationality_name, "
            + " schedule.id as schedule_id, schedule.name as schedule_name "
            + " from hp_profile"
            + " LEFT JOIN registration_details on hp_profile_id = hp_profile.id"
            + " LEFT JOIN state_medical_council ON state_medical_council.id = state_medical_council_id"
            + " LEFT JOIN state_medical_council_status ON state_medical_council_status.id = registration_details.council_status"
            + " LEFT JOIN country ON country.id = hp_profile.country_nationality_id"
            + " LEFT JOIN schedule ON schedule.id = hp_profile.schedule_id"
            + " where registration_details.hp_profile_id = :hpProfileId", nativeQuery = true)
    Tuple fetchHpProfileDetail(BigInteger hpProfileId);

    @Query(value = "select course_name, course.id as course_id, country.name as country_name, country.id as country_id, state.id as state_id, state.name as state_name, "
            + " colleges.name as college_name, colleges.id as college_id, universities.name as university_name, "
            + " universities.id as university_id, qualification_month, qualification_year, is_name_change, is_verified, qualification_details.id as qualification_details_id "
//			+ " qualification_status.id as qualification_status_id, qualification_status.name as qualification_status_name "
            + " from qualification_details "
            + " LEFT JOIN country on country.id = country_id "
            + " LEFT JOIN state on state.id = state_id "
            + " LEFT JOIN colleges on colleges.id = qualification_details.college_id "
            + " LEFT JOIN universities on universities.id = qualification_details.university_id "
            + " LEFT JOIN course on course.id = course_id "
//			+ " LEFT JOIN qualification_status on qualification_status.id = qualification_status "
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
            + " villages.name as villages_name, villages.id as villages_id, district.name as district_name, district.id as district_id,  "
            + " sub_district.id as sub_district_id, sub_district.name as sub_district_name, pincode, address_type.id as address_type_id,"
            + " address_type.address_type as address_type_name, email, mobile  "
            + " from address "
            + " LEFT JOIN address_type on address_type.id = address.address_type_id "
            + " LEFT JOIN country on country.id = address.country_id "
            + " LEFT JOIN state on state.id = address.state_id"
            + " LEFT JOIN district on district.id = address.district_id "
            + " LEFT JOIN sub_district on sub_district.id = address.sub_district_id "
            + " LEFT JOIN villages on villages.id = address.village_id "
            + " where address.hp_profile_id = :hpProfileId and address_type.id = :addressType", nativeQuery = true)
    Tuple fetchCommunicationAddress(BigInteger hpProfileId, Integer addressType);

    @Query(value = "select address_line1, country.name as country_name, country.id as country_id, state.id as state_id, state.name as state_name, "
            + " villages.name as village_name, villages.id as village_id, district.name as district_name, district.id as district_id,  "
            + " sub_district.id as sub_district_id, sub_district.name as sub_district_name, pincode, address_type.id as address_type_id,"
            + " address_type.address_type as address_type_name, address.created_at, address.updated_at, email, mobile  "
            + " from address "
            + " LEFT JOIN country on country.id = address.country_id "
            + " LEFT JOIN state on state.id = address.state_id "
            + " LEFT JOIN district on district.id = address.district_id "
            + " LEFT JOIN sub_district on sub_district.id = address.sub_district_id "
            + " LEFT JOIN villages on villages.id = address.village_id "
            + " LEFT JOIN address_type on address_type.id = address.address_type_id "
            + " where address.hp_profile_id = :hpProfileId and address_type.id = :addressType", nativeQuery = true)
    Tuple fetchCurrentAddress(BigInteger hpProfileId, Integer addressType);

    @Query(value = "select super_speciality.name as speciality_name, super_speciality.id as speciality_id "
            + " from super_speciality "
            + " where super_speciality.hp_profile_id = :hpProfileId", nativeQuery = true)
    List<Tuple> fetchSuperSpecialityDetails(BigInteger hpProfileId);

    @Query(value = "select language.name as language, language.id as language_id "
            + " from languages_known"
            + " LEFT JOIN language on languages_known.language_id = language.id"
            + " where languages_known.hp_profile_id = :hpProfileId", nativeQuery = true)
    List<Tuple> fetchLanguageDetails(BigInteger hpProfileId);

    @Query(value = "SELECT * FROM hp_profile WHERE id=(SELECT MAX(id) FROM hp_profile WHERE user_id=:userId)", nativeQuery = true)
    HpProfile findLatestEntryByUserid(BigInteger userId);

    HpProfile findHpProfileById(BigInteger id);

    @Query(value = "SELECT hp FROM hpProfile hp where hp.registrationId=:registrationId")
    List<HpProfile> findByRegistrationId(String registrationId);

    @Query(value = "SELECT * FROM hp_profile where registration_id =:registrationId ORDER BY id DESC LIMIT 1 OFFSET 1", nativeQuery = true)
    HpProfile findSecondLastHpProfile(String registrationId);

}
