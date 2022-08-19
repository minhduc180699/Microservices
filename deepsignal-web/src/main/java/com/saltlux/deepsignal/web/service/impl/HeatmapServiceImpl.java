package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.domain.Heatmap;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.HeatmapRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.HeatmapService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class HeatmapServiceImpl implements HeatmapService {

    private final HeatmapRepository heatmapRepository;

    private final UserRepository userRepository;

    public HeatmapServiceImpl(HeatmapRepository heatmapRepository, UserRepository userRepository) {
        this.heatmapRepository = heatmapRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Heatmap> findByUserId(String userId) {
        return null;
    }

    @Override
    public List<Heatmap> saveHeatmaps(List<Heatmap> heatmaps) {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (!user.isPresent()) {
            throw new BadRequestException("Save Failed! User not exist");
        } else {
            heatmaps.stream().forEach(heatmap -> heatmap.setUser(user.get()));
        }
        return heatmapRepository.saveAll(heatmaps);
    }

    @Override
    public List<Heatmap> findAll() {
        return heatmapRepository.findAll();
    }

    @Override
    public List<Heatmap> findAllHeatmaps() {
        return heatmapRepository.getAllHeatmaps();
    }

    @Override
    public Optional<Heatmap> findById(String id) {
        return heatmapRepository.findById(Long.valueOf(id));
    }

    @Override
    public Heatmap save(Heatmap heatmap) {
        return heatmapRepository.save(heatmap);
    }

    @Override
    public void remove(String s) {}

    @Override
    public Heatmap findByXAndY(int x, int y) {
        return heatmapRepository.findByXAndY(x, y);
    }

    @Override
    public int findMaxValue() {
        return heatmapRepository.findMaxValue();
    }
}
