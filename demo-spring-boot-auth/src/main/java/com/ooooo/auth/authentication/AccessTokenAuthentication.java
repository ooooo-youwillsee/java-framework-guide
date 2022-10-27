package com.ooooo.auth.authentication;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author leizhijie
 * @date 2022/3/29 14:33
 * @see AccessTokenFilter
 * @since 1.0.0
 */
@Getter
@Setter
public class AccessTokenAuthentication implements Authentication {

  public static final String PRINCIPAL = "principal";
  public static final String DETAILS = "details";
  public static final String AUTHORITIES = "authorities";
  public static final String NAME = "name";
  public static final String UNIQUE_ID = "uniqueId";
  public static final String EXPIRES_AT = "expiresAt";

  private Object principal;

  private Object details;

  @JSONField(serializeUsing = AuthoritiesFieldsSerilaizer.class, deserializeUsing = AuthoritiesFieldsDeserializer.class)
  private Collection<? extends GrantedAuthority> authorities;

  private String name;

  @JSONField(serialize = false, deserialize = false)
  private boolean authenticated;

  @JSONField(serialize = false, deserialize = false)
  private Object credentials;

  // ===========extension properties===============

  /**
   * 用户的唯一标识
   */
  private String uniqueId;

  private Date expiresAt;

  public Date getExpiresAt() {
    if (expiresAt == null) {
      throw new IllegalArgumentException("expiresAt is null");
    }
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    if (expiresAt == null) {
      throw new IllegalArgumentException("expiresAt is null");
    }
    this.expiresAt = expiresAt;
    this.authenticated = expiresAt.after(new Date());
  }

  public void setAuthenticated(boolean authenticated) {
    throw new IllegalArgumentException("you cann't set the field, you should set field named 'expiresAt'");
  }

  static String[] getAuthorities(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
  }

  static Collection<? extends GrantedAuthority> getAuthorities(List<String> authorities) {
    if (authorities == null || authorities.size() == 0) {
      return Collections.emptyList();
    }
    return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  // ====================fastjson======================

  public static class AuthoritiesFieldsSerilaizer implements ObjectSerializer {

    @SuppressWarnings("unchecked")
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
      serializer.write(getAuthorities((Collection<? extends GrantedAuthority>) object));
    }
  }

  public static class AuthoritiesFieldsDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
      return (T) getAuthorities(parser.parseArray(String.class));
    }

    @Override
    public int getFastMatchToken() {
      return 0;
    }
  }
}
