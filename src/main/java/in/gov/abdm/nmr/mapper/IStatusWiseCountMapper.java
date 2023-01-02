package in.gov.abdm.nmr.mapper;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper Interface to ensure smooth transfer of data between two different Beans
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IStatusWiseCountMapper {

        /**
         * This method transforms the StatusWiseCount Bean to the StatusWiseCountTO Bean
         * by transferring its contents
         * @param iStatusWiseCount
         * @return StatusWiseCountTO
         */
        StatusWiseCountTO toStatusWiseCountTO(IStatusWiseCount iStatusWiseCount);
}
