package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalRequestParam;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import org.springframework.data.domain.Pageable;

/**
 * An interface for the customized repository of work flow repository
 */
public interface IWorkFlowCustomRepository {
    /**
     * Customized Repository Interface with a method for fetching the reactivation records
     * of the health professionals for the NMC to approve or reject their reactivation request.
     *
     * @param reactivateHealthProfessionalQueryParam - Object with all the attributes related to pagination, filter and sorting
     * @param pageable                               - Object of Pageable that helps in pagination
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    ReactivateHealthProfessionalResponseTO getReactivationRecordsOfHealthProfessionalsToNmc(ReactivateHealthProfessionalRequestParam reactivateHealthProfessionalQueryParam, Pageable pageable);
}
