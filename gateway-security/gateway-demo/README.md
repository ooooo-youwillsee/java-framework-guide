## 说明

1. 使用 `spring cloud gateway` 来实现网关
2. `.oauth2ResourceServer()` 来实现资源服务器，需要配置 `jwt`

注意：

1. `jwt` 配置可以通过实现 `org.springframework.security.authentication.ReactiveAuthenticationManager` 接口来配置，比如，请求 `auth` 服务的检查 token 的地址 `/oauth/check_token`，利用 `org.springframework.web.reactive.function.client.WebClient` 发送请求，完成认证