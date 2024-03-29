package com.sokuri.plog.domain.relations.user;

import com.sokuri.plog.domain.entity.Feed;
import com.sokuri.plog.domain.entity.User;
import com.sokuri.plog.domain.converter.StringToUuidConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "likes")
@Data
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(generator = "like")
    @GenericGenerator(name = "like", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "like_id", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
    @Convert(converter = StringToUuidConverter.class)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;
}
