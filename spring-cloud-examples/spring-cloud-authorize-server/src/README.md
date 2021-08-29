## 请求地址

- 请求授权码：

`curl --location --request GET 'http://localhost:7777/oauth/authorize?state=1&client_id=xxx&redirect_uri=http://localhost:7777/oauth/code&response_type=code&scope=read' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' `


- 请求token

`curl --location --request POST 'http://localhost:7777/oauth/token?grant_type=authorization_code&client_id=xxx&code=请求到的code&scope=read&redirect_uri=http://localhost:7777/oauth/code' \
--header 'Authorization: Basic Y3JoOnNlY3JldA=='`


- 刷新token

`curl --location --request POST 'http://localhost:7777/oauth/token?grant_type=refresh_token&client_id=xxx&refresh_token=你的RefreshToken\
--header 'Authorization: Basic Y3JoOnNlY3JldA=='`


- 检查token

`curl --location --request GET 'http://localhost:7777/oauth/check_token?token=你的token' \`


关键原理：

1. 请求 `/oauth/authorize` 地址，必须经过security的认证，用**Http Basic**, 用户名和密码由 `UserDetailsService` 来实现。

2. 请求 `/oauth/token` 地址，此时也需要进行 **Http Basic** 认证，底层实现有一个特殊的 `org.springframework.security.web.SecurityFilterChain`, 此时用户名和密码用 `ClientDetailsService` 来实现， 对应源码位置 `org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)` 的 `requestMatchers()`
   

关键类：

1. `org.springframework.security.oauth2.provider.endpoint.TokenEndpoint` 请求token地址

2. `org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint` 请求授权码地址

3. `org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint` 检查token地址

4、 `org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter` spring security 的配置，可以通过它来配置 `org.springframework.security.authentication.AuthenticationManager`


##

`jwt` 默认使用 `SHA256withRSA` 算法， 源码位置：`org.springframework.security.jwt.crypto.sign.RsaSigner`