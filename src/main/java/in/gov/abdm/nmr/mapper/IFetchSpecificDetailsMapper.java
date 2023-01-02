package in.gov.abdm.nmr.mapper;
import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper Interface to ensure smooth transfer of data between two different Beans
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IFetchSpecificDetailsMapper {

        /**
         * This method transforms the StatusWiseCount Bean to the StatusWiseCountTO Bean
         * by transferring its contents
         * @param iFetchSpecificDetails
         * @return FetchSpecificDetailsResponseTO
         */
        FetchSpecificDetailsResponseTO toFetchSpecificDetailsResponseTO(IFetchSpecificDetails iFetchSpecificDetails);
}
