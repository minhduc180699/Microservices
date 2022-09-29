package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.ExternalUrl;
import com.saltlux.deepsignal.web.repository.ExternalUrlRepository;
import com.saltlux.deepsignal.web.service.IExternalUrlService;
import com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ExternalUrlServiceImpl implements IExternalUrlService {

    private final ExternalUrlRepository externalUrlRepository;

    public ExternalUrlServiceImpl(ExternalUrlRepository externalUrlRepository) {
        this.externalUrlRepository = externalUrlRepository;
    }

    @Override
    public List<ExternalUrl> findAll() {
        return null;
    }

    @Override
    public Optional<ExternalUrl> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public ExternalUrl save(ExternalUrl externalUrlTracking) {
        return externalUrlRepository.save(externalUrlTracking);
    }

    @Override
    public void remove(Long aLong) {}

    @Override
    public int countByOriginalUrl(String originalUrl) {
        return externalUrlRepository.countByUrl(originalUrl);
    }

    @Override
    public List<UrlTrackingDTO> countMostClickedUrl(int limit) {
        List<UrlTrackingDTO> urlTrackingDTOS = externalUrlRepository.countMostClickedUrl(PageRequest.of(0, limit));
        return urlTrackingDTOS;
    }

    @Override
    public Optional<ExternalUrl> findByOriginalUrl(String originalUrl) {
        return externalUrlRepository.findByOriginalUrl(originalUrl);
    }

    @Override
    public Optional<ExternalUrl> findByShortUrl(String shortUrl) {
        return externalUrlRepository.findByShortUrl(shortUrl);
    }

    @Override
    public String createShortUrl(String originalUrl) {
        String shortUrl = "";
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 5; i++) {
            shortUrl += validCharacters.charAt((int) Math.floor(Math.random() * validCharacters.length()));
        }
        return shortUrl;
    }
}
