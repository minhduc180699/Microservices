package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.ConnectomeSocialMedia;
import com.saltlux.deepsignal.web.repository.ConnectomeSocialMediaRepository;
import com.saltlux.deepsignal.web.service.IConnectomeSocialMediaService;
import com.saltlux.deepsignal.web.service.dto.ConnectomeSocialMediaDTO;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing SNS account.
 */
@Service
@Transactional
public class ConnectomeSocialMediaService implements IConnectomeSocialMediaService {

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final ConnectomeSocialMediaRepository connectomeSocialMediaRepository;

    public ConnectomeSocialMediaService(
        ModelMapper modelMapper,
        ConnectomeSocialMediaRepository connectomeSocialMediaRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.modelMapper = modelMapper;
        this.connectomeSocialMediaRepository = connectomeSocialMediaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ConnectomeSocialMedia> findAll() {
        return connectomeSocialMediaRepository.findAll();
    }

    @Override
    public Optional<ConnectomeSocialMedia> findById(Long id) {
        return connectomeSocialMediaRepository.findById(id);
    }

    @Override
    public ConnectomeSocialMedia save(ConnectomeSocialMedia connectomeSocialMedia) {
        return connectomeSocialMediaRepository.save(connectomeSocialMedia);
    }

    @Override
    public void remove(Long id) {
        connectomeSocialMediaRepository.deleteById(id);
    }

    @Override
    public ConnectomeSocialMedia createConnectomeSocialMedial(ConnectomeSocialMediaDTO socialMediaDTO) {
        String encryptedPassword = passwordEncoder.encode(socialMediaDTO.getLoginPassword());
        ConnectomeSocialMedia connectomeSocialMedia = modelMapper.map(socialMediaDTO, ConnectomeSocialMedia.class);
        connectomeSocialMedia.setLoginPassword(encryptedPassword);
        ConnectomeSocialMedia result = connectomeSocialMediaRepository.save(connectomeSocialMedia);
        return result;
    }
}
