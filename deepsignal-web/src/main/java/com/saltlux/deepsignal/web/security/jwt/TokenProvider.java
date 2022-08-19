package com.saltlux.deepsignal.web.security.jwt;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import tech.jhipster.config.JHipsterProperties;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private static final String REFRESH_COUNT = "refreshCount";

    private static final String REFRESH_LIMIT = "refreshLimit";

    private final Key key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;

    private final Integer refreshLimit;

    private final String issuer;

    private final String audience;

    private int refreshCount;

    @Autowired
    ApplicationContext applicationContext;

    public TokenProvider(JHipsterProperties jHipsterProperties, ApplicationProperties applicationProperties) {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
        if (!ObjectUtils.isEmpty(secret)) {
            log.warn(
                "Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
        this.refreshLimit = applicationProperties.getSecurity().getJwt().getTokenRefreshLimit();
        this.issuer = applicationProperties.getSecurity().getJwt().getTokenIssuer();
        this.audience = applicationProperties.getSecurity().getJwt().getTokenAudience();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        this.refreshCount = 0;
        return this.generateToken(authentication, rememberMe);
    }

    public String refreshToken(String jwt, boolean isCheckResetTk) {
        Authentication authentication = this.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Claims claims = jwtParser.parseClaimsJws(jwt).getBody();
        refreshCount = extractRefreshCountFromClaims(claims) + 1;
        return this.generateToken(authentication, isCheckResetTk);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .filter(auth -> !auth.trim().isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    /**
     * Generate a Token
     * @param authentication
     * @param rememberMe
     * @return
     */
    private String generateToken(Authentication authentication, boolean rememberMe) {
        if (!rememberMe && !this.isEligibleForRefreshment()) {
            log.error("This token cannot be refreshed");
            return null;
        }

        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        String subject = authentication.getName();
        long now = (new Date()).getTime();

        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        String id = this.generateTokenIdentifier();
        return Jwts
            .builder()
            .setId(id)
            .setIssuer(issuer)
            .setAudience(audience)
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setSubject(subject)
            .claim(AUTHORITIES_KEY, authorities)
            .claim(REFRESH_COUNT, refreshCount)
            .claim(REFRESH_LIMIT, refreshLimit)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }

    /**
     * Extract the refresh count from the token claims.
     *
     * @param claims
     * @return Refresh count from the JWT token
     */
    private int extractRefreshCountFromClaims(@NotNull Claims claims) {
        return (int) claims.get(REFRESH_COUNT);
    }

    public boolean isEligibleForRefreshment() {
        return refreshCount < refreshLimit;
    }
}
