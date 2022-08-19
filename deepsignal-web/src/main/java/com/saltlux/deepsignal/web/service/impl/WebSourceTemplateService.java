package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.WebSourceTemplate;
import com.saltlux.deepsignal.web.repository.WebSourceTemplateRepository;
import com.saltlux.deepsignal.web.service.IWebSourceTemplateService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Web source template.
 */
@Service
@Transactional
public class WebSourceTemplateService implements IWebSourceTemplateService {

    private final WebSourceTemplateRepository webSourceTemplateRepository;

    public WebSourceTemplateService(WebSourceTemplateRepository webSourceTemplateRepository) {
        this.webSourceTemplateRepository = webSourceTemplateRepository;
    }

    @Override
    public List<WebSourceTemplate> findAll() {
        return webSourceTemplateRepository.findAll();
    }

    @Override
    public Optional<WebSourceTemplate> findById(String id) {
        return webSourceTemplateRepository.findById(id);
    }

    @Override
    public WebSourceTemplate save(WebSourceTemplate webSourceTemplate) {
        return webSourceTemplateRepository.save(webSourceTemplate);
    }

    @Override
    public void remove(String id) {
        webSourceTemplateRepository.deleteById(id);
    }
}
