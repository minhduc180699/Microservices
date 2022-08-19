package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.domain.UserSetting;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.service.IUserSettingService;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usersetting")
@Tag(name = "UserSetting Management", description = "The UserSetting management API")
public class UserSettingResource {

    private final IUserSettingService userSettingService;

    public UserSettingResource(IUserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<?> getByUserId(@RequestParam("connectomeId") String connectomeId) {
        if (StringUtils.isEmpty(connectomeId)) {
            return ResponseEntity.badRequest().body("connectomeId is null");
        }
        return ResponseEntity.ok().body(userSettingService.getUserSettingByUserId(connectomeId));
    }

    @PostMapping("/saveSetting")
    public ResponseEntity<?> saveSetting(@RequestBody UserSetting userSetting, @RequestParam("connectomeId") String connectomeId) {
        try {
            userSettingService.saveUserSetting(userSetting, connectomeId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Save successfully!");
    }
}
