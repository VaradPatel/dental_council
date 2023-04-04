package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.QueryCreateTo;
import in.gov.abdm.nmr.dto.QueryResponseTo;
import in.gov.abdm.nmr.jpa.entity.Queries;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface QueriesDtoMapper {

    List<Queries> queryDtoToData(List<QueryCreateTo> queryCreateTos);
    List<QueryCreateTo> queryDataToDto(List<Queries> queries);
    List<QueryResponseTo> queryDataToOpenQueriesDto(List<Queries> queries);
}
