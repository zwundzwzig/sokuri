package com.sokuri.plog.service;

import com.sokuri.plog.domain.entity.Feed;
import com.sokuri.plog.global.dto.SimpleDataResponse;
import com.sokuri.plog.global.dto.feed.CreateFeedRequest;
import com.sokuri.plog.global.dto.feed.FeedDetailResponse;
import com.sokuri.plog.global.dto.feed.FeedSummaryResponse;
import com.sokuri.plog.domain.eums.AccessStatus;
import com.sokuri.plog.domain.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
  private final FeedRepository feedRepository;
  private final UserService userService;
  private final ImageService imageService;
  private final HashtagService hashtagService;

  private void isValidUUID(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("유효하지 않은 UUID입니다: " + id);
    }
  }

  @Transactional(readOnly = true)
  public Feed findById(String id) {
    return feedRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NoResultException("해당 ID 값을 가진 피드는 존재하지 않아요."));
  }

  @Transactional(readOnly = true)
  public List<FeedSummaryResponse> getFeedList(AccessStatus status, int page, int limit) {
    Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate").descending());
    Page<Feed> feedPage = feedRepository.findAllByStatusIs(status, pageable);

    return feedPage
            .stream()
            .map(Feed::toSummaryResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<FeedSummaryResponse> getAllFeedList(int page, int limit) {
    PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("createdDate").descending());
    Page<Feed> feedPage = feedRepository.findAll(pageRequest);

    return feedPage.getContent()
            .stream()
            .map(Feed::toSummaryResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public FeedDetailResponse getFeedDetail(String id) {
    isValidUUID(id);
    Feed feed = feedRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NoResultException("해당 ID 값을 가진 피드는 존재하지 않아요."));

    return feed.toDetailResponse();
  }

  @Transactional
  public SimpleDataResponse create(CreateFeedRequest request) {
    Feed feed = request.toEntity();
    feed.setUser(userService.findById(request.getUser()));

    Feed response = feedRepository.save(feed);

    Optional.ofNullable(request.getImages())
            .ifPresent(files -> imageService.saveAllFeedImage(files, response));
    Optional.ofNullable(request.getHashtags())
            .ifPresent(hashtag -> hashtagService.saveAllFeedHashtag(hashtag, response));

    return new SimpleDataResponse(response.getId().toString());
  }

  @Transactional
  public SimpleDataResponse update(CreateFeedRequest request, String id) {
    Feed targetFeed = findById(id);

    Optional.ofNullable(request.getDescription()).ifPresent(targetFeed::setDescription);
    Optional.ofNullable(request.getHashtags())
            .ifPresent(files -> hashtagService.updateFeedHashtag(request.getHashtags(), targetFeed));
    Optional.ofNullable(request.getImages())
            .ifPresent(files -> imageService.updateFeedImage(files, targetFeed));

    return new SimpleDataResponse(id);
  }

  @Transactional
  public void delete(String id) {
    Feed targetFeed = findById(id);
    feedRepository.delete(targetFeed);
  }
}
