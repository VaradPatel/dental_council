package in.gov.abdm.nmr.api.controller.dashboard.to;
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
         * @param statusWiseCount
         * @return StatusWiseCountTO
         */
        StatusWiseCountTO toStatusWiseCountTO(StatusWiseCount statusWiseCount);
}
