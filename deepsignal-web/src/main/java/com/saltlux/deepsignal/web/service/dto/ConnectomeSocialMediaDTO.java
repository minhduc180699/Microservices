package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.SocialNetwork;
import java.time.Instant;
import java.util.Set;
import javax.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectomeSocialMediaDTO {

    private Long id;

    @Size(max = 50)
    private String loginUsername;

    @Size(max = 50)
    private String loginPassword;

    @Size(max = 512)
    private String userToken;

    private String loginCookies;

    private Instant registeredAt = Instant.now();

    private Instant lastModifiedAt = Instant.now();

    private int status;

    private int socialAccountState;

    private int userTokenState;

    private String socialAccountStateDescription;

    private String userTokenStateDescription;

    @Size(max = 100)
    private String uuid;

    private int cookiesState;

    private String cookiesStateDescription;

    private SocialNetwork socialNetwork;

    private UserDTO user;

    private Set<Connectome> connectomes;
}
