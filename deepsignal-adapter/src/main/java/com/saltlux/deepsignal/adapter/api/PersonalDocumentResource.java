package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.PersonalDocument;
import com.saltlux.deepsignal.adapter.repository.dsservice.PersonalDocumentRepository;
import com.saltlux.deepsignal.adapter.service.IPersonalDocumentService;
import com.saltlux.deepsignal.adapter.service.dto.FilterFeedDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-documents")
@Tag(name = "Personal Documents Management", description = "The Documents management API")
public class PersonalDocumentResource {

    private final IPersonalDocumentService iPersonalDocumentService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public PersonalDocumentResource(IPersonalDocumentService personalDocumentService) {
        this.iPersonalDocumentService = personalDocumentService;
    }

    @GetMapping("/{connectomeId}")
    @Operation(summary = "Get personal document by id", tags = { "Personal Document Management" })
    public ResponseEntity<?> getPersonalDocumentById(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam(value = "id", required = true) String personalDocumentId
    ) {
        PersonalDocument personalDocument = iPersonalDocumentService.findPersonalDocumentByConnectomeIdAndId(
            connectomeId,
            personalDocumentId
        );
        if (personalDocument != null) {
            return ResponseEntity.ok(personalDocument);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/getListDocuments/{connectomeId}")
    @Operation(summary = "Get list personal documents", tags = { "Personal Document Management" })
    public ResponseEntity<Map<String, Object>> getListPersonalDocuments(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "published_at") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "uploadType", defaultValue = "") String uploadType
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            Page<PersonalDocument> personalDocuments = iPersonalDocumentService.getPersonalDocumentByConnectomeIdAndType(
                connectomeId,
                uploadType,
                page,
                size,
                orderBy,
                sortDirection
            );

            Map<String, Object> response = new HashMap<>();
            response.put("results", personalDocuments.getContent());
            response.put("currentPage", personalDocuments.getNumber());
            response.put("totalItems", personalDocuments.getTotalElements());
            response.put("totalPages", personalDocuments.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getListDocuments/{connectomeId}")
    @Operation(summary = "Get feed by connectome Id", tags = { "Connectome Feed Management" })
    public ResponseEntity<Map<String, Object>> getFeedByConnectomeId(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "recommendDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "uploadType", defaultValue = "") String uploadType,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "entityLabel", required = false) String entityLabel,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            String keywordDecoded = null;
            if (!"".equals(keyword) && keyword != null) {
                keywordDecoded = URLDecoder.decode(keyword, StandardCharsets.UTF_8.toString());
            }
            String entityLabelDecoded = null;
            if (!"".equals(entityLabel) && entityLabel != null) {
                entityLabelDecoded = URLDecoder.decode(entityLabel, StandardCharsets.UTF_8.toString());
            }
            Page<PersonalDocument> personalDocuments = iPersonalDocumentService.findPersonalDocumentByConnectomeIdKeywordAndEntityLabelAndFilter(
                page,
                size,
                orderBy,
                sortDirection,
                connectomeId,
                uploadType,
                keywordDecoded,
                entityLabelDecoded,
                filterFeedDTOS
            );
            Map<String, Object> response = new HashMap<>();
            response.put("results", personalDocuments.getContent());
            response.put("currentPage", personalDocuments.getNumber());
            response.put("totalItems", personalDocuments.getTotalElements());
            response.put("totalPages", personalDocuments.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getListDeletedDocuments/{connectomeId}")
    @Operation(summary = "Get list personal documents that has been deleted", tags = { "Personal Document Management" })
    public ResponseEntity<Map<String, Object>> getListDeletedDocuments(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "createdDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            Page<PersonalDocument> personalDocuments = iPersonalDocumentService.getDeletedPersonalDocuments(
                connectomeId,
                page,
                size,
                orderBy,
                sortDirection
            );
            Map<String, Object> response = new HashMap<>();
            response.put("results", personalDocuments.getContent());
            response.put("currentPage", personalDocuments.getNumber());
            response.put("totalItems", personalDocuments.getTotalElements());
            response.put("totalPages", personalDocuments.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @DeleteMapping("deleteDocuments/{connectomeId}")
    @Operation(summary = "Delete personal documents", tags = { "Personal Document Management" })
    public ResponseEntity<?> deleteDocuments(@RequestBody List<String> docIds, @PathVariable("connectomeId") String connectomeId) {
        List<ObjectId> objectIds = new ArrayList<>();
        for (String docId : docIds) {
            objectIds.add(new ObjectId(docId));
        }
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").in(objectIds));
            Update update = new Update();
            update.set("isDelete", 1);
            try {
                mongoTemplate.updateMulti(query, update, PersonalDocument.class);
                return ResponseEntity.ok().body("Delete Success");
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getByIds")
    @Operation(summary = "Get connectome personal docs by feed ids", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> getByIds(
        @RequestBody List<String> ids,
        @RequestParam(value = "isDeleted", required = false) Boolean isDeleted
    ) {
        try {
            List<PersonalDocument> docs = iPersonalDocumentService.findPersonalDocumentsByIds(ids, isDeleted);
            Map<String, Object> response = new HashMap<>();
            response.put("connectomePersonalDocuments", docs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getByDocIds")
    @Operation(summary = "Get connectome personal docs by feed ids", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> getByDocIds(
        @RequestBody List<String> docIds,
        @RequestParam(value = "isDeleted", required = false) Boolean isDeleted
    ) {
        try {
            List<PersonalDocument> docs = iPersonalDocumentService.findPersonalDocumentsByDocIds(docIds, isDeleted);
            Map<String, Object> response = new HashMap<>();
            response.put("connectomePersonalDocuments", docs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //
    //    @GetMapping("/handleActivity")
    //    @Operation(summary = "Handle activity: like, bookmark, delete", tags = { "Personal Document Management" })
    //    public ResponseEntity<?> handleBookmark(
    //        @RequestParam("id") ObjectId id,
    //        @RequestParam("state") boolean state,
    //        @RequestParam("activity") Constants.activity activity,
    //        @RequestParam("likeState") int likeState
    //    ) {
    //        return ResponseEntity.ok().body(iPersonalDocumentService.handleActivity(id, activity, state, likeState));
    //
    //    }
}
