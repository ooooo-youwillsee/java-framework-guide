package com.ooooo.auth.config;

import com.ooooo.auth.convert.ConverterApplyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author leizhijie
 * @date 2022/3/31 15:22
 * @since 1.0.0
 */
public class ConvertedAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

  @Autowired
  @Lazy
  private ConverterApplyFactory converterApplyFactory;

  private static final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver = new AuthenticationPrincipalArgumentResolver();

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return authenticationPrincipalArgumentResolver.supportsParameter(parameter);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    // get securtiy context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // method parameter type
    Class<?> parameterType = parameter.getParameterType();
    // try convert
    if (authentication != null && converterApplyFactory.canConvert(authentication.getClass(), parameterType)) {
      return converterApplyFactory.convert(authentication, parameterType.newInstance());
    }

    return authenticationPrincipalArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
  }
}
