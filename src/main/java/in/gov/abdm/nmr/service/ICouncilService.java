package in.gov.abdm.nmr.service;


import in.gov.abdm.nmr.nosql.entity.Council;

import java.util.List;

public interface ICouncilService {
    List<Council> getCouncilByRegistrationNumberAndCouncilName(String registrationNumber, String councilName);
}
