package in.gov.abdm.nmr.mapper;
import in.gov.abdm.nmr.dto.QuerySuggestionsTo;
import in.gov.abdm.nmr.entity.QuerySuggestions;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface QuerySuggestionsDtoMapper {

    QuerySuggestionsDtoMapper QUERY_SUGGESTIONS_DTO_MAPPER = Mappers.getMapper(QuerySuggestionsDtoMapper.class);

    List<QuerySuggestionsTo> querySuggestionToDto(List<QuerySuggestions> querySuggestions);

}
