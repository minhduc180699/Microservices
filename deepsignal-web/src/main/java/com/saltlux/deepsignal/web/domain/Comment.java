package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saltlux.deepsignal.web.service.dto.UserDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Comment implements Serializable {

    private static final long serialVersionUID = 12345678L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    private String content;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Instant createdDate = Instant.now();

    @NotNull
    private String feedId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_comment_id")
    private Comment comment;

    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    @OrderBy("createdDate ASC")
    private Set<Comment> children = new HashSet<>();

    private transient UserDTO userDTO = new UserDTO();
    private transient Long parentCommentId;

    @PostLoad
    public void onLoad() {
        if (Objects.nonNull(this.user)) {
            this.userDTO = new UserDTO(this.user);
        }
    }
}
