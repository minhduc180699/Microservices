package com.saltlux.deepsignal.adapter.service.impl;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.Company;
import com.saltlux.deepsignal.adapter.domain.Feed;
import com.saltlux.deepsignal.adapter.domain.People;
import com.saltlux.deepsignal.adapter.domain.PeopleDTO.CompanyDTO;
import com.saltlux.deepsignal.adapter.repository.dsservice.PeopleRepository;
import com.saltlux.deepsignal.adapter.service.IPeopleService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class PeopleServiceImpl implements IPeopleService {

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<People> findAll(int page, int size, String orderBy, String sortDirection) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );
        return peopleRepository.findAll(pageable);
    }

    @Override
    public Page<People> findByConnectomId(Pageable pageable, String connectomeId) {
        //        Pageable pageable = PageRequest.of(
        //            page,
        //            size,
        //            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
        //            orderBy
        //        );
        return peopleRepository.findByConnectomeId(pageable, connectomeId);
    }

    @Override
    public Optional<People> findOneByConnectomeId(String connectomeId) {
        return peopleRepository.findByConnectomeId(connectomeId);
    }

    @Override
    public People getByConnectomeIdAndLang(String connectomeId, String lang) {
        Optional<People> op = peopleRepository.findByConnectomeIdAndLang(connectomeId, lang);
        if (!op.isPresent()) {
            return null;
        }
        People people = op.get();
        List<CompanyDTO> peoples = people.getPeople().stream().filter(item -> item.getDeleted() != true).collect(Collectors.toList());
        List<CompanyDTO> companies = people.getCompany().stream().filter(item -> item.getDeleted() != true).collect(Collectors.toList());
        people.setPeople(peoples);
        people.setCompany(companies);
        return people;
    }

    @Override
    public Optional<People> findById(String id) {
        return peopleRepository.findById(new ObjectId(id));
    }

    @Override
    public boolean hiddenPeople(String connectomeId, String title, String type, String lang, Boolean deleted) {
        Optional<People> optionalPeople = peopleRepository.findByConnectomeIdAndLang(connectomeId, lang);
        if (optionalPeople.isPresent()) {
            String typeQuery = type.toLowerCase();
            Query query = new Query(
                new Criteria().andOperator(Criteria.where("connectome_id").is(connectomeId).and(typeQuery + ".title").is(title))
            );
            Update update = new Update();
            update.set(typeQuery + ".$.deleted", deleted);
            mongoTemplate.updateMulti(query, update, People.class);
            return true;
        }
        return false;
    }
}
