package com.ploging.plog.domain;

import com.ploging.plog.domain.eums.RecruitStatus;
import com.ploging.plog.domain.utils.BaseTimeEntity;
import com.ploging.plog.domain.utils.StringListConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@RequiredArgsConstructor
public class Event extends BaseTimeEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
//    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "event_id", columnDefinition = "BINARY(16) DEFAULT UUID()")
    private UUID id; // 식별자 id

    @NotBlank
    private String title;

    @Convert(converter = StringListConverter.class)
    private List<String> images = new ArrayList<>();

    @NotBlank
    private String location;

    @Column
    private String description;

    @Column
    private String organizer;

    @Column
    private int dues;

    @Enumerated(EnumType.STRING)
    private RecruitStatus status;

    @Embedded
    private RecruitPeriod recruitPeriod;

    @Column
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime beginEvent; // 행사

    @Column
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime finishEvent;

}
