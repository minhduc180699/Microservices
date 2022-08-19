package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.domain.Feed;
import com.saltlux.deepsignal.adapter.domain.People;
import com.saltlux.deepsignal.adapter.domain.PeopleDTO.CompanyDTO;
import com.saltlux.deepsignal.adapter.domain.PeopleDTO.DocumentDTO;
import com.saltlux.deepsignal.adapter.service.IPeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peoples")
@Tag(name = "Peoples Management", description = "The peoples management API")
public class PeopleResource {

    private IPeopleService iPeopleService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public PeopleResource(IPeopleService iPeopleService) {
        this.iPeopleService = iPeopleService;
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all peoples", tags = { "Peoples Management" })
    public ResponseEntity<Map<String, Object>> getAllPeoples(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            Page<People> peoples = iPeopleService.findAll(page, size, orderBy, sortDirection);
            Map<String, Object> response = new HashMap<>();
            response.put("results", peoples.getContent());
            response.put("currentPage", peoples.getNumber());
            response.put("totalItems", peoples.getTotalElements());
            response.put("totalPages", peoples.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get people by Id", tags = { "Peoples Management" })
    public ResponseEntity<People> getPeopleById(@PathVariable(value = "id") String id) {
        if (Objects.isNull(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            return new ResponseEntity<>(!iPeopleService.findById(id).isPresent() ? null : iPeopleService.findById(id).get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hidden/{connectomeId}")
    @Operation(summary = "Hidden people by Id", tags = { "Peoples Management" })
    public void hiddenPeopleById(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam String title,
        @RequestParam String type,
        @RequestParam String lang,
        @RequestParam boolean deleted
    ) {
        iPeopleService.hiddenPeople(connectomeId, title, type, lang, deleted);
    }

    @GetMapping("/find/{connectomeId}")
    @Operation(summary = "Get people by connectome Id", tags = { "Peoples Management" })
    public ResponseEntity<?> findByConnectomeIdAndLang(
        Pageable pageable,
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("isGetStock") boolean isGetStock,
        @RequestParam("lang") String lang
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            //            Optional<People> peopleOptional = iPeopleService.findOneByConnectomeId(connectomeId);
            //            if (!peopleOptional.isPresent()) {
            //                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            //            }
            People people = iPeopleService.getByConnectomeIdAndLang(connectomeId, lang);
            Map<String, Object> response = new HashMap<>();
            response.put("results", people);
            response.put("totalItems", people.getCompany().size() + people.getPeople().size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
