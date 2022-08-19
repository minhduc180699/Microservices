package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.Heatmap;
import java.util.List;

public interface HeatmapService extends GeneralService<Heatmap, String> {
    List<Heatmap> findByUserId(String userId);

    List<Heatmap> saveHeatmaps(List<Heatmap> heatmaps);

    List<Heatmap> findAllHeatmaps();

    Heatmap findByXAndY(int x, int y);

    int findMaxValue();
}
