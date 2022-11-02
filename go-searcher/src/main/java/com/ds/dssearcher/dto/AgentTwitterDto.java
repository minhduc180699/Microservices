package com.ds.dssearcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentTwitterDto {
    private Long agentid;
    private Integer agentType;
    private String consumerKey;
    private String consumerSecret;
    private String tokenKey;
    private String tokenSecret;
    private String lang;
   // private Long tweetsRecentTimes;
    private Long tweetsMaxResults;
    private String tweetsSinceId;
    private Integer tweetSize;
}
