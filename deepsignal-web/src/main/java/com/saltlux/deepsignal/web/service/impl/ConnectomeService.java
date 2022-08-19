package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.exception.AccountAlreadyNotExistException;
import com.saltlux.deepsignal.web.exception.AccountTemporaryException;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.service.IConnectomeService;
import com.saltlux.deepsignal.web.service.dto.ConnectomeDTO;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConnectomeService implements IConnectomeService {

    private final ConnectomeRepository connectomeRepository;

    private final ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public ConnectomeService(ConnectomeRepository connectomeRepository, ModelMapper modelMapper) {
        this.connectomeRepository = connectomeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Connectome> findAll() {
        return connectomeRepository.findAll();
    }

    @Override
    public Optional<Connectome> findById(String id) {
        return connectomeRepository.findById(id);
    }

    @Override
    public Connectome save(Connectome connectome) {
        return connectomeRepository.save(connectome);
    }

    @Override
    public Connectome saveConnectome(ConnectomeDTO connectomeDTO) {
        Connectome connectome = modelMapper.map(connectomeDTO, Connectome.class);
        return connectomeRepository.save(connectome);
    }

    @Override
    public ConnectomeDTO findByConnectomeId(String id) {
        Optional<Connectome> connectome = connectomeRepository.findById(id);
        return connectome.map(value -> modelMapper.map(value, ConnectomeDTO.class)).orElse(null);
    }

    @Override
    public List<Connectome> findByUserLogin(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);
        if (!user.isPresent()) {
            throw new AccountAlreadyNotExistException();
        }
        if (user.isPresent() && user.get().getActivated().equals(3)) {
            throw new AccountTemporaryException();
        }
        return connectomeRepository.findByUser_Id(user.get().getId());
    }

    @Override
    public void remove(String id) {
        connectomeRepository.deleteById(id);
    }
}
