package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.domain.User;
import java.util.List;
import lombok.Data;

@Data
public class UserManagementDTO {

    private List<AdminUserDTO> UserManagement;
    private int count;
}
