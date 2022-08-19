package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saltlux.deepsignal.web.config.Constants;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "interaction")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Interaction implements Serializable {

    private static final long serialVersionUID = 12345678L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    private int type;

    private String name;

    @OneToMany(mappedBy = "interaction", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "interaction-user")
    private Set<InteractionUser> interactionUsers = new HashSet<>();

    public Interaction(int type) {
        this.type = type;
        this.name = this.getNameByType(type);
    }

    private String getNameByType(int type) {
        if (type == 0) {
            return null;
        }
        Constants.TypeInteraction[] typeInteractions = Constants.TypeInteraction.values();
        Stream<Constants.TypeInteraction> stream = Stream.of(typeInteractions);
        Optional<Constants.TypeInteraction> optional = stream.filter(item -> item.type == type).findFirst();
        return optional.map(Enum::name).orElse(null);
    }
}
