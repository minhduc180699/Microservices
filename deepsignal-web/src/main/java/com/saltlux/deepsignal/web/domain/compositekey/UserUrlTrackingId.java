package com.saltlux.deepsignal.web.domain.compositekey;

import java.io.Serializable;
import java.util.Objects;

public class UserUrlTrackingId implements Serializable {

    private String userId;

    private Long externalUrlId;

    public UserUrlTrackingId() {}

    public UserUrlTrackingId(String userId, Long externalUrlId) {
        this.userId = userId;
        this.externalUrlId = externalUrlId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getExternalUrlId() {
        return externalUrlId;
    }

    public void setExternalUrlId(Long externalUrlId) {
        this.externalUrlId = externalUrlId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserUrlTrackingId that = (UserUrlTrackingId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(externalUrlId, that.externalUrlId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, externalUrlId);
    }
}
