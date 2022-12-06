package in.gov.abdm.nmr.db.sql.domain.language;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface LanguageDtoMapper {

    List<LanguageTO> LanguageDataToDto(List<Language> language); 


}
