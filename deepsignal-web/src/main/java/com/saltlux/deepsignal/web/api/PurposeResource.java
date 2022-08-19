package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.domain.Purpose;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.service.IPurposeService;
import com.saltlux.deepsignal.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purpose")
@Tag(name = "Purpose Management", description = "The Purpose management API")
public class PurposeResource {

    @Autowired
    private IPurposeService iPurposeService;

    @GetMapping("/getAll")
    @Operation(summary = "get all purpose information", tags = { "Purpose Management" })
    public List<Purpose> getAll() {
        return iPurposeService.getAll();
    }
}
