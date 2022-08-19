package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.domain.Connectome;
import com.saltlux.deepsignal.adapter.domain.Feed;
import com.saltlux.deepsignal.adapter.domain.PersonalDocument;
import com.saltlux.deepsignal.adapter.service.IConnectomeFeedService;
import com.saltlux.deepsignal.adapter.service.IConnectomeService;
import com.saltlux.deepsignal.adapter.service.IPersonalDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/connectome")
@Tag(name = "Connectome Management", description = "The connectome management API")
public class ConnectomeResource {

    @Autowired
    private IConnectomeService iConnectomeService;

    @Autowired
    private IConnectomeFeedService iConnectomeFeedService;

    @Autowired
    private IPersonalDocumentService iPersonalDocumentService;

    @GetMapping("/getAll")
    @Operation(summary = "Get all connectome", tags = { "Connectome Management" })
    public ResponseEntity<Map<String, Object>> getAllConnectome(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            Page<Connectome> connectomePage = iConnectomeService.findAll(page, size, orderBy, sortDirection);
            Map<String, Object> response = new HashMap<>();
            response.put("connectomes", connectomePage.getContent());
            response.put("currentPage", connectomePage.getNumber());
            response.put("totalItems", connectomePage.getTotalElements());
            response.put("totalPages", connectomePage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{connectomeId}")
    @Operation(summary = "Get all connectome by connectome id and language", tags = { "Connectome Management" })
    public ResponseEntity<Map<String, Object>> getConnetomeByConnectomeId(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "lang", defaultValue = "en") String lang
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (Objects.isNull(lang)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            List<Connectome> res = iConnectomeService.findByConnectomeIdAndLang(connectomeId, lang);
            Map<String, Object> response = new HashMap<>();
            response.put("connectomes", res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{connectomeId}/vertices")
    @Operation(summary = "Get all vertex by connectome id, language and vertex label", tags = { "Connectome Management" })
    public ResponseEntity<List<com.saltlux.deepsignal.adapter.domain.Vertex>> getVerticesByConnectomeIdAndLangAndLabel(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "lang", defaultValue = "en") String lang,
        @RequestParam(value = "label") String vertexLabel
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (Objects.isNull(lang)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (Objects.isNull(vertexLabel)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            List<Connectome> res = iConnectomeService.findByConnectomeIdAndLang(connectomeId, lang);
            ArrayList<com.saltlux.deepsignal.adapter.domain.Vertex> resVertices = new ArrayList<>();
            for (Connectome connectome : res) {
                if (connectome.getVertices().size() > 0) {
                    for (com.saltlux.deepsignal.adapter.domain.Vertex vertex : connectome.getVertices()) {
                        if (vertex.getLabel().equals(vertexLabel)) {
                            resVertices.add(vertex);
                            break;
                        }
                    }
                }
            }

            return new ResponseEntity<>(resVertices, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{connectomeId}/vertices/docs")
    @Operation(summary = "Get all vertex by connectome id, language and vertex label", tags = { "Connectome Management" })
    public ResponseEntity<Map<String, Object>> getConnetomeById(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "lang", defaultValue = "en") String lang,
        @RequestParam(value = "label") String vertexLabel
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (Objects.isNull(lang)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (Objects.isNull(vertexLabel)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            ResponseEntity<List<com.saltlux.deepsignal.adapter.domain.Vertex>> verticesResponse = getVerticesByConnectomeIdAndLangAndLabel(
                connectomeId,
                lang,
                vertexLabel
            );
            if (verticesResponse.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<com.saltlux.deepsignal.adapter.domain.Vertex> vertices = verticesResponse.getBody();
            ArrayList<Feed> feeds = new ArrayList<>();
            ArrayList<PersonalDocument> personalDocuments = new ArrayList<>();
            for (com.saltlux.deepsignal.adapter.domain.Vertex vertex : vertices) {
                if (vertex.getFeedIds().size() > 0) {
                    feeds.addAll(iConnectomeFeedService.findFeedByIds(vertex.getFeedIds()));
                }
                if (vertex.getDocumentIds().size() > 0) {
                    personalDocuments.addAll(iPersonalDocumentService.findPersonalDocumentsByIds(vertex.getDocumentIds()));
                }
            }

            System.out.println("docs:" + personalDocuments.size());
            System.out.println("feeds:" + feeds.size());

            Map<String, Object> response = new HashMap<>();
            response.put("personalDocuments", personalDocuments);
            response.put("feeds", feeds);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
