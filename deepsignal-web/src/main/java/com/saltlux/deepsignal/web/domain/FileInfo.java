package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A File upload
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_upload")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FileInfo extends AbstractBaseEntity implements Serializable {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "size")
    private long size;

    @Column(name = "path")
    private String path;

    @Column(name = "mine_type")
    private String mineType;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "chrome_type")
    private Integer chromeType;

    private transient String author;

    private transient String originDate;

    private transient Instant publishedDate;

    private transient String fileType;

    private transient String searchType;

    private transient String favicon;

    private transient String img;

    private transient String lang;

    private transient String keyword;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "connectome_file_upload_detail",
        joinColumns = { @JoinColumn(name = "file_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "connectome_id", referencedColumnName = "connectome_id") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Connectome> connectomes = new HashSet<>();

    public Connectome getConnectome(String connectomeId) {
        Connectome connectome = connectomes.stream().filter(item -> connectomeId.equals(item.getConnectomeId())).findAny().orElse(null);
        return connectome;
    }
}
