package com.saltlux.deepsignal.adapter.service.impl;

import com.saltlux.deepsignal.adapter.domain.Project;
import com.saltlux.deepsignal.adapter.domain.WebSource;
import com.saltlux.deepsignal.adapter.domain.WebSourceTemplate;
import com.saltlux.deepsignal.adapter.repository.ProjectRepository;
import com.saltlux.deepsignal.adapter.repository.WebSourceRepository;
import com.saltlux.deepsignal.adapter.repository.WebSourceTemplateRepository;
import com.saltlux.deepsignal.adapter.service.IWebSourceService;
import com.saltlux.deepsignal.adapter.service.dto.WebSourceConditionDTO;
import java.util.*;
import liquibase.util.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Web source.
 */
@Service
@Transactional
public class WebSourceService implements IWebSourceService {

    private final WebSourceRepository webSourceRepository;

    private final WebSourceTemplateRepository webSourceTemplateRepository;

    private final ProjectRepository projectRepository;

    public WebSourceService(
        WebSourceRepository webSourceRepository,
        WebSourceTemplateRepository webSourceTemplateRepository,
        ProjectRepository projectRepository
    ) {
        this.webSourceRepository = webSourceRepository;
        this.webSourceTemplateRepository = webSourceTemplateRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<WebSource> findAll() {
        return webSourceRepository.findAll();
    }

    @Override
    public Optional<WebSource> findById(Long id) {
        return webSourceRepository.findById(id);
    }

    @Override
    public WebSource save(WebSource webSource) {
        return webSourceRepository.save(webSource);
    }

    @Override
    public List<WebSource> saveAll(List<WebSource> webSources) {
        return webSourceRepository.saveAll(webSources);
    }

    @Override
    public void remove(Long id) {
        webSourceRepository.deleteById(id);
    }

    @Override
    public List<WebSource> createWebSource(WebSource webSource) {
        List<WebSource> result = new ArrayList<>();
        List<WebSourceTemplate> webSourceTemplateList = webSourceTemplateRepository.findAll();
        for (WebSourceTemplate source : webSourceTemplateList) {
            String webURL = source.getWebUrl().replace("$query$", webSource.getCondition());
            String websourceId = MD5Util.computeMD5(webURL);

            webSource.setWebSourceId(websourceId);
            webSource.setCategory(webSource.getCategory());
            webSource.setCondition(webSource.getCondition());
            webSource.setWebSourceName(webSource.getWebSourceName());
            webSource.setWebUrl(webURL);
            webSource.setServiceLanguage(source.getServiceLanguage());
            webSource.setServiceType(source.getServiceType());
            webSource.setServiceCrawlerType(source.getServiceCrawlerType().getServiceCrawlerType());
            result.add(webSource);
        }
        return webSourceRepository.saveAll(result);
    }

    @Override
    public WebSourceConditionDTO addWebSourceCondition(WebSourceConditionDTO webSourceDTO) {
        List<WebSource> result = new ArrayList<>();
        List<WebSourceTemplate> webSourceTemplateList = webSourceTemplateRepository.findAll();
        Set<Project> projects = new HashSet<>();
        for (WebSourceTemplate source : webSourceTemplateList) {
            WebSource webSource = new WebSource();
            Project project = new Project();

            project.setProjectId(webSourceDTO.getConnectomeId());
            project.setCompanyId("deepsignal");
            project.setProjectName(webSourceDTO.getConnectomeName());
            projects.add(project);

            String webURL = source.getWebUrl().replace("$query$", webSourceDTO.getCondition());
            if (webSourceRepository.existsByWebUrl(webURL)) {
                continue;
            }

            webSource.setCategory(webSourceDTO.getCategory());
            webSource.setCondition(webSourceDTO.getCondition());
            webSource.setWebSourceName(webSourceDTO.getWebSourceName());
            webSource.setWebUrl(webURL);
            webSource.setServiceLanguage(source.getServiceLanguage());
            webSource.setServiceType(source.getServiceType());
            webSource.setServiceCrawlerType(source.getServiceCrawlerType().getServiceCrawlerType());
            webSource.setProjects(projects);

            result.add(webSource);
        }
        projectRepository.saveAll(projects);

        webSourceRepository.saveAll(result);

        return webSourceDTO;
    }

    @Override
    public List<WebSource> updateWebSourceCondition(WebSourceConditionDTO webSourceDTO) {
        List<WebSource> webSources = findAll();
        for (WebSource source : webSources) {
            source.setCondition(webSourceDTO.getCondition());
        }
        return webSourceRepository.saveAll(webSources);
    }
}
