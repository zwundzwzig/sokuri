package com.sokuri.plog.global.dto.feed;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FeedSummaryResponse {
  private UUID id;
  private String nickname;
  private String avatar;
  private List<String> images;
  private int viewCount;
  private Integer likeCount;
  private LocalDate createdAt;
  private String timeSinceUpload;
}
