package com.ploging.plog.domain;

import com.ploging.plog.domain.eums.RecruitStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "communities")
@Getter
@RequiredArgsConstructor
@Builder
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "community_id")
    private UUID id; // 식별자 id

    @NotBlank
    private String title;

    @Column
    private String image;

    @NotBlank
    private String location;

    @Column
    private String description;

    @Column
    private String owner;

    @Column
    private RecruitStatus status;

    @Column
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime created; // 생성

    @Column
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime modified;

}