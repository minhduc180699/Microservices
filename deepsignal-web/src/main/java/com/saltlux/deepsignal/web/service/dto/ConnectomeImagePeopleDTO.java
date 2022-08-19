package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectomeImagePeopleDTO {
    private String title;
    private List<String> titles;
}
