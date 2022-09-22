package com.ooooo.impl;

import com.ooooo.AbstractSimplePropertySource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2020/11/5 09:58 从请求参数中获取数据
 */
@Order(0)
public class RequestParamsPropertySource extends AbstractSimplePropertySource {

  public RequestParamsPropertySource() {
    super("requestParams");
  }

  @Override
  public Object getProperty(String name) {
    String propertyValue = null;
    try {
      // 先请求参数中获取
      ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      HttpServletRequest request = attr.getRequest();
      propertyValue = request.getParameter(name);
      if (StringUtils.isBlank(propertyValue)) {
        // 再从请求头中获取
        propertyValue = request.getHeader(name);
      }
    } catch (Throwable ignored) {
    }
    // 空字符也当做null
    propertyValue = defaultIfBlank(propertyValue, null);
    return propertyValue;
  }

}