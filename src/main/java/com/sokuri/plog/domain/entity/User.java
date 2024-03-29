package com.sokuri.plog.domain.entity;

import com.sokuri.plog.domain.auditing.BaseTimeEntity;
import com.sokuri.plog.domain.converter.StringToUuidConverter;
import com.sokuri.plog.global.dto.user.SignInResponse;
import com.sokuri.plog.domain.eums.Role;
import com.sokuri.plog.domain.eums.SocialProvider;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
  @Id
  @GeneratedValue(generator = "user")
  @GenericGenerator(name = "user", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "user_id", columnDefinition = "BINARY(16) DEFAULT (UNHEX(REPLACE(UUID(), \"-\", \"\")))")
  @Convert(converter = StringToUuidConverter.class)
  private UUID id;

  @Column(nullable = false, unique = true)
  @NotEmpty(message = "닉네임은 필수 입력값이에요")
  private String nickname;

  @Column(nullable = false, unique = true)
  @Email(message = "메일 형식에 맞춰 작성해주세요",
          regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
  @NotEmpty(message = "메일 형식에 맞춰 작성해주세요")
  private String email;

  @Column
  private String password;

  @Column
  private LocalDate birthday;

  @Column
  private String profileImage;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Feed> feeds;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private SocialProvider socialProvider;

  public SignInResponse toSummaryResponse() {
    return SignInResponse.builder()
            .id(id)
            .nickname(nickname)
            .email(email)
            .profileImage(profileImage)
            .build();
  }
}
