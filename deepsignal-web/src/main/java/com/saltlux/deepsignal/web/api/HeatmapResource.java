package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.domain.Heatmap;
import com.saltlux.deepsignal.web.repository.HeatmapRepository;
import com.saltlux.deepsignal.web.service.HeatmapService;
import com.saltlux.deepsignal.web.service.dto.HeatmapDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/heatmap")
@Tag(name = "Heatmap Management", description = "Heatmap Management API")
public class HeatmapResource {

    private final HeatmapService heatmapService;

    private final HeatmapRepository heatmapRepository;

    private final ModelMapper modelMapper;

    public HeatmapResource(HeatmapService heatmapService, ModelMapper modelMapper, HeatmapRepository heatmapRepository) {
        this.heatmapService = heatmapService;
        this.modelMapper = modelMapper;
        this.heatmapRepository = heatmapRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllHeatmaps() {
        try {
            List<Heatmap> heatmaps = heatmapService.findAll();
            JSONObject heatmapsJSON = new JSONObject();
            heatmapsJSON.put("heatmaps", heatmaps);
            heatmapsJSON.put("maxValue", heatmapService.findMaxValue());
            return ResponseEntity.ok().body(heatmapsJSON);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("{connectomeId}")
    public ResponseEntity<?> getAllHeatmapsByConnectomeId(@PathVariable("connectomeId") String connectomeId) {
        try {
            List<Heatmap> heatmaps = heatmapService.findAllHeatmaps();
            return ResponseEntity.ok().body(heatmaps);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //    @GetMapping("{connectomeId}/userId")
    //    public ResponseEntity<?> getAllHeatmapsByUserId( @PathVariable("connectomeId") String connectomeId) {
    //        try {
    //            List<Heatmap> heatmaps = heatmapRepository.findByUser_Id("UID_1379e745-9ca3-4687-b5fa-db6c32b19e10");
    //            return ResponseEntity.ok().body(heatmaps);
    //        } catch (Exception ex) {
    //            return ResponseEntity.badRequest().body(null);
    //        }
    //    }

    @PostMapping("")
    public ResponseEntity<?> saveHeatmaps(@RequestBody List<HeatmapDTO> heatmapDTOs) {
        try {
            List<Heatmap> heatmaps = heatmapDTOs.stream().map(item -> modelMapper.map(item, Heatmap.class)).collect(Collectors.toList());

            for (int i = 0; i < heatmaps.size(); i++) {
                Heatmap heatmap = heatmapService.findByXAndY(heatmaps.get(i).getX(), heatmaps.get(i).getY());
                if (heatmap != null) {
                    heatmap.setValue(heatmap.getValue() + heatmaps.get(i).getValue());
                    heatmapService.save(heatmap);
                    heatmaps.remove(i);
                    i--;
                }
            }

            if (heatmaps.size() > 0) {
                return ResponseEntity.ok().body(heatmapService.saveHeatmaps(heatmaps));
            }
            return ResponseEntity.ok().body("");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
