package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.service.dto.CollectionCreateRequestBody;
import com.saltlux.deepsignal.web.service.dto.CollectionResponseDTO;
import com.saltlux.deepsignal.web.service.dto.CollectionsItemResponseDTO;
import com.saltlux.deepsignal.web.service.dto.ConnectomeNodeDTO;
import com.saltlux.deepsignal.web.service.dto.DocumentMapRequestBody;
import com.saltlux.deepsignal.web.service.dto.DocumentMapResponse;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * REST controller for managing the Connectome.
 */
@RestController
@RequestMapping("/api/collections")
@Tag(name = "Collection Management", description = "The collection management API")
public class CollectionResource {

    private final Logger log = LoggerFactory.getLogger(CollectionResource.class);

    private static final String ENTITY_NAME = "connectome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelMapper modelMapper;

    @Autowired
    private ConnectAdapterApi connectAdapterApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    private final String APIContextualMemory;
    private final String APIRecommendation;
    private final String APIConnectome;

    public CollectionResource(ModelMapper modelMapper, ApplicationProperties applicationProperties) {
        this.modelMapper = modelMapper;
        this.APIContextualMemory = applicationProperties.getExternalApi().getDeepsignalConnectomeTest();
        this.APIRecommendation = applicationProperties.getExternalApi().getDeepsignalRecommendationTest();
        this.APIConnectome = applicationProperties.getExternalApi().getDeepsignalConnectomeTest();
    }

    // #region collection manager
    @PostMapping("/document-map")
    @Operation(summary = "get document map from docId", tags = { "Document map manager" })
    public ResponseEntity<?> postDocumentMapByDocId(@RequestHeader HttpHeaders headers, @RequestBody DocumentMapRequestBody content) {
        if (Objects.isNull(content) || Objects.isNull(content.getDocumentId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(content);
        }

        if (Objects.isNull(headers.get("lang")) || headers.get("lang").size() < 1 || headers.get("lang").get(0).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        StringBuilder url = new StringBuilder();
        url.append(APIContextualMemory).append(Constants.POST_TEXT_CONNECTOME_URI).append("/").append(headers.get("lang").get(0));

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("connectomeId", headers.get("connectomeId").get(0));
            requestBody.put("documentId", content.getDocumentId());
            requestBody.put("title", null);
            requestBody.put("contents", null);
            System.out.println(requestBody.get("connectomeId"));
            System.out.println(requestBody.get("documentId"));
            HttpEntity<Object> request = new HttpEntity<>(requestBody);
            HttpEntity<ConnectomeNodeDTO[]> response = restTemplate.postForEntity(url.toString(), request, ConnectomeNodeDTO[].class);

            ConnectomeNodeDTO[] nodeList = response.getBody();

            if (nodeList == null) {
                System.out.println("Doc map Request Body");
                System.out.println(requestBody.get("connectomeId"));
                System.out.println(requestBody.get("documentId"));
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }

            if (nodeList.length == 0) {
                System.out.println("Doc map is empty");
                System.out.println(requestBody.get("connectomeId"));
                System.out.println(requestBody.get("documentId"));
            }

            DocumentMapResponse responseToSend = new DocumentMapResponse();
            responseToSend.setDocumentId(content.getDocumentId());
            responseToSend.setNodeList(nodeList);

            return ResponseEntity.ok().body(new ResponseEntity<DocumentMapResponse>(responseToSend, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(false, "call of external api [" + url.toString() + "]" + e.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/tmp/create")
    @Operation(
        summary = "create a temp collection with connectomeId and language and list docId if exist",
        tags = { "Collections manager" }
    )
    public ResponseEntity<?> createTmpCollectionWithConnectomeIdAndLanguageAndDocIds(
        @RequestHeader HttpHeaders headers,
        @RequestBody CollectionCreateRequestBody content
    ) {
        if (Objects.isNull(headers.get("lang")) || headers.get("lang").size() < 1 || headers.get("lang").get(0).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        StringBuilder url = new StringBuilder();
        url.append(APIContextualMemory).append(Constants.POST_CONTEXTUAL_MEMORY_GET_TMP_COLLECTION_URI);
        CollectionCreateRequestBody requestBody = new CollectionCreateRequestBody();
        if (
            !Objects.isNull(content) &&
            !Objects.isNull(content.getDocumentIdList()) &&
            !Objects.isNull(content.getDocumentIdList().size() > 0)
        ) {
            requestBody.setDocumentIdList(content.getDocumentIdList());
        }

        try {
            HttpEntity<CollectionCreateRequestBody> request = new HttpEntity<>(requestBody, headers);
            HttpEntity<CollectionResponseDTO> response = restTemplate.postForEntity(url.toString(), request, CollectionResponseDTO.class);

            CollectionResponseDTO collectionResponse = response.getBody();

            if (collectionResponse == null) {
                System.out.println("Doc connectome Request Body");
                System.out.println(requestBody);
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }

            return ResponseEntity
                .ok()
                .body(new ResponseEntity<CollectionResponseDTO>(collectionResponse, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            System.out.println(requestBody.getDocumentIdList().toString());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(
                    false,
                    "call of external api [" + Constants.POST_CONTEXTUAL_MEMORY_CREATE_COLLECTION_URI + "]" + e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/create")
    @Operation(summary = "create a collection with connectomeId and language and list docId if exist", tags = { "Collections manager" })
    public ResponseEntity<?> createCollectionWithConnectomeIdAndLanguage(
        @RequestHeader HttpHeaders headers,
        @RequestBody CollectionCreateRequestBody content
    ) {
        if (Objects.isNull(headers.get("lang")) || headers.get("lang").size() < 1 || headers.get("lang").get(0).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        StringBuilder url = new StringBuilder();
        url.append(APIContextualMemory).append(Constants.POST_CONTEXTUAL_MEMORY_CREATE_COLLECTION_URI);
        CollectionCreateRequestBody requestBody = new CollectionCreateRequestBody();
        if (
            !Objects.isNull(content) &&
            !Objects.isNull(content.getDocumentIdList()) &&
            !Objects.isNull(content.getDocumentIdList().size() > 0)
        ) {
            requestBody.setDocumentIdList(content.getDocumentIdList());
        }

        try {
            HttpEntity<CollectionCreateRequestBody> request = new HttpEntity<>(requestBody, headers);
            // HttpEntity<CollectionResponseDTO[]> response =
            // restTemplate.exchange(urlTemplate.toString(), HttpMethod.POST,
            // entity,CollectionResponseDTO[].class);
            // HttpEntity<Object> request = new HttpEntity<>(requestBody);
            HttpEntity<CollectionResponseDTO> response = restTemplate.postForEntity(url.toString(), request, CollectionResponseDTO.class);

            CollectionResponseDTO collectionResponse = response.getBody();

            if (collectionResponse == null) {
                System.out.println("Doc connectome Request Body");
                System.out.println(requestBody);
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }

            return ResponseEntity
                .ok()
                .body(new ResponseEntity<CollectionResponseDTO>(collectionResponse, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            System.out.println(requestBody.getDocumentIdList().toString());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(
                    false,
                    "call of external api [" + Constants.POST_CONTEXTUAL_MEMORY_CREATE_COLLECTION_URI + "]" + e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/all")
    @Operation(summary = "get collections from connectomeId and language", tags = { "Collections manager" })
    public ResponseEntity<?> getCollectionsByConnectomeIdAndLanguage(@RequestHeader HttpHeaders headers) {
        if (
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        StringBuilder url = new StringBuilder();
        url.append(APIContextualMemory).append(Constants.GET_CONTEXTUAL_MEMORY_GET_COLLECTION_LIST_URI);
        try {
            if (Objects.isNull(headers.get("lang"))) {
                headers.add("lang", "en");
            }

            if (headers.get("lang").isEmpty()) {
                headers.set("lang", "en");
            }

            HttpEntity<String> entity = new HttpEntity<String>(headers);
            HttpEntity<CollectionsItemResponseDTO[]> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                entity,
                CollectionsItemResponseDTO[].class
            );

            CollectionsItemResponseDTO[] collectionsResponse = response.getBody();

            if (collectionsResponse == null) {
                System.out.println("get collection Request Body");
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }

            return ResponseEntity
                .ok()
                .body(new ResponseEntity<CollectionsItemResponseDTO[]>(collectionsResponse, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            for (String iterable_element : headers.toSingleValueMap().values()) {
                System.out.println(iterable_element);
            }
            System.out.println(headers.get("lang"));
            System.out.println(headers.get("connectomeId"));
            System.out.println(headers.get("lang").get(0));
            System.out.println(headers.get("connectomeId").get(0));
            System.out.println(e.getMessage());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(
                    false,
                    "call of external api [" + Constants.GET_CONTEXTUAL_MEMORY_GET_COLLECTION_LIST_URI + "]" + e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get")
    @Operation(summary = "get a collection with collectionId", tags = { "Collections manager" })
    public ResponseEntity<?> getCollectionsByCollectionId(@RequestHeader HttpHeaders headers) {
        if (
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (
            Objects.isNull(headers.get("collectionId")) ||
            Objects.isNull(headers.get("collectionId").size() < 1) ||
            headers.get("collectionId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        StringBuilder url = new StringBuilder();
        url.append(APIContextualMemory).append(Constants.GET_CONTEXTUAL_MEMORY_GET_COLLECTION_URI);
        try {
            if (Objects.isNull(headers.get("lang"))) {
                headers.add("lang", "en");
            }

            if (headers.get("lang").isEmpty()) {
                headers.set("lang", "en");
            }

            HttpEntity<String> entity = new HttpEntity<String>(headers);
            HttpEntity<CollectionResponseDTO> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                entity,
                CollectionResponseDTO.class
            );

            CollectionResponseDTO collectionsResponse = response.getBody();

            if (collectionsResponse == null) {
                System.out.println("get collection Request Body");
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }

            return ResponseEntity
                .ok()
                .body(new ResponseEntity<CollectionResponseDTO>(collectionsResponse, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            for (String iterable_element : headers.toSingleValueMap().values()) {
                System.out.println(iterable_element);
            }
            System.out.println(headers.get("lang"));
            System.out.println(headers.get("connectomeId"));
            System.out.println(headers.get("collectionId"));
            System.out.println(e.getMessage());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(
                    false,
                    "call of external api [" + Constants.GET_CONTEXTUAL_MEMORY_GET_COLLECTION_URI + "]" + e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/update")
    @Operation(summary = "update document list of a collection", tags = { "Collections manager" })
    public ResponseEntity<?> updateCollection(@RequestHeader HttpHeaders headers, @RequestBody CollectionResponseDTO content) {
        if (
            Objects.isNull(headers) ||
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (Objects.isNull(content.getCollectionId()) || content.getCollectionId().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(content);
        }

        if (Objects.isNull(headers.get("lang"))) {
            headers.add("lang", "en");
        }

        if (headers.get("lang").isEmpty()) {
            headers.set("lang", "en");
        }

        StringBuilder url = new StringBuilder();
        url.append(APIContextualMemory).append(Constants.POST_CONTEXTUAL_MEMORY_UPDATE_URI);
        try {
            // restTemplate.put(url.toString(), content);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("collectionId", content.getCollectionId());
            requestBody.put("documentIdList", content.getDocumentIdList());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            HttpEntity<CollectionResponseDTO> response = restTemplate.postForEntity(url.toString(), request, CollectionResponseDTO.class);
            CollectionResponseDTO collectionResponse = response.getBody();

            if (collectionResponse == null) {
                System.out.println("Doc connectome Request Body");
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }
            return ResponseEntity
                .ok()
                .body(new ResponseEntity<CollectionResponseDTO>(collectionResponse, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(headers.get("lang"));
            System.out.println(headers.get("connectomeId"));
            System.out.println(headers.get("lang").get(0));
            System.out.println(headers.get("connectomeId").get(0));
            System.out.println(e.getMessage());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(false, "call of external api [" + Constants.POST_CONTEXTUAL_MEMORY_UPDATE_URI + "]" + e.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "delete a collection", tags = { "Collections manager" })
    public ResponseEntity<?> deleteCollection(@RequestHeader HttpHeaders headers) {
        if (
            Objects.isNull(headers) ||
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (
            Objects.isNull(headers.get("collectionId")) ||
            Objects.isNull(headers.get("collectionId").size() < 1) ||
            headers.get("collectionId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (Objects.isNull(headers.get("lang"))) {
            headers.add("lang", "en");
        }

        if (headers.get("lang").isEmpty()) {
            headers.set("lang", "en");
        }

        System.out.println(headers.get("lang"));
        System.out.println(headers.get("connectomeId"));
        System.out.println(headers.get("lang").get(0));
        System.out.println(headers.get("connectomeId").get(0));
        System.out.println(headers.get("collectionId"));
        System.out.println(headers.get("collectionId").get(0));
        System.out.println(headers.get("collectionId").get(0).toString());

        StringBuilder url = new StringBuilder();
        url.append(APIContextualMemory).append(Constants.DELETE_CONTEXTUAL_MEMORY_DELETE_URI);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url.toString()).toUriString();
        try {
            HttpEntity<String> request = new HttpEntity<String>(headers);
            HttpEntity<String> response = restTemplate.exchange(urlTemplate, HttpMethod.DELETE, request, String.class);

            String collectionResponse = response.getBody();

            return ResponseEntity.ok().body(new ResponseEntity<String>(collectionResponse, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(headers.get("lang"));
            System.out.println(headers.get("connectomeId"));
            System.out.println(headers.get("lang").get(0));
            System.out.println(headers.get("connectomeId").get(0));

            System.out.println(headers.get("collectionId"));
            System.out.println(headers.get("collectionId").get(0));
            System.out.println(e.getMessage());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(
                    false,
                    "call of external api [" +
                    Constants.DELETE_CONTEXTUAL_MEMORY_DELETE_URI +
                    "] collectionId=" +
                    headers.get("collectionId") +
                    e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/getRequestList")
    @Operation(summary = "get a collection with collectionId", tags = { "Collections manager" })
    public ResponseEntity<?> getRequestListByCollectionId(@RequestHeader HttpHeaders headers) {
        if (
            Objects.isNull(headers) ||
            Objects.isNull(headers.get("connectomeId")) ||
            headers.get("connectomeId").size() < 1 ||
            headers.get("connectomeId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        if (
            Objects.isNull(headers) ||
            Objects.isNull(headers.get("collectionId")) ||
            headers.get("collectionId").size() < 1 ||
            headers.get("collectionId").get(0).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(headers);
        }

        StringBuilder url = new StringBuilder();
        url.append(APIRecommendation).append(Constants.GET_RECOMMENDATION_GET_REQUESTLIST_URI);
        try {
            if (Objects.isNull(headers.get("lang"))) {
                headers.add("lang", "en");
            }

            if (headers.get("lang").isEmpty()) {
                headers.set("lang", "en");
            }

            HttpEntity<String> entity = new HttpEntity<String>(headers);
            HttpEntity<Object> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, Object.class);

            Object collectionsResponse = response.getBody();

            if (collectionsResponse == null) {
                System.out.println("get collection Request Body");
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Body response empty"), HttpStatus.OK);
            }

            return ResponseEntity.ok().body(new ResponseEntity<Object>(collectionsResponse, response.getHeaders(), HttpStatus.OK));
        } catch (Exception e) {
            System.out.println("Error");
            for (String iterable_element : headers.toSingleValueMap().values()) {
                System.out.println(iterable_element);
            }
            System.out.println(headers.get("lang"));
            System.out.println(headers.get("connectomeId"));
            System.out.println(headers.get("collectionId"));
            System.out.println(e.getMessage());
            return new ResponseEntity<ApiResponse>(
                new ApiResponse(
                    false,
                    "call of external api [" + Constants.GET_CONTEXTUAL_MEMORY_GET_COLLECTION_URI + "]" + e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
            );
        }
    }
    // #endregion
}
