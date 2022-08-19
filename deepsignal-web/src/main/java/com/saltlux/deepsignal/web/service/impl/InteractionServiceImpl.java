package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.Interaction;
import com.saltlux.deepsignal.web.repository.InteractionRepository;
import com.saltlux.deepsignal.web.service.IInteractionService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteractionServiceImpl implements IInteractionService {

    private final InteractionRepository interactionRepository;

    public InteractionServiceImpl(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Transactional
    @Override
    public Interaction saveByType(int type) {
        return interactionRepository.save(new Interaction(type));
    }

    @Override
    public Optional<Interaction> findByType(int type) {
        return interactionRepository.findByType(type);
    }

    @Override
    public Interaction saveIfNoneExist(int type) {
        Optional<Interaction> interaction = findByType(type);
        return interaction.orElseGet(() -> interactionRepository.save(new Interaction(type)));
    }

    @Override
    public List<Interaction> findAll() {
        return (List<Interaction>) interactionRepository.findAll();
    }
}
