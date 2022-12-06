package in.gov.abdm.nmr.db.sql.domain.language;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class LanguageService implements ILanguageService {

    public LanguageRepository languageRepository;

    private LanguageDtoMapper languageDtoMapper;

    public LanguageService( LanguageRepository languageRepository, LanguageDtoMapper languageDtoMapper) {
        this.languageRepository = languageRepository;
        this.languageDtoMapper = languageDtoMapper;
    }

    @Override
    public List<LanguageTO> getLanguageData() {
        return languageDtoMapper.LanguageDataToDto(languageRepository.getLanguage());

    }
}
