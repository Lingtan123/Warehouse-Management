package com.wms.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wms.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    //final不能加注解解决注入依赖问题，而且构造函数是行业规范
    /*@Autowired
    private final ObjectMapper objectMapper;*/
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret:wms-jwt-secret-change-me}")
    private String secret;

    @Value("${jwt.expire-hours:12}")
    private long expireHours;

    public JwtUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String generateToken(User user) {
        try {
            //令牌签发时间
            long issuedAt = Instant.now().getEpochSecond();
            //令牌过期时间
            long expiresAt = Instant.now().plusSeconds(expireHours * 3600).getEpochSecond();

            //组装令牌，这里是令牌头
            Map<String, Object> header = new LinkedHashMap<>();
            //加入加密算法
            header.put("alg", "HS256");
            //加入类型固定JWT
            header.put("typ", "JWT");

            //用户数据封装，准备加密
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("userId", user.getId());
            payload.put("no", user.getNo());
            payload.put("name", user.getName());
            payload.put("roleId", user.getRoleId());
            //签发时间和过期时间
            payload.put("iat", issuedAt);
            payload.put("exp", expiresAt);

            //令牌头和用户数据转JSON进行URL-Base64编码
            String headerPart = encodeJson(header);
            String payloadPart = encodeJson(payload);
            String signaturePart = sign(headerPart + "." + payloadPart);
            //签名只是为了加密校验，防止篡改，规避用户数据暴露只能在业务层进行
            return headerPart + "." + payloadPart + "." + signaturePart;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate JWT token", ex);
        }
    }

    public CurrentUser parseToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            String es = sign(parts[0] + "." + parts[1]);
            //这是一个安全的字节对比方法，把本地再次加密的令牌头+用户数据对比token的加密结果，保证是本地用户
            if (!MessageDigest.isEqual(es.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
                return null;
            }

            //校验通过，解码Base64，获取原始二进制JSON
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            Map<String, Object> payload = objectMapper.readValue(
                    payloadBytes, new TypeReference<Map<String, Object>>() {}
            );
            //判断令牌过期
            Number exp = (Number) payload.get("exp");
            if (exp == null || Instant.now().getEpochSecond() >= exp.longValue()) {
                return null;
            }

            //封装当前登录对象
            return new CurrentUser(
                    getInteger(payload.get("userId")),
                    (String) payload.get("no"),
                    (String) payload.get("name"),
                    getInteger(payload.get("roleId"))
            );
        } catch (Exception ex) {
            return null;
        }
    }

    private String encodeJson(Map<String, Object> value) throws Exception {
        //转二进制JSON，因为JSON通用性强，Base只能转换字节数组，使用二进制JSON
        byte[] jsonBytes = objectMapper.writeValueAsBytes(value);
        //转URL-Base64编码，这是JWT规范，也能防止转义符等等
        return Base64.getUrlEncoder().withoutPadding().encodeToString(jsonBytes);
    }

    //给令牌头和用户数据整合一块生成签名的
    private String sign(String content) throws Exception {
        //获取HmacSHA256加密实例
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        //使用配置文件的密钥构建加密密钥对象
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
        mac.init(keySpec);
        //对(令牌头.用户数据)字符串加密
        byte[] signatureBytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        //转Base64编码
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
    }

    private Integer getInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }
}
