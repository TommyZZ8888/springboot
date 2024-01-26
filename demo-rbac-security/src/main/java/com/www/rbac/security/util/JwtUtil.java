package com.www.rbac.security.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.www.rbac.security.common.Consts;
import com.www.rbac.security.common.Status;
import com.www.rbac.security.config.JwtConfig;
import com.www.rbac.security.domain.vo.UserPrincipal;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description JwtUtil
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
@EnableConfigurationProperties(JwtConfig.class)
@Configuration
@Slf4j
public class JwtUtil {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 创建jwt
     *
     * @param rememberMe  记住我
     * @param id          用户id
     * @param subject     用户名
     * @param roles       用户角色
     * @param authorities 用户权限
     *
     * @return jwt
     */
    public String createJwt(Boolean rememberMe, Long id, String subject, List<String> roles, Collection<? extends GrantedAuthority> authorities) throws NoSuchAlgorithmException {
        Date now = new Date();
//        SecretKey secretKey = new SecretKeySpec(jwtConfig.getKey().getBytes(), SignatureAlgorithm.HS512.getJcaName()); //HmacSHA256加密 比sha1 加密性强
//        SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getKey().getBytes());   //HMAC-SHA1 加密
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

        // 设置密钥长度为256位
        keyGen.init(256);

        // 生成密钥
        SecretKey secretKey = keyGen.generateKey();

        JwtBuilder builder = Jwts.builder().setId(id.toString())
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .claim("roles", roles)
                .claim("authorities", authorities);
        // 过期时间
        Long ttl = rememberMe ? jwtConfig.getRemember() : jwtConfig.getTtl();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }

        String jwt = builder.compact();
        stringRedisTemplate.opsForValue().set(Consts.REDIS_JWT_KEY_PREFIX + subject, jwt, ttl, TimeUnit.MILLISECONDS);
        return jwt;
    }

    /**
     * 创建jwt
     *
     * @param authentication 用户认证信息
     * @param rememberMe     记住我
     *
     * @return jwt
     */
    public String createJwt(Authentication authentication, Boolean rememberMe) throws NoSuchAlgorithmException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return createJwt(rememberMe, principal.getId(), principal.getUsername(), principal.getRoles(), principal.getAuthorities());
    }

    /**
     * 解析jwt
     *
     * @param jwt jwt
     *
     * @return claim
     */
    public Claims parseJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getKey())
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            String redisKey = Consts.REDIS_JWT_KEY_PREFIX + username;

            Long expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
            if (Objects.isNull(expire) || expire <= 0) {
                throw new SecurityException(Status.TOKEN_EXPIRED.getMessage());
            }

            String token = stringRedisTemplate.opsForValue().get(redisKey);
            if (!StrUtil.equals(jwt, token)) {
                throw new SecurityException(Status.TOKEN_OUT_OF_CTRL.getMessage());
            }
            return claims;
        } catch (
                ExpiredJwtException e) {
            log.error("Token 已过期");
            throw new SecurityException(Status.TOKEN_EXPIRED.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("不支持的 Token");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token 无效");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR.getMessage());
        } catch (SecurityException e) {
            log.error("无效的 Token 签名");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token 参数不存在");
            throw new SecurityException(Status.TOKEN_PARSE_ERROR.getMessage());
        }
    }


    /**
     * 设置JWT过期
     *
     * @param request 请求
     */
    public void invalidateJwt(HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);
        String userName = getUserNameFromJwt(jwt);
        stringRedisTemplate.delete(Consts.REDIS_JWT_KEY_PREFIX + userName);
    }

    /**
     * 根据 jwt 获取用户名
     *
     * @param jwt JWT
     *
     * @return 用户名
     */
    public String getUserNameFromJwt(String jwt) {
        Claims claims = parseJwt(jwt);
        return claims.getSubject();
    }

    /**
     * 从 request 的header 中获取 token
     *
     * @param request 请求
     *
     * @return jwt
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
