package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Service.PasswordService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service(value = "codeChangePasswordService")
public class PasswordServiceImpl implements PasswordService {

    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public PasswordServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getCodeChangePassword(String idUser) {
        Object objCode = redisTemplate.opsForValue().get(buildKeyId1(idUser));
        if (objCode != null) {
            return (String) objCode;
        }
        return generateCodeChangePassword(idUser);
    }

    @Override
    public Boolean verifyCodeChangePassword(String code, String idUser) {
        Object objCode = redisTemplate.opsForValue().get(buildKeyId1(idUser));
        if (objCode != null) {
            return objCode.equals(code);
        }
        return false;
    }

    private String generateCodeChangePassword(String idUser) {
        String code = RandomStringUtils.randomAlphanumeric(8);
        Object key = buildKeyId1(idUser);
        redisTemplate.opsForValue().set(key, code);
        redisTemplate.expire(key, TIME_LOGOUT_CODE_EXPIRES, TimeUnit.MINUTES);
        return code;
    }

    private Object buildKeyId1(String idUser) { return PREFIX_CHANGE_PASSWORD + idUser; }
}
