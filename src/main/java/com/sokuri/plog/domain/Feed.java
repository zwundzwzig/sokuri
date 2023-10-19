package com.sokuri.plog.domain;

import com.sokuri.plog.domain.dto.FeedsResponse;
import com.sokuri.plog.domain.relations.hashtag.FeedHashtag;
import com.sokuri.plog.domain.relations.image.FeedImage;
import com.sokuri.plog.domain.utils.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "feeds")
@Getter
@NoArgsConstructor
public class Feed extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "feed_id", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "image", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FeedImage> images;

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FeedHashtag> hashtags = new HashSet<>();

    public FeedsResponse toResponse() {
        return FeedsResponse.builder()
                .userId(user.getNickname())
                .createdAt(LocalDate.from(getCreateDate()))
                .thumbnail(images.toString())
                .build();
    }
}
