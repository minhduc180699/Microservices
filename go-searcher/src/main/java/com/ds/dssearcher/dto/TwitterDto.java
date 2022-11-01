package com.ds.dssearcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TwitterDto {

    private Long agentid;
    private Integer agentType;
    private String screenNames;
    private Integer isCollectProfile;
    private Integer isCollectTweet;
    private Integer isCollectFollowing;
    private Integer isCollectFollower;
    private String consumerKey;
    private String consumerSecret;
    private String tokenKey;
    private String tokenSecret;
    private String lang;
    private Integer tweetSize;
    private Integer requestId;
}
