package com.saltlux.deepsignal.web.domain.compositekey;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InteractionUserCompositeKey implements Serializable {

    private Long interaction;
    private String user;
    private String feedId;
    private Instant createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionUserCompositeKey that = (InteractionUserCompositeKey) o;
        return (
            Objects.equals(interaction, that.interaction) &&
            Objects.equals(user, that.user) &&
            Objects.equals(feedId, that.feedId) &&
            Objects.equals(createdDate, that.createdDate)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(interaction, user, feedId, createdDate);
    }
}
