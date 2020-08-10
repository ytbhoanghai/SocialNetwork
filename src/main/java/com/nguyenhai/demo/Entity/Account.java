package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Document
@TypeAlias("account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    public static final Boolean IS_VALID_EMAIL_DEFAULT = false;
    public static final Boolean IS_ACCOUNT_NON_EXPIRED_DEFAULT = true;
    public static final Boolean IS_ACCOUNT_NON_LOCKED_DEFAULT = true;
    public static final Boolean IS_CREDENTIALS_NON_EXPIRED_DEFAULT = true;
    public static final Boolean IS_ENABLE_DEFAULT = true;
    public static final List<String> ROLES_DEFAULT = Collections.singletonList("USER");

    @Id
    private String id;
    @Indexed(unique=true)
    private String email;
    private String password;
    private TypeLogin typeLogin;
    private Boolean isValidEmail;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isEnabled;
    private Date dateCreated;
    private List<String> roles;

    public enum TypeLogin {
        DEFAULT, FACEBOOK, GOOGLE;
    }

    public static Account build(String id, String email, String password, TypeLogin type) {
        String encodePassword = new BCryptPasswordEncoder().encode(password);
        return Account.builder()
                .id(id)
                .email(email)
                .password(encodePassword)
                .typeLogin(type)
                .isValidEmail(IS_VALID_EMAIL_DEFAULT)
                .isAccountNonExpired(IS_ACCOUNT_NON_EXPIRED_DEFAULT)
                .isAccountNonLocked(IS_ACCOUNT_NON_LOCKED_DEFAULT)
                .isCredentialsNonExpired(IS_CREDENTIALS_NON_EXPIRED_DEFAULT)
                .isEnabled(IS_ENABLE_DEFAULT)
                .dateCreated(new Date(System.currentTimeMillis()))
                .roles(ROLES_DEFAULT).build();
    }
}
