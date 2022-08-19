package com.saltlux.deepsignal.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11)", updatable = false, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @Column(name = "weather")
    private String weather;

    @Column(name = "stock")
    private String stock;

    @Column(name = "loc_weather")
    private String locWeather;
}
