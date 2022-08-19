package com.saltlux.deepsignal.adapter.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "code_language")
public class CodeLanguage implements Serializable {

    @Id
    @Column(name = "language", nullable = false, length = 20)
    private String language;

    @Column(name = "description", length = 512)
    private String description;
}
