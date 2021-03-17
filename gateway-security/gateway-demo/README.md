## 说明

1. 使用 `spring cloud gateway` 来实现网关
2. `.oauth2ResourceServer()` 来实现资源服务器，需要配置 `jwt`

注意：

1. `jwt` 配置可以通过实现 `org.springframework.security.authentication.ReactiveAuthenticationManager` 接口来配置，比如，请求 `auth` 服务的检查 token 的地址 `/oauth/check_token`，利用 `org.springframework.web.reactive.function.client.WebClient` 发送请求，完成认证

注意：

1. 对于**资源服务器**来说，默认用 `hasAuthority("SCOPE_api")` ，`SCOPE` 这种的权限。因为 `ROLE` 权限，是针对于自身服务 `UserDetailsService` 的实现，必须要进行相应的 `HttpBasic/Form` 认证。

默认实现 `org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter` 中 的 `org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter`，只提取 `SCOPE` 的权限


