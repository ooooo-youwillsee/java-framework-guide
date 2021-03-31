# @EnableOAuth2Sso 实现原理


## 用法

注解 `@EnableOAuth2Sso` 是用在 **client** 端。

- 例如， 有三个系统，分别有 A、B、C， A,B 都是**client**, 是需要认证，而 C 是**授权服务器**。
- A，B 通过 `ClientCredentialsResourceDetails` 配置其属性，然后配置 `WebSecurityConfigurerAdapter` 。
- 要用 `Redis` 作为 `Session` 在 A 和 B 中共享，否则每次重启都要重新认证。

说明：
- `ClientCredentialsResourceDetails` 主要配置**授权码模式**的必要参数，但是其中没有参数 `username` 和 `password`。
- `WebSecurityConfigurerAdapter` 主要配置**判断权限**和**登录页**，。如果没有权限，跳转登录页，让用户输入 `username` 和 `password`, 这样就让**基于授权码模式的SSO**串起来了。


源码简述：
- `OAuth2ClientConfiguration` 重要，页面传入参数的 `username` 和 `password` 会填入 `AccessTokenRequest` 类，然后结合 `OAuth2RestTemplate` 完成**授权码认证**的过程。
- `OAuth2ClientContextFilter` 会设置 `currentUri` 属性，就是当前的url, 作为**授权码认证的**参数 `redirect_uri`。
- `OAuth2SsoDefaultConfiguration` 会配置 `SsoSecurityConfigurer`（非常重要）,其中会配置 `OAuth2ClientAuthenticationProcessingFilter`，如果该类 `attemptAuthentication()` 调用失败，关键代码为 `restTemplate.getAccessToken()`，会抛出 `UserRedirectRequiredException` 异常，在方法 `attemptAuthentication()` 中不会被捕获，会继续向外抛,最终会在类 `OAuth2ClientContextFilter` 中捕获，被重定向到**授权服务器**的地址，**授权服务器**发现没有权限，最终重定向到**登录页**，所以**授权服务器**必须配置登录页，重定向的参数都会带上。
- 重定向**登录页**后，用户输入 `username` 和 `password`， **授权服务器**认证通过后，会返回一个**302**的重定向请求，浏览器发现这个请求是**302**，就会直接请求这个地址，这时参数 `code` 和 `state` 会传到 **client** 端，**client** 尝试继续走**授权码模式**认证过程，这里很重要一点就是 `OAuth2ClientContext` 配置的 scope 是 **session** 级别，里面有新一个的 `AccessTokenRequest` 信息。



可以参考的例子：

- [1](https://blog.csdn.net/javarrr/article/details/88710407)

- `PreAuthenticatedAuthenticationProvider` 用于**刷新token**的认证，userDetailService