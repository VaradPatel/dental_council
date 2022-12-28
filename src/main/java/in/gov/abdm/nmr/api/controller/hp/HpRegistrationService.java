package in.gov.abdm.nmr.api.controller.hp;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.hp.to.HpProfileAddRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileAddResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.api.exception.InvalidRequestException;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.HpProfileDaoService;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.IHpProfileMapper;

@Service
public class HpRegistrationService implements IHpRegistrationService {

	private HpProfileDaoService hpProfileService;

	private IHpProfileMapper iHpProfileMapper;

	public HpRegistrationService(HpProfileDaoService hpProfileService, IHpProfileMapper iHpProfileMapper) {
		super();
		this.hpProfileService = hpProfileService;
		this.iHpProfileMapper = iHpProfileMapper;
	}

	@Override
	public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(
			SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
		return iHpProfileMapper
				.SmcRegistrationToDto(hpProfileService.fetchSmcRegistrationDetail(smcRegistrationDetailRequestTO));
	}

	@Override
	public HpProfileDetailResponseTO fetchHpProfileDetail(BigInteger hpProfileId) {
		return iHpProfileMapper.HpProfileDetailToDto(hpProfileService.fetchHpProfileDetail(hpProfileId));
	}

	@Override
	public HpProfileUpdateResponseTO updateHpProfileDetail(BigInteger hpProfileId,
			HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException {
		return iHpProfileMapper
				.HpProfileUpdateToDto(hpProfileService.updateHpProfile(hpProfileId, hpProfileUpdateRequest));
	}

	@Override
	public HpProfileAddResponseTO addHpProfileDetail(HpProfileAddRequestTO hpProfileUpdateRequest)
			throws InvalidRequestException {
		
		return iHpProfileMapper
				.HpProfileAddToDto(hpProfileService.addHpProfile(hpProfileUpdateRequest));
	}
}
