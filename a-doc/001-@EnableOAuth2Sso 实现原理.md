# @EnableOAuth2Sso 实现原理


## 1.用法

注解 `@EnableOAuth2Sso` 是用在 **client** 端。

- 例如， 有三个系统，分别有 A、B、C， A,B 都是**client**, 是需要认证，而 C 是**授权服务器**。
- A，B 通过 `AuthorizationCodeResourceDetails` 配置其属性，然后配置 `WebSecurityConfigurerAdapter`(防止无限重定向) 。

说明：
- `AuthorizationCodeResourceDetails` 主要配置**授权码模式**的必要参数，但是其中没有参数 `username` 和 `password`,该属性配置到client端 。
- `WebSecurityConfigurerAdapter` 主要配置**判断权限**和**登录页**，。如果没有权限，跳转登录页，让用户输入 `username` 和 `password`, 这样就让**基于授权码模式的SSO**串起来了, 该属性配置到服务端，通过 `loginPage` 和 `loginProcessingUrl` 来配置。


## 2.源码简述：
- `OAuth2ClientConfiguration` 重要，页面传入参数的 `username` 和 `password` 会填入 `AccessTokenRequest` 类，然后结合 `OAuth2RestTemplate` 完成**授权码认证**的过程。
- `OAuth2ClientContextFilter` 会设置 `currentUri` 属性，就是当前的url, 作为**授权码认证的**参数 `redirect_uri`。
- `OAuth2SsoDefaultConfiguration` 会配置 `SsoSecurityConfigurer`（非常重要）,其中会配置 `OAuth2ClientAuthenticationProcessingFilter`，如果该类 `attemptAuthentication()` 调用失败，关键代码为 `restTemplate.getAccessToken()`，会抛出 `UserRedirectRequiredException` 异常，在方法 `attemptAuthentication()` 中不会被捕获，会继续向外抛,最终会在类 `OAuth2ClientContextFilter` 中捕获，被重定向到**授权服务器**的地址，**授权服务器**发现没有权限，最终重定向到**登录页**，所以**授权服务器**必须配置登录页，重定向的参数都会带上。
- 重定向**登录页**后，用户输入 `username` 和 `password`， **授权服务器**认证通过后，会返回一个**302**的重定向请求，浏览器发现这个请求是**302**，就会直接请求这个地址，这时参数 `code` 和 `state` 会传到 **client** 端，**client** 尝试继续走**授权码模式**认证过程，这里很重要一点就是 `OAuth2ClientContext` 配置的 scope 是 **session** 级别，里面有新一个的 `AccessTokenRequest` 信息。



可以参考的例子：

- [1](https://blog.csdn.net/javarrr/article/details/88710407)

- `PreAuthenticatedAuthenticationProvider` 用于**刷新token**的认证，userDetailService


## 3. 详细源码流程分析


1. client 端： 页面请求 `http://127.0.0.1:8986` 地址，**WebSecurityConfig** 配置，发现其没有权限访问，会被重定向到 `/login` (`OAuth2SsoProperties`的loginPath来配置), 原因是认证失败后会有异常，`org.springframework.boot.autoconfigure.security.oauth2.client.SsoSecurityConfigurer.addAuthenticationEntryPoint` 这个方法会异常拦截,重定向到 `/login`。
2. client 端： `org.springframework.boot.autoconfigure.security.oauth2.client.SsoSecurityConfigurer.oauth2SsoFilter` 这个方法添加了类 **OAuth2ClientAuthenticationProcessingFilter** ，会处理上面的 `/login` 的请求， 在调用这个方法 `org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter.attemptAuthentication` 尝试认证, 执行 `restTemplate.getAccessToken()`
3. client 端：`org.springframework.security.oauth2.client.OAuth2RestTemplate.acquireAccessToken` 来获取token， 最终调用方法 `accessTokenProvider.obtainAccessToken(resource, accessTokenRequest)` 来获取, 实现类为 **AuthorizationCodeAccessTokenProvider**, 最后调用方法 `org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider.getRedirectForAuthorization` 来抛出重定义异常，然后 `OAuth2ClientContextFilter` 跳转重定向地址 `http://127.0.0.1:7777/oauth/authorize` 。
4. server 端: 发现地址 `/oauth/authorize` 没有权限，重定向到 `loginPage` 页面, 这时候用户就输入 `username` 和 `password`, 输入完成后，点击登录，就要请求 `loginProcessingUrl` 地址。
5. 对于配置 `loginProcessingUrl` 就会配置一个 `UsernamePasswordAuthenticationFilter`, 完成 `username` 和 `password` 校验之后，如果成功会回调 `SavedRequestAwareAuthenticationSuccessHandler` (这个配置在 AbstractAuthenticationFilterConfigurer 中)，在方法 `org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler.onAuthenticationSuccess` 中 **savedRequest**肯定不为空 (因为之前重定向过)  ，最终就会执行这个语句 `getRedirectStrategy().sendRedirect(request, response, targetUrl)`, 这个 targetUrl 就是 `http://127.0.0.1:7777/oauth/authorize?client_id=xxx&redirect_uri=http://127.0.0.1:8986/login&response_type=code&scope=api&state=1Tj9iT`, 获取**授权码**。
6. 在上一步中，如果获取**授权码**成功，就会回调到 `http://127.0.0.1:8986/login`, 又会经过类 `OAuth2ClientAuthenticationProcessingFilter` 来处理，这时最终就会调用 `org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport.retrieveToken` 来获取**token**, 成功调用之后，会回调类 `SavedRequestAwareAuthenticationSuccessHandler`，返回到最初的页面, 这个 targetUrl 是 `http://127.0.0.1:8986/`

7. 如果回调失败，原因肯定是 `SavedRequestAwareAuthenticationSuccessHandler` 中没有会话信息
8. 默认情况下，Spring 是没有提供 SSO 刷新token的接口。 