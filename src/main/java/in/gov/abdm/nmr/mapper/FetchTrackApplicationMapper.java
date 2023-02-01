package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.FetchTrackApplicationResponseTO;
import in.gov.abdm.nmr.entity.FetchTrackApplication;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


//@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FetchTrackApplicationMapper {

    List<FetchTrackApplicationResponseTO> toFetchTrackApplicationResponseTO(List<FetchTrackApplication> iFetchSpecificDetails);
}
