package com.ds.dssearcher.dto;

import com.ds.dssearcher.dto.AgentTwitterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentTwitterSearchDto extends AgentTwitterDto {
    private String keyword;
    private String bearerToken;
}
