package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Service.LogoutService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service(value = "logoutService")
public class LogoutServiceImpl<S extends Session> implements LogoutService {

    private FindByIndexNameSessionRepository<S> sessionRepository;
    private SpringSessionBackedSessionRegistry<S> sessionRegistry;
    private RedisTemplate<Object, Object> redisTemplate;
    private InfoUserService infoUserService;

    @Autowired
    public LogoutServiceImpl(FindByIndexNameSessionRepository<S> sessionRepository, SpringSessionBackedSessionRegistry<S> sessionRegistry, RedisTemplate<Object, Object> redisTemplate, InfoUserService infoUserService) {
        this.sessionRepository = sessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.redisTemplate = redisTemplate;
        this.infoUserService = infoUserService;
    }

    @Override
    public Boolean logoutAllDevices(String code) {
        Object objIdUser = redisTemplate.opsForValue().get(buildKeyId(code));
        if (objIdUser == null) { return false; }

        InfoUser me = infoUserService.findById((String) objIdUser);
        sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, me.getEmail()).values()
                .forEach(s -> {
                    if (!s.isExpired()) {
                        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(s.getId());
                        sessionInformation.expireNow();
                    }
                });

        return true;
    }

    @Override
    public String getCodeLogoutAllDevices(String idUser) {
        String key1 = buildKeyId(idUser);
        Object obj = redisTemplate.opsForValue().get(key1);
        if (obj != null) {
            redisTemplate.expire(key1, TIME_THE_LOGOUT_CODE_EXPIRES, TimeUnit.MINUTES);
            redisTemplate.expire(buildKeyId((String) obj), TIME_THE_LOGOUT_CODE_EXPIRES, TimeUnit.MINUTES);
            return (String) obj;
        }

        String code = RandomStringUtils.randomAlphanumeric(18);

        // code -> id
        redisTemplate.opsForValue().set(buildKeyId(code), idUser);
        redisTemplate.expire(buildKeyId(code), TIME_THE_LOGOUT_CODE_EXPIRES, TimeUnit.MINUTES);
        // id -> code
        redisTemplate.opsForValue().set(buildKeyId(idUser), code);
        redisTemplate.expire(buildKeyId(idUser), TIME_THE_LOGOUT_CODE_EXPIRES, TimeUnit.MINUTES);

        return code;
    }

    private String buildKeyId(String v) { return PREFIX_LOGOUT_ALL_DEVICES + v; }
}
