package in.gov.abdm.nmr.api.controller.hp.to;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.address.Address;
import in.gov.abdm.nmr.db.sql.domain.address.AddressTO;
import in.gov.abdm.nmr.db.sql.domain.broad_speciality.BroadSpeciality;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.IMRDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.PersonalDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.SpecialityDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.WorkDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.organization_type.OrganizationTypeTO;
import in.gov.abdm.nmr.db.sql.domain.qualification_detail.QualificationDetailRequestTO;
import in.gov.abdm.nmr.db.sql.domain.qualification_detail.QualificationDetailTO;
import in.gov.abdm.nmr.db.sql.domain.registration_detail.RegistrationDetailTO;
import in.gov.abdm.nmr.db.sql.domain.state.StateTO;
import in.gov.abdm.nmr.db.sql.domain.super_speciality.SuperSpeciality;
import in.gov.abdm.nmr.db.sql.domain.work_nature.WorkNatureTO;
import in.gov.abdm.nmr.db.sql.domain.work_status.WorkStatusTO;
import lombok.Value;

@Value
public class HpProfileUpdateResponseTO {

	private Integer status;
	private String message;
}
