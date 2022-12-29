package in.gov.abdm.nmr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.mapper.LanguageDtoMapper;
import in.gov.abdm.nmr.repository.LanguageRepository;
import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.service.ILanguageService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LanguageServiceImpl implements ILanguageService {

    public LanguageRepository languageRepository;

    private LanguageDtoMapper languageDtoMapper;

    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageDtoMapper languageDtoMapper) {
        this.languageRepository = languageRepository;
        this.languageDtoMapper = languageDtoMapper;
    }

    @Override
    public List<LanguageTO> getLanguageData() {
        return languageDtoMapper.LanguageDataToDto(languageRepository.getLanguage());

    }
}
