package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Heatmap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HeatmapRepository extends JpaRepository<Heatmap, Long> {
    //    List<Heatmap> findByUser_Id(String userId);

    @Query(value = "SELECT * FROM heatmap h GROUP BY x,y", nativeQuery = true)
    List<Heatmap> getAllHeatmaps();

    Heatmap findByXAndY(int x, int y);

    @Query(value = "SELECT MAX(value ) FROM heatmap", nativeQuery = true)
    int findMaxValue();
}
