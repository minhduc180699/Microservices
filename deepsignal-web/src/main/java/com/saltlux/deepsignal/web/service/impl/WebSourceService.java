package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.WebSource;
import com.saltlux.deepsignal.web.domain.WebSourceTemplate;
import com.saltlux.deepsignal.web.repository.WebSourceRepository;
import com.saltlux.deepsignal.web.repository.WebSourceTemplateRepository;
import com.saltlux.deepsignal.web.service.IWebSourceService;
import com.saltlux.deepsignal.web.service.dto.WebSourceConditionDTO;
import com.saltlux.deepsignal.web.service.dto.WebSourceTornadoDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for managing Web source.
 */
@Service
@Transactional
public class WebSourceService implements IWebSourceService {

    private final WebSourceRepository webSourceRepository;

    private final WebSourceTemplateRepository webSourceTemplateRepository;

    private final ModelMapper modelMapper;

    private final String API;

    public WebSourceService(
        WebSourceRepository webSourceRepository,
        WebSourceTemplateRepository webSourceTemplateRepository,
        ApplicationProperties properties,
        ModelMapper modelMapper
    ) {
        this.webSourceRepository = webSourceRepository;
        this.webSourceTemplateRepository = webSourceTemplateRepository;
        this.API = properties.getExternalApi().getDeepsignalAdapter();
        this.modelMapper = modelMapper;
    }

    @Override
    public List<WebSource> findAll() {
        return webSourceRepository.findAll();
    }

    @Override
    public Optional<WebSource> findById(String id) {
        return webSourceRepository.findById(id);
    }

    @Override
    public WebSource save(WebSource webSource) {
        return webSourceRepository.save(webSource);
    }

    @Override
    public void remove(String id) {
        webSourceRepository.deleteById(id);
    }

    public WebSource createWebSource(WebSource webSource) {
        String webURL = webSource.getWebUrl() + webSource.getCondition();
        webSource.setWebUrl(webURL);
        return webSourceRepository.save(webSource);
    }

    public List<WebSource> addWebSourceCondition(WebSourceConditionDTO webSourceDTO) {
        List<WebSource> result = new ArrayList<>();
        List<WebSourceTemplate> webSourceTemplateList = webSourceTemplateRepository.findAll();
        Set<Connectome> connectomes = new HashSet<>();
        for (WebSourceTemplate source : webSourceTemplateList) {
            WebSource webSource = new WebSource();
            Connectome connectome = new Connectome();

            connectome.setConnectomeId(webSourceDTO.getConnectomeId());
            connectomes.add(connectome);

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
            webSource.setConnectomes(connectomes);
            result.add(webSource);
        }

        if (result.size() > 0) {
            result = webSourceRepository.saveAll(result);
        }
        // Call to Deepsignal API adapter to add keyword
        this.callSaveAllWebsourceAPI(webSourceDTO);
        return result;
    }

    /**
     * The method to call deepsignal API adapter to save a websource condition
     * @param webSourceConditionDTO
     * @return
     */
    @Async
    public WebSourceConditionDTO callAddConditionAPI(WebSourceConditionDTO webSourceConditionDTO) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(API + Constants.ADD_CONDITION_URI, webSourceConditionDTO, WebSourceConditionDTO.class);
    }

    //    /**
    //     * The method to call deepsignal API adapter to save a list websource conditions
    //     * @param webSources
    //     * @return
    //     */
    //    @Async
    //    public List<WebSourceTornadoDTO> callSaveAllWebsourceAPI(List<WebSource> webSources) {
    //        List<WebSourceTornadoDTO> webSourceTornadoDTOS = webSources
    //            .stream()
    //            .map(
    //                wb -> {
    //                    Set<WebSourceTornadoDTO.ProjectDTO> projects = wb
    //                        .getConnectomes()
    //                        .stream()
    //                        .map(c -> (new WebSourceTornadoDTO.ProjectDTO(c.getConnectomeId())))
    //                        .collect(Collectors.toSet());
    //
    //                    return new WebSourceTornadoDTO(
    //                        wb.getWebSourceId(),
    //                        wb.getWebUrl(),
    //                        wb.getCategory(),
    //                        wb.getCondition(),
    //                        wb.isAttachment(),
    //                        wb.isComment(),
    //                        wb.getLastModifiedAt(),
    //                        wb.getRegisteredAt(),
    //                        wb.getState(),
    //                        wb.getServiceCrawlerType(),
    //                        wb.getWebSourceName(),
    //                        wb.getServiceType(),
    //                        wb.getServiceLanguage(),
    //                        projects
    //                    );
    //                }
    //            )
    //            .collect(Collectors.toList());
    //        RestTemplate restTemplate = new RestTemplate();
    //
    //        restTemplate.postForObject(API + Constants.SAVE_ALL_WEBSOURCE_URI, webSourceTornadoDTOS, Object.class);
    //
    //        return webSourceTornadoDTOS;
    //    }

    public WebSourceConditionDTO callSaveAllWebsourceAPI(WebSourceConditionDTO webSourceConditionDTO) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(API + Constants.ADD_CONDITION_URI, webSourceConditionDTO, Object.class);
        return webSourceConditionDTO;
    }
}
