package com.sokuri.plog.domain.relations.image;

import com.sokuri.plog.domain.Community;
import com.sokuri.plog.domain.Image;
import com.sokuri.plog.domain.converter.StringToUuidConverter;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "community_images")
@Getter
public class CommunityImage {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name="uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
  @Convert(converter = StringToUuidConverter.class)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "community_id")
  @NotNull
  private Community community;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "image_id")
  @NotNull
  private Image image;
}