package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.api.errors.BadRequestAlertException;
import com.saltlux.deepsignal.web.api.vm.ConnectomeParams;
import com.saltlux.deepsignal.web.api.vm.EntitiesDetailsParams;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.IConnectomeService;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.service.dto.ConnectomeDTO;
import com.saltlux.deepsignal.web.service.dto.ConnectomeNetworkDTO;
import com.saltlux.deepsignal.web.service.dto.ConnectomeNodeDTO;
import com.saltlux.deepsignal.web.service.dto.ConnectomeOnTextRequestBody;
import com.saltlux.deepsignal.web.service.dto.ConnectomePersonalDocumentDTO;
import com.saltlux.deepsignal.web.service.dto.ConnectomeStatusDTO;
import com.saltlux.deepsignal.web.service.dto.EdgeDTO;
import com.saltlux.deepsignal.web.service.dto.FeedRes;
import com.saltlux.deepsignal.web.service.dto.VertexDocsRes;
import com.saltlux.deepsignal.web.service.dto.VerticeDTO;
import com.saltlux.deepsignal.web.service.dto.entityUpdateRequestBody;
import com.saltlux.deepsignal.web.service.dto.miniConnectomeRequestBody;
import com.saltlux.deepsignal.web.service.impl.ConnectomeService;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.util.StringHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing the Connectome.
 */
@RestController
@RequestMapping("/api/connectome")
@Tag(name = "Connectome Management", description = "The connectome management API")
public class ConnectomeResource {

    private final Logger log = LoggerFactory.getLogger(ConnectomeResource.class);

    private static final String ENTITY_NAME = "connectome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IConnectomeService connectomeService;

    private final ConnectomeRepository connectomeRepository;

    private final ModelMapper modelMapper;

    @Autowired
    private ConnectAdapterApi connectAdapterApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    private final String API;

    private final String APIConnectome;
    private final String APIConnectomeTest;
    private final String APIEntityNetwork;

    public ConnectomeResource(
        ConnectomeService connectomeService,
        ModelMapper modelMapper,
        ApplicationProperties applicationProperties,
        ConnectomeRepository connectomeRepository
    ) {
        this.connectomeService = connectomeService;
        this.modelMapper = modelMapper;
        this.API = applicationProperties.getExternalApi().getDeepsignalAdapter();
        this.APIConnectomeTest = applicationProperties.getExternalApi().getDeepsignalConnectomeTest();
        this.connectomeRepository = connectomeRepository;
        this.APIConnectome = applicationProperties.getExternalApi().getDeepsignalConnectome();
        this.APIEntityNetwork = applicationProperties.getExternalApi().getDeepsignalEntityNetwork();
    }

    @Operation(summary = "Add a connectome", tags = { "Connectome Management" })
    @PostMapping("/create")
    public ResponseEntity<?> createConnectome(
        @Parameter(
            description = "Connectome to create. Cannot null or empty.",
            required = true,
            schema = @Schema(implementation = ConnectomeDTO.class)
        ) @RequestBody ConnectomeDTO connectomeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save connectome : {} ", connectomeDTO);

        if (connectomeDTO.getConnectomeId() != null) {
            throw new BadRequestAlertException(
                "A new connectome cannot already have an ID",
                ENTITY_NAME,
                Constants.ErrorKeys.IDEXISTS.label
            );
        }

        if (StringHelper.isNullOrEmptyString(connectomeDTO.getUser().getId())) {
            throw new BadRequestAlertException("User id cannot be empty", ENTITY_NAME, Constants.ErrorKeys.IDUSERNULL.label);
        }
        if (!connectomeDTO.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }

        Connectome result = connectomeService.save(modelMapper.map(connectomeDTO, Connectome.class));
        return ResponseEntity
            .created(new URI("/api/connectome/create/" + result.getConnectomeId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getConnectomeId()))
            .body(modelMapper.map(result, ConnectomeDTO.class));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get the current information connectome ", tags = { "Connectome Management" })
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        if (Objects.isNull(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(connectomeService.findByConnectomeId(id));
    }

    @GetMapping("/get/{connectomeId}")
    @Operation(summary = "get the current information connectome from outside ", tags = { "Connectome Management" })
    public ResponseEntity<?> getConnectomeByConnectomeId(@PathVariable("connectomeId") String connectomeId) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            ConnectomeParams params = new ConnectomeParams();
            params.setConnectomeId(connectomeId);
            Object result = restTemplate.postForObject(
                applicationProperties.getExternalApi().getDeepsignalConnectome() + "/connectome/get",
                params,
                Object.class
            );
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{connectomeId}")
    @Operation(summary = "Updates an existing Connectome", tags = { "Connectome Management" })
    @UserActivity(activityName = Constants.UserActivities.UPDATE_CONNECTOME_NAME)
    public ResponseEntity<Connectome> updateConnectomeName(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("connectomeName") String connectomeName
    ) {
        Optional<Connectome> connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId);
        if (connectome.isPresent()) {
            Connectome connectomeSave = connectome.get();
            connectomeSave.setConnectomeName(connectomeName);
            connectomeRepository.save(connectomeSave);
            return ResponseEntity.ok().body(connectomeSave);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/status/{connectomeId}")
    @Operation(summary = "get the connectome status ", tags = { "Connectome Management" })
    public ResponseEntity<?> getConnectomeStatusV2ByConnectomeId(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam(value = "sourceLang", defaultValue = "en") String sourceLang
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        StringBuilder url = new StringBuilder();
        url.append(APIConnectome).append(Constants.GET_CONNECTOMES_URI).append(Constants.GET_CONNECTOMES_STATUS_URI);
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(url.toString())
            .queryParam("id", connectomeId)
            .queryParam("sourceLang", sourceLang)
            .build()
            .toUriString();
        try {
            // JSONObject dummiesData = getDummiesConnectomeStatus((connectomeId));
            // System.out.println("dummies");
            // System.out.println(dummiesData);
            // return ResponseEntity.ok().body(new ResponseEntity<>(dummiesData, new HttpHeaders(), HttpStatus.OK));
            log.debug("REST request to connectome status : {} ", urlTemplate);
            HttpEntity<ConnectomeStatusDTO> response = restTemplate.getForEntity(urlTemplate, ConnectomeStatusDTO.class);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/v2/map/{connectomeId}")
    @Operation(summary = "get the connectome map ", tags = { "Connectome Management" })
    public ResponseEntity<?> getConnectomeMapV2ByConnectomeId(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam(value = "filterBlackEntity", defaultValue = "false") String filterBlackEntity,
        @RequestParam(value = "sourceLang", defaultValue = "en") String sourceLang
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        StringBuilder url = new StringBuilder();
        url.append(APIConnectome).append(Constants.GET_CONNECTOMES_URI);
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(url.toString())
            .queryParam("id", connectomeId)
            .queryParam("sourceLang", sourceLang)
            .build()
            .toUriString();
        try {
            // JSONObject dummiesData = getDummiesConnectomeData((connectomeId));
            // // System.out.println("dummies");
            // // System.out.println(dummiesData);
            // return ResponseEntity.ok().body(new ResponseEntity<>(dummiesData, new HttpHeaders(), HttpStatus.OK));
            HttpEntity<ConnectomeNetworkDTO> response = restTemplate.getForEntity(urlTemplate, ConnectomeNetworkDTO.class);

            ConnectomeNetworkDTO connectome = response.getBody();
            ConnectomeNetworkDTO connectomeFiltered = new ConnectomeNetworkDTO();
            ArrayList<VerticeDTO> verticesFiltered = new ArrayList<VerticeDTO>();
            ArrayList<EdgeDTO> edgesFiltered = new ArrayList<EdgeDTO>();

            // if (!Objects.isNull(filterBlackEntity) && filterBlackEntity.compareToIgnoreCase("true") == 0) {
            //     ArrayList<String> blackEntityLabels = new ArrayList<>();
            //     connectomeFiltered.setConnectomeId(connectome.getConnectomeId());
            //     connectomeFiltered.setConnectomeStatus(connectome.getConnectomeStatus());
            //     for (VerticeDTO vertice : connectome.getVertices()) {
            //         if (vertice.getType().compareToIgnoreCase("BLACK_ENTITY") == 0) {
            //             blackEntityLabels.add(vertice.getLabel());
            //         } else {
            //             verticesFiltered.add(vertice);
            //         }
            //     }
            //     for (EdgeDTO edge : connectome.getEdges()) {
            //         if (!blackEntityLabels.contains(edge.getFrom()) && !blackEntityLabels.contains(edge.getTo())) {
            //             edgesFiltered.add(edge);
            //         }
            //     }
            //     ArrayList<String> entities = new ArrayList<>();
            //     for (VerticeDTO vertice : verticesFiltered) {
            //         entities = new ArrayList<>();
            //         for (EdgeDTO edge : edgesFiltered) {
            //             if (edge.getFrom().compareToIgnoreCase(vertice.getLabel()) == 0) {
            //                 entities.add(edge.getTo());
            //             }
            //             if (edge.getTo().compareToIgnoreCase(vertice.getLabel()) == 0) {
            //                 entities.add(edge.getFrom());
            //             }
            //         }
            //         HashSet<String> hs = new HashSet<>();
            //         hs.addAll(entities);
            //         entities.clear();
            //         entities.addAll(hs);
            //         vertice.setEntities(entities);
            //     }

            //     connectomeFiltered.setVertices(verticesFiltered);
            //     connectomeFiltered.setEdges(edgesFiltered);

            //     return ResponseEntity
            //         .ok()
            //         .body(new ResponseEntity<ConnectomeNetworkDTO>(connectomeFiltered, response.getHeaders(), HttpStatus.OK));
            // } else {
            connectomeFiltered.setConnectomeId(connectome.getConnectomeId());
            connectomeFiltered.setConnectomeStatus(connectome.getConnectomeStatus());

            VerticeDTO tmpVertice = new VerticeDTO();
            VerticeDTO tmpCluster = new VerticeDTO();
            ArrayList<String> tmpEntities = new ArrayList<>();
            ArrayList<String> tmpClusterEntities = new ArrayList<>();
            EdgeDTO tmpLink = new EdgeDTO();

            //connectome node
            tmpEntities = new ArrayList<>();
            tmpVertice.setLabel(connectome.getConnectomeId());
            tmpVertice.setType("ROOT");
            tmpVertice.setMainCluster(connectome.getConnectomeId());
            tmpVertice.setFavorite(false);
            tmpVertice.setDisable(false);

            Set<String> clustersLabels = new HashSet<String>();
            Map<String, VerticeDTO> clustersByLabel = new HashMap<String, VerticeDTO>();
            Map<String, String> clusterByVertex = new HashMap<String, String>();
            for (VerticeDTO vertice : connectome.getVertices()) {
                if (!clustersLabels.contains(vertice.getMainCluster())) {
                    tmpCluster = new VerticeDTO();
                    tmpCluster.setLabel(vertice.getMainCluster());
                    tmpCluster.setType("CLUSTER");
                    tmpCluster.setMainCluster(connectome.getConnectomeId());
                    tmpCluster.setFavorite(false);
                    tmpCluster.setDisable(false);
                    tmpCluster.setEntities(new ArrayList<>());

                    tmpEntities.add(vertice.getMainCluster());
                    tmpLink = new EdgeDTO();
                    tmpLink.setLabel(connectome.getConnectomeId() + "=>" + vertice.getMainCluster());
                    tmpLink.setFrom(connectome.getConnectomeId());
                    tmpLink.setTo(vertice.getMainCluster());
                    //edgesFiltered.add(tmpLink);
                    clustersByLabel.put(vertice.getMainCluster(), tmpCluster);
                    clustersLabels.add(vertice.getMainCluster());
                }
                tmpCluster = clustersByLabel.get(vertice.getMainCluster());
                clusterByVertex.put(vertice.getLabel(), vertice.getMainCluster());
                tmpCluster.getEntities().add(vertice.getLabel());
                tmpLink = new EdgeDTO();
                tmpLink.setLabel(vertice.getMainCluster() + "=>" + vertice.getLabel());
                tmpLink.setFrom(vertice.getMainCluster());
                tmpLink.setTo(vertice.getLabel());
                edgesFiltered.add(tmpLink);
                clustersByLabel.put(vertice.getMainCluster(), tmpCluster);
            }

            for (VerticeDTO cluster : clustersByLabel.values()) {
                HashSet<String> hs = new HashSet<>();
                hs.addAll(cluster.getEntities());
                cluster.getEntities().clear();
                cluster.getEntities().addAll(hs);
            }

            verticesFiltered.addAll(clustersByLabel.values());

            HashSet<String> hs = new HashSet<>();
            hs.addAll(tmpEntities);
            tmpEntities.clear();
            tmpEntities.addAll(hs);
            tmpVertice.setEntities(tmpEntities);
            verticesFiltered.add(tmpVertice);

            //add links to connectome node
            Set<String> uniqueEdge = new HashSet<String>();
            String labelFromTo = null, labelToFrom = null;
            for (EdgeDTO edge : connectome.getEdges()) {
                labelFromTo = edge.getFrom() + "=>" + edge.getTo();
                labelToFrom = edge.getTo() + "=>" + edge.getFrom();
                if (!uniqueEdge.contains(labelFromTo) && !uniqueEdge.contains(labelToFrom)) {
                    uniqueEdge.add(edge.getLabel());
                    edgesFiltered.add(edge);
                    String clusterFrom = clusterByVertex.get(edge.getFrom());
                    String clusterTo = clusterByVertex.get(edge.getTo());
                    labelFromTo = clusterFrom + "=>" + clusterTo;
                    labelToFrom = clusterTo + "=>" + clusterFrom;
                    if (!clusterFrom.equals(clusterTo) && !uniqueEdge.contains(labelFromTo) && !uniqueEdge.contains(labelToFrom)) {
                        tmpLink = new EdgeDTO();
                        tmpLink.setLabel(clusterFrom + "=>" + clusterTo);
                        tmpLink.setFrom(clusterFrom);
                        tmpLink.setTo(clusterTo);
                        edgesFiltered.add(tmpLink);
                        uniqueEdge.add(clusterFrom + "=>" + clusterTo);
                    }
                }
            }

            for (VerticeDTO vertice : connectome.getVertices()) {
                tmpVertice = new VerticeDTO();
                tmpEntities = new ArrayList<>();
                tmpVertice.setLabel(vertice.getLabel());
                tmpVertice.setDfCnt(vertice.getDfCnt());
                tmpVertice.setType(vertice.getType());
                tmpVertice.setMainCluster(vertice.getMainCluster());
                tmpVertice.setClusters(vertice.getClusters());
                tmpVertice.setFavorite(vertice.getFavorite());
                tmpVertice.setDisable(vertice.getDisable());
                for (EdgeDTO edge : connectome.getEdges()) {
                    if (edge.getFrom().equals(vertice.getLabel())) {
                        tmpEntities.add(edge.getTo());
                    } else if (edge.getTo().equals(vertice.getLabel())) {
                        tmpEntities.add(edge.getFrom());
                    }
                }
                hs = new HashSet<>();
                hs.addAll(tmpEntities);
                tmpEntities.clear();
                tmpEntities.addAll(hs);
                tmpVertice.setEntities(tmpEntities);
                verticesFiltered.add(tmpVertice);
            }

            connectomeFiltered.setVertices(verticesFiltered);
            connectomeFiltered.setEdges(edgesFiltered);
            return ResponseEntity
                .ok()
                .body(new ResponseEntity<ConnectomeNetworkDTO>(connectomeFiltered, response.getHeaders(), HttpStatus.OK));
            //}
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private JSONObject getDummiesConnectomeData(String id) {
        LocalDateTime myLocalDateTime = LocalDateTime.now();
        LocalDateTime myUpdateTime = LocalDateTime.of(
            myLocalDateTime.getYear(),
            myLocalDateTime.getMonth(),
            myLocalDateTime.getDayOfMonth(),
            myLocalDateTime.getHour(),
            myLocalDateTime.getMinute(),
            0,
            0
        );

        int nodesCount = (myLocalDateTime.getMinute() + 1) * 2;
        Map<String, Object> connectomeStatus = new HashMap<>();
        connectomeStatus.put("connectomeId", id);
        connectomeStatus.put("status", "COMPLETED");
        connectomeStatus.put("firstCreatedDate", myUpdateTime);
        connectomeStatus.put("whenLearningStarted", myUpdateTime);
        connectomeStatus.put("lastUpdatedAt", myUpdateTime);
        connectomeStatus.put("numberOfTimesConnectomeWasUpdated", 0);
        connectomeStatus.put("numberOfDocuments", 0);
        connectomeStatus.put("numberOfUniqueEntities", 0);

        ArrayList<Map<String, Object>> listVertices = new ArrayList<Map<String, Object>>();
        ArrayList<Map<String, Object>> listEdges = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < nodesCount; i++) {
            Map<String, Object> vertice = new HashMap<>();
            vertice.put("label", i);
            vertice.put("dfCnt", 0);
            vertice.put("type", Math.round(Math.random() * 2) % 2 == 1 ? "ENTITY" : "CLUSTER");
            vertice.put("mainCluster", i);
            ArrayList<String> clusters = new ArrayList<String>();
            ArrayList<String> entities = new ArrayList<String>();
            vertice.put("favorite", Math.round(Math.random() * 2) % 2 == 1 ? true : false);
            vertice.put("disable", Math.round(Math.random() * 2) % 2 == 1 ? true : false);
            if (i > 3 && i < 60) {
                long linksCount = 2;
                for (int k = 1; k <= linksCount; k++) {
                    long to = i - k;
                    Map<String, Object> edge = new HashMap<>();
                    edge.put("label", i + "=>" + to);
                    edge.put("from", i);
                    edge.put("to", to);
                    entities.add(String.valueOf(to));
                    listEdges.add(edge);
                }
            } else {
                long linksCount = Math.round(Math.random() * 3) % 3;
                for (int k = 0; k < linksCount; k++) {
                    long to = Math.round(Math.random() * nodesCount) % nodesCount;
                    Map<String, Object> edge = new HashMap<>();
                    edge.put("label", i + "=>" + to);
                    edge.put("from", i);
                    edge.put("to", to);
                    entities.add(String.valueOf(to));
                    listEdges.add(edge);
                }
            }

            vertice.put("clusters", clusters);
            vertice.put("entities", entities);
            listVertices.add(vertice);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("connectomeId", id);
        response.put("connectomeStatus", connectomeStatus);
        response.put("vertices", listVertices);
        response.put("edges", listEdges);

        return new JSONObject(response);
    }

    private JSONObject getDummiesConnectomeStatus(String id) {
        LocalDateTime myLocalDateTime = LocalDateTime.now();
        LocalDateTime myUpdateTime = LocalDateTime.of(
            myLocalDateTime.getYear(),
            myLocalDateTime.getMonth(),
            myLocalDateTime.getDayOfMonth(),
            myLocalDateTime.getHour(),
            myLocalDateTime.getMinute(),
            0,
            0
        );
        Map<String, Object> connectomeStatus = new HashMap<>();
        connectomeStatus.put("connectomeId", id);
        connectomeStatus.put("status", "COMPLETED");
        connectomeStatus.put("firstCreatedDate", myUpdateTime);
        connectomeStatus.put("whenLearningStarted", myUpdateTime);
        connectomeStatus.put("lastUpdatedAt", myUpdateTime);
        connectomeStatus.put("numberOfTimesConnectomeWasUpdated", 0);
        connectomeStatus.put("numberOfDocuments", 0);
        connectomeStatus.put("numberOfUniqueEntities", 0);

        return new JSONObject(connectomeStatus);
    }

    @GetMapping("/entity/{srclang}/details")
    @Operation(summary = "Search card by id", tags = { "Connectome Entity Management" })
    public ResponseEntity<?> getDetailEntity(@PathVariable("srclang") String sourceLang, @RequestParam("label") String label) {
        if (StringUtils.isEmpty(label) || StringUtils.isEmpty(sourceLang)) {
            return ResponseEntity.badRequest().body("Entity's label is null or invalid");
        }
        try {
            EntitiesDetailsParams params = new EntitiesDetailsParams();
            params.setSourceLang(sourceLang);
            ArrayList<String> titles = new ArrayList<String>();
            titles.add(label);
            params.setTitles(titles);
            ArrayList<String> targetLanguage = new ArrayList<String>();
            params.setTargetLangList(targetLanguage);

            StringBuilder url = new StringBuilder();
            url.append(APIEntityNetwork).append(Constants.POST_CONNECTOMES_ENTITIES_DETAIL_URI);

            String response = restTemplate.postForObject(url.toString(), params, String.class);
            JSONArray responseListEntities = (JSONArray) new JSONParser().parse(response);

            ArrayList<Object> resultEntityList = new ArrayList<Object>();
            for (int indexRoot = 0; indexRoot < responseListEntities.size(); indexRoot++) {
                JSONObject entity = (JSONObject) responseListEntities.get(indexRoot);
                JSONObject social = null, googleInfobox = null, googleSocial = null;
                String wikiText = null;
                HashMap<String, Object> resultEntity = new HashMap<String, Object>();
                for (Object rootKey : entity.keySet()) {
                    if ("lang".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("lang", entity.get(rootKey));
                    }

                    if ("label".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("label", entity.get(rootKey));
                    }
                    if ("imageUrl".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("imageUrl", entity.get(rootKey));
                    }

                    if ("wikiUrl".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("wikiUrl", entity.get(rootKey));
                    }

                    if ("description".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("description", entity.get(rootKey));
                    }

                    if ("twitter".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("twitter", entity.get(rootKey));
                    }

                    if ("instagram".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("instagram", entity.get(rootKey));
                    }

                    if ("facebook".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("facebook", entity.get(rootKey));
                    }

                    if ("youtube".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("youtube", entity.get(rootKey));
                    }

                    if ("linkedin".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("linkedin", entity.get(rootKey));
                    }

                    if ("wikipediaInfobox".compareToIgnoreCase(rootKey.toString()) == 0) {
                        resultEntity.put("wikipediaInfobox", entity.get(rootKey));
                        //System.out.println("wikipediaInfobox :" + entity.get(rootKey).toString());
                    }

                    if ("social".compareToIgnoreCase(rootKey.toString()) == 0) {
                        social = (JSONObject) new JSONParser().parse(entity.get(rootKey).toString());
                    }

                    if ("googleInfobox".compareToIgnoreCase(rootKey.toString()) == 0) {
                        googleInfobox = (JSONObject) new JSONParser().parse(entity.get(rootKey).toString());
                    }

                    if ("wikiText".compareToIgnoreCase(rootKey.toString()) == 0) {
                        wikiText = entity.get(rootKey).toString();
                        if (wikiText.length() > 200) {
                            wikiText = wikiText.substring(0, 199) + "...";
                        }
                    }
                }

                if (wikiText != null && !wikiText.isEmpty()) {
                    resultEntity.put("description", wikiText);
                }

                if (googleInfobox != null) {
                    for (Object googleKey : googleInfobox.keySet()) {
                        if ("subtitle".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("subtitle", googleInfobox.get(googleKey));
                        }

                        if ("description_url".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("wikiUrl", googleInfobox.get(googleKey));
                        }

                        if ("description".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("description", googleInfobox.get(googleKey));
                        }

                        if ("logo".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("imageUrl", googleInfobox.get(googleKey));
                        }

                        if ("social".compareToIgnoreCase(googleKey.toString()) == 0) {
                            social = (JSONObject) new JSONParser().parse(googleInfobox.get(googleKey).toString());
                        }

                        if ("twitter".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("twitter", googleInfobox.get(googleKey));
                        }

                        if ("instagram".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("instagram", googleInfobox.get(googleKey));
                        }

                        if ("facebook".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("facebook", googleInfobox.get(googleKey));
                        }

                        if ("youtube".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("youtube", googleInfobox.get(googleKey));
                        }

                        if ("linkedin".compareToIgnoreCase(googleKey.toString()) == 0) {
                            resultEntity.put("linkedin", googleInfobox.get(googleKey));
                        }
                    }
                }

                if (social != null) {
                    for (Object socialKey : social.keySet()) {
                        if ("twitter".compareToIgnoreCase(socialKey.toString()) == 0) {
                            resultEntity.put("twitter", social.get(socialKey));
                        }

                        if ("instagram".compareToIgnoreCase(socialKey.toString()) == 0) {
                            resultEntity.put("instagram", social.get(socialKey));
                        }

                        if ("facebook".compareToIgnoreCase(socialKey.toString()) == 0) {
                            resultEntity.put("facebook", social.get(socialKey));
                        }

                        if ("youtube".compareToIgnoreCase(socialKey.toString()) == 0) {
                            resultEntity.put("youtube", social.get(socialKey));
                        }

                        if ("linkedin".compareToIgnoreCase(socialKey.toString()) == 0) {
                            resultEntity.put("linkedin", social.get(socialKey));
                        }
                    }
                }
                resultEntityList.add(resultEntity);
            }

            return new ResponseEntity<>(resultEntityList, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/entity/{connectomeId}/docs")
    @Operation(summary = "get the documents from a specific vertex ", tags = { "Connectome Management" })
    public ResponseEntity<?> getDocsEntity(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam(value = "sourceLang", defaultValue = "en") String sourceLang,
        @RequestParam("label") String label
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (Objects.isNull(label)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String uriConnectome = connectAdapterApi.getExternalApi() + "/connectome/" + connectomeId + "/vertices/docs";

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(uriConnectome)
            .queryParam("label", label)
            .queryParam("lang", sourceLang)
            .build()
            .toUriString();

        try {
            HttpEntity<VertexDocsRes> response = restTemplate.getForEntity(urlTemplate, VertexDocsRes.class);

            VertexDocsRes docs = response.getBody();

            System.out.println("docs" + docs.getPersonalDocuments().size());
            System.out.println("feeds" + docs.getFeeds().size());

            return ResponseEntity.ok().body(new ResponseEntity<>(docs, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/disable/entity")
    @Operation(summary = "update entity", tags = { "Connectome Entity Management" })
    public ResponseEntity<?> toggleDisableEntity(@RequestBody entityUpdateRequestBody requestBody) throws URISyntaxException {
        log.debug("REST request to update entity : {} ", requestBody);
        entityUpdateRequestBody requestBodyToAPI = new entityUpdateRequestBody();
        if (StringHelper.isNullOrEmptyString(requestBody.getVertexLabel())) {
            throw new BadRequestAlertException("Entity's label is null or invalid", "entity", Constants.ErrorKeys.LABELENTITYNULL.label);
        }
        requestBodyToAPI.setVertexLabel(requestBody.getVertexLabel());
        if (StringHelper.isNullOrEmptyString(requestBody.getConnectomeId())) {
            throw new BadRequestAlertException("connectomeId is null or invalid", "entity", Constants.ErrorKeys.LABELENTITYNULL.label);
        }
        requestBodyToAPI.setConnectomeId(requestBody.getConnectomeId());
        if (StringHelper.isNullOrEmptyString(requestBody.getSourceLang())) {
            requestBodyToAPI.setSourceLang("en");
        }
        try {
            StringBuilder url = new StringBuilder();
            url.append(APIConnectome).append(Constants.GET_CONNECTOMES_URI).append(Constants.PATCH_DISABLE_ENTITY_URI);

            VerticeDTO response = restTemplate.postForObject(url.toString(), requestBodyToAPI, VerticeDTO.class);

            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/favorite/entity")
    @Operation(summary = "update entity", tags = { "Connectome Entity Management" })
    public ResponseEntity<?> toggleFavoriteEntity(@RequestBody entityUpdateRequestBody requestBody) throws URISyntaxException {
        log.debug("REST request to update entity : {} ", requestBody);
        entityUpdateRequestBody requestBodyToAPI = new entityUpdateRequestBody();
        if (StringHelper.isNullOrEmptyString(requestBody.getVertexLabel())) {
            throw new BadRequestAlertException("Entity's label is null or invalid", "entity", Constants.ErrorKeys.LABELENTITYNULL.label);
        }
        requestBodyToAPI.setVertexLabel(requestBody.getVertexLabel());
        if (StringHelper.isNullOrEmptyString(requestBody.getConnectomeId())) {
            throw new BadRequestAlertException("connectomeId is null or invalid", "entity", Constants.ErrorKeys.LABELENTITYNULL.label);
        }
        requestBodyToAPI.setConnectomeId(requestBody.getConnectomeId());
        String sourceLang = "en";
        if (!StringHelper.isNullOrEmptyString(requestBody.getSourceLang())) {
            sourceLang = requestBody.getSourceLang();
        }
        requestBodyToAPI.setSourceLang(sourceLang);
        try {
            StringBuilder url = new StringBuilder();
            url.append(APIConnectome).append(Constants.GET_CONNECTOMES_URI).append(Constants.PATCH_FAVORITE_ENTITY_URI);

            VerticeDTO response = restTemplate.postForObject(url.toString(), requestBodyToAPI, VerticeDTO.class);

            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/mini-map/{connectomeId}")
    @Operation(summary = "get the mini connectome map ", tags = { "Connectome Management" })
    public ResponseEntity<?> postMiniConnectomeMapByConnectomeIdAndObjectIds(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestBody miniConnectomeRequestBody filterIDS
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (Objects.isNull(filterIDS)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        StringBuilder url = new StringBuilder();
        url.append(APIConnectome).append(Constants.POST_MINI_CONNECTOMES_URI);
        try {
            //check if it's feeds
            String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/getByIds";
            String strJson = connectAdapterApi.getDataFromAdapterApi(uri, null, HttpMethod.POST, null, filterIDS.getIds());
            ObjectMapper objectMapper = new ObjectMapper();
            FeedRes feedRes = objectMapper.readValue(strJson, FeedRes.class);
            System.out.println("Mini connectome Check is Feed");
            System.out.println(feedRes.getConnectomeFeeds().size());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("connectomeId", connectomeId);
            requestBody.put("sourceLang", filterIDS.getSourceLang());
            requestBody.put(
                "personalDocumentObjectIds",
                feedRes.getConnectomeFeeds().size() == 0 ? filterIDS.getIds() : new ArrayList<String>()
            );
            requestBody.put("feedObjectIds", feedRes.getConnectomeFeeds().size() > 0 ? filterIDS.getIds() : new ArrayList<String>());
            HttpEntity<Object> request = new HttpEntity<>(requestBody);
            HttpEntity<ConnectomeNetworkDTO> response = restTemplate.postForEntity(url.toString(), request, ConnectomeNetworkDTO.class);
            /*{sourceLang=en, personalDocumentObjectIds=[], feedObjectIds=[62b50aa7cea9a2500dd0a6e7], connectomeId=CID_92340659-a63c-4d72-b808-63461f40625f} */
            ConnectomeNetworkDTO connectome = response.getBody();
            ConnectomeNetworkDTO connectomeFiltered = new ConnectomeNetworkDTO();
            ArrayList<VerticeDTO> verticesFiltered = new ArrayList<VerticeDTO>();
            ArrayList<EdgeDTO> edgesFiltered = new ArrayList<EdgeDTO>();

            if (connectome == null) {
                System.out.println("Mini connectome Request Body");
                System.out.println(requestBody);
                return new ResponseEntity(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }
            connectomeFiltered.setConnectomeId(connectome.getConnectomeId());
            connectomeFiltered.setConnectomeStatus(connectome.getConnectomeStatus());

            VerticeDTO tmpVertice = new VerticeDTO();
            VerticeDTO tmpCluster = new VerticeDTO();
            ArrayList<String> tmpEntities = new ArrayList<>();
            ArrayList<String> tmpClusterEntities = new ArrayList<>();
            EdgeDTO tmpLink = new EdgeDTO();

            //connectome node
            tmpEntities = new ArrayList<>();
            tmpVertice.setLabel(connectome.getConnectomeId());
            tmpVertice.setType("ROOT");
            tmpVertice.setMainCluster(connectome.getConnectomeId());
            tmpVertice.setFavorite(false);
            tmpVertice.setDisable(false);

            Set<String> clustersLabels = new HashSet<String>();
            Map<String, VerticeDTO> clustersByLabel = new HashMap<String, VerticeDTO>();
            for (VerticeDTO vertice : connectome.getVertices()) {
                if (!clustersLabels.contains(vertice.getMainCluster())) {
                    tmpCluster = new VerticeDTO();
                    tmpCluster.setLabel(vertice.getMainCluster());
                    tmpCluster.setType("CLUSTER");
                    tmpCluster.setMainCluster(connectome.getConnectomeId());
                    tmpCluster.setFavorite(false);
                    tmpCluster.setDisable(false);
                    tmpCluster.setEntities(new ArrayList<>());

                    tmpEntities.add(vertice.getMainCluster());
                    tmpLink = new EdgeDTO();
                    tmpLink.setLabel(connectome.getConnectomeId() + "=>" + vertice.getMainCluster());
                    tmpLink.setFrom(connectome.getConnectomeId());
                    tmpLink.setTo(vertice.getMainCluster());
                    edgesFiltered.add(tmpLink);
                    clustersByLabel.put(vertice.getMainCluster(), tmpCluster);
                    //verticesFiltered.add(tmpCluster);
                    clustersLabels.add(vertice.getMainCluster());
                }
                tmpCluster = clustersByLabel.get(vertice.getMainCluster());
                tmpCluster.getEntities().add(vertice.getLabel());
                tmpLink = new EdgeDTO();
                tmpLink.setLabel(vertice.getMainCluster() + "=>" + vertice.getLabel());
                tmpLink.setFrom(vertice.getMainCluster());
                tmpLink.setTo(vertice.getLabel());
                edgesFiltered.add(tmpLink);
                clustersByLabel.put(vertice.getMainCluster(), tmpCluster);
            }
            verticesFiltered.addAll(clustersByLabel.values());

            HashSet<String> hs = new HashSet<>();
            hs.addAll(tmpEntities);
            tmpEntities.clear();
            tmpEntities.addAll(hs);
            tmpVertice.setEntities(tmpEntities);
            verticesFiltered.add(tmpVertice);

            //add links to connectome node
            Set<String> uniqueEdge = new HashSet<String>();
            String labelFromTo = null, labelToFrom = null;
            for (EdgeDTO edge : connectome.getEdges()) {
                labelFromTo = edge.getFrom() + "=>" + edge.getTo();
                labelToFrom = edge.getTo() + "=>" + edge.getFrom();

                if (!uniqueEdge.contains(labelFromTo) && !uniqueEdge.contains(labelToFrom)) {
                    uniqueEdge.add(edge.getLabel());
                    edgesFiltered.add(edge);
                }
            }

            for (VerticeDTO vertice : connectome.getVertices()) {
                tmpVertice = new VerticeDTO();
                tmpEntities = new ArrayList<>();
                tmpVertice.setLabel(vertice.getLabel());
                tmpVertice.setDfCnt(vertice.getDfCnt());
                tmpVertice.setType(vertice.getType());
                tmpVertice.setMainCluster(vertice.getMainCluster());
                tmpVertice.setClusters(vertice.getClusters());
                tmpVertice.setFavorite(vertice.getFavorite());
                tmpVertice.setDisable(vertice.getDisable());
                for (EdgeDTO edge : connectome.getEdges()) {
                    if (edge.getFrom().equals(vertice.getLabel())) {
                        tmpEntities.add(edge.getTo());
                    } else if (edge.getTo().equals(vertice.getLabel())) {
                        tmpEntities.add(edge.getFrom());
                    }
                }
                hs = new HashSet<>();
                hs.addAll(tmpEntities);
                tmpEntities.clear();
                tmpEntities.addAll(hs);
                tmpVertice.setEntities(tmpEntities);
                verticesFiltered.add(tmpVertice);
            }

            connectomeFiltered.setVertices(verticesFiltered);
            connectomeFiltered.setEdges(edgesFiltered);
            return ResponseEntity
                .ok()
                .body(new ResponseEntity<ConnectomeNetworkDTO>(connectomeFiltered, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // @PostMapping("/pd-map/{connectomeId}")
    // @Operation(summary = "get the document connectome map ", tags = { "Connectome Management" })
    // public ResponseEntity<?> postPDConnectomeMapByConnectomeIdAndDocIds(
    //     @PathVariable(value = "connectomeId") String connectomeId,
    //     @RequestBody miniConnectomeRequestBody filterIDS
    // ) {
    //     if (Objects.isNull(connectomeId)) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    //     }
    //     if (Objects.isNull(filterIDS)) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    //     }

    //     StringBuilder url = new StringBuilder();
    //     url.append(APIConnectome).append(Constants.POST_MINI_CONNECTOMES_URI);
    //     try {
    //         Map<String, Object> requestBody = new HashMap<>();
    //         requestBody.put("connectomeId", connectomeId);
    //         requestBody.put("sourceLang", filterIDS.getSourceLang());
    //         requestBody.put("personalDocumentObjectIds", filterIDS.getIds());
    //         requestBody.put("feedObjectIds", new ArrayList<String>());
    //         HttpEntity<Object> request = new HttpEntity<>(requestBody);
    //         HttpEntity<ConnectomeNetworkDTO> response = restTemplate.postForEntity(url.toString(), request, ConnectomeNetworkDTO.class);
    //         /*{sourceLang=en, personalDocumentObjectIds=[], feedObjectIds=[62b50aa7cea9a2500dd0a6e7], connectomeId=CID_92340659-a63c-4d72-b808-63461f40625f} */
    //         ConnectomeNetworkDTO connectome = response.getBody();

    //         ArrayList<EdgeDTO> edgesFiltered = new ArrayList<EdgeDTO>();

    //         if (connectome == null) {
    //             System.out.println("Doc connectome Request Body");
    //             System.out.println(requestBody);
    //             return new ResponseEntity(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
    //         }
    //         ArrayList<ConnectomeNodeDTO> verticesFiltered = new ArrayList<>();
    //         ConnectomeNodeDTO tmpVertice = new ConnectomeNodeDTO();
    //         ArrayList<String> tmpEntities = new ArrayList<>();
    //         //connectome node
    //         HashSet<String> hs = new HashSet<>();

    //         //add links to connectome node
    //         Set<String> uniqueEdge = new HashSet<String>();
    //         String labelFromTo = null, labelToFrom = null;
    //         for (EdgeDTO edge : connectome.getEdges()) {
    //             labelFromTo = edge.getFrom() + "=>" + edge.getTo();
    //             labelToFrom = edge.getTo() + "=>" + edge.getFrom();

    //             if (!uniqueEdge.contains(labelFromTo) && !uniqueEdge.contains(labelToFrom)) {
    //                 uniqueEdge.add(edge.getLabel());
    //                 edgesFiltered.add(edge);
    //             }
    //         }

    //         for (VerticeDTO vertice : connectome.getVertices()) {
    //             tmpVertice = new ConnectomeNodeDTO();
    //             tmpEntities = new ArrayList<>();
    //             tmpVertice.setWeight(vertice.getWeight());
    //             tmpVertice.setRelatedDocuments(filterIDS.getIds());
    //             tmpVertice.setLabel(vertice.getLabel());
    //             tmpVertice.setFavorite(vertice.getFavorite());
    //             tmpVertice.setDisable(vertice.getDisable());
    //             for (EdgeDTO edge : edgesFiltered) {
    //                 if (edge.getFrom().equals(vertice.getLabel())) {
    //                     tmpEntities.add(edge.getTo());
    //                 } else if (edge.getTo().equals(vertice.getLabel())) {
    //                     tmpEntities.add(edge.getFrom());
    //                 }
    //             }
    //             hs = new HashSet<>();
    //             hs.addAll(tmpEntities);
    //             tmpEntities.clear();
    //             tmpEntities.addAll(hs);
    //             tmpVertice.setLinkedNodes(tmpEntities);
    //             verticesFiltered.add(tmpVertice);
    //         }
    //         ConnectomePersonalDocumentDTO responseToSend = new ConnectomePersonalDocumentDTO();
    //         responseToSend.setId(filterIDS.getIds().get(0));
    //         responseToSend.setConnectome(verticesFiltered);

    //         return ResponseEntity
    //             .ok()
    //             .body(new ResponseEntity<ConnectomePersonalDocumentDTO>(responseToSend, response.getHeaders(), HttpStatus.OK));
    //     } catch (Exception e) {
    //         return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
    //     }
    // }

    @PostMapping("/text-map/{lang}")
    @Operation(summary = "get connectome map by title and content", tags = { "Connectome Management" })
    public ResponseEntity<?> postPDConnectomeMapByDocTitleAndContent(
        @PathVariable(value = "lang") String lang,
        @RequestBody ConnectomeOnTextRequestBody content
    ) {
        //return ResponseEntity.status(HttpStatus.OK).body(content);
        // System.out.println("Content postPDConnectomeMapByDocTitleAndContent");
        // System.out.println(content.getTitle());
        // System.out.println(content.getContent());

        if (Objects.isNull(content)) {
            return ResponseEntity.status(HttpStatus.OK).body(content);
        }
        // System.out.println("Content postPDConnectomeMapByDocTitleAndContent");
        // System.out.println(content.getTitle());

        StringBuilder url = new StringBuilder();
        url.append(APIConnectomeTest).append(Constants.POST_TEXT_CONNECTOME_URI).append("/").append(lang);
        try {
            //String requestBody = String.format("%s\r\n\r\n%s", content.getTitle(), content.getContent());
            //System.out.println(requestBody);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("connectomeId", content.getConnectomeId());
            requestBody.put("documentIds", content.getDocumentIds());
            requestBody.put("title", content.getTitle());
            requestBody.put("contents", content.getContent());
            System.out.println(requestBody);
            HttpEntity<Object> request = new HttpEntity<>(requestBody);
            HttpEntity<ConnectomeNodeDTO[]> response = restTemplate.postForEntity(url.toString(), request, ConnectomeNodeDTO[].class);
            /*{sourceLang=en, personalDocumentObjectIds=[], feedObjectIds=[62b50aa7cea9a2500dd0a6e7], connectomeId=CID_92340659-a63c-4d72-b808-63461f40625f} */
            ConnectomeNodeDTO[] connectome = response.getBody();

            // ArrayList<EdgeDTO> edgesFiltered = new ArrayList<EdgeDTO>();

            if (connectome == null) {
                System.out.println("Doc connectome Request Body");
                System.out.println(requestBody);
                return new ResponseEntity(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }
            // ArrayList<ConnectomeNodeDTO> verticesFiltered = new ArrayList<>();
            // ConnectomeNodeDTO tmpVertice = new ConnectomeNodeDTO();
            // ArrayList<String> tmpEntities = new ArrayList<>();
            // //connectome node
            // HashSet<String> hs = new HashSet<>();

            // //add links to connectome node
            // Set<String> uniqueEdge = new HashSet<String>();
            // String labelFromTo = null, labelToFrom = null;
            // for (EdgeDTO edge : connectome.getEdges()) {
            //     labelFromTo = edge.getFrom() + "=>" + edge.getTo();
            //     labelToFrom = edge.getTo() + "=>" + edge.getFrom();

            //     if (!uniqueEdge.contains(labelFromTo) && !uniqueEdge.contains(labelToFrom)) {
            //         uniqueEdge.add(edge.getLabel());
            //         edgesFiltered.add(edge);
            //     }
            // }

            // ArrayList<String> documentIds = new ArrayList<>();
            // if (!Objects.isNull(content.getDocumentId()) || !content.getDocumentId().isEmpty()) {
            //     documentIds.add(content.getDocumentId());
            // }
            // for (VerticeDTO vertice : connectome.getVertices()) {
            //     tmpVertice = new ConnectomeNodeDTO();
            //     tmpEntities = new ArrayList<>();
            //     tmpVertice.setWeight(vertice.getWeight());
            //     tmpVertice.setRelatedDocuments(documentIds);
            //     tmpVertice.setLabel(vertice.getLabel());
            //     tmpVertice.setFavorite(vertice.getFavorite());
            //     tmpVertice.setDisable(vertice.getDisable());
            //     for (EdgeDTO edge : edgesFiltered) {
            //         if (edge.getFrom().equals(vertice.getLabel())) {
            //             tmpEntities.add(edge.getTo());
            //         } else if (edge.getTo().equals(vertice.getLabel())) {
            //             tmpEntities.add(edge.getFrom());
            //         }
            //     }
            //     hs = new HashSet<>();
            //     hs.addAll(tmpEntities);
            //     tmpEntities.clear();
            //     tmpEntities.addAll(hs);
            //     tmpVertice.setLinkedNodes(tmpEntities);
            //     verticesFiltered.add(tmpVertice);
            // }
            ConnectomePersonalDocumentDTO responseToSend = new ConnectomePersonalDocumentDTO();
            responseToSend.setDocumentIds(content.getDocumentIds());
            responseToSend.setConnectome(connectome);

            return ResponseEntity
                .ok()
                .body(new ResponseEntity<ConnectomePersonalDocumentDTO>(responseToSend, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            return new ResponseEntity(new ApiResponse(false, "call of external api " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
