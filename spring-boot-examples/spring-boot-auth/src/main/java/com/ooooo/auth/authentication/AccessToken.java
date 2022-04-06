package com.ooooo.auth.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

  public static final AccessToken INVALID_ACCESS_TOKEN = new AccessToken(null, new Date());

  private String accessToken;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
  private Date expiresAt;
}