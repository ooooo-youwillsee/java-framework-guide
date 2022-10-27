package com.ooooo.auth.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author leizhijie
 * @date 2022/3/30 15:17
 * @since 1.0.0
 */
@Component
@Getter
public class SM4KeyPairConfig {

  private final boolean hexString = true;

  private final String secretKey = "c9740a359a10f7443267792932d34ab8";

  private final String iv = "a2917782e93df1f9e88c6dda24b3e806";


}
