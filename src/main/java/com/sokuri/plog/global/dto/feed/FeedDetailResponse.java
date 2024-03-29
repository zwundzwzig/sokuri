package com.sokuri.plog.global.dto.feed;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FeedDetailResponse {
  private String nickname;
  private String avatar;
  private String description;
  private List<String> hashtags;
  private int viewCount;
  private Integer likeCount;
  private List<String> images;
  private LocalDateTime createdAt;
  private String timeSinceUpload;
}
