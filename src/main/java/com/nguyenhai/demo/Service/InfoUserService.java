package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InfoUserService {

    InfoUser save(InfoUser infoUser);

    InfoUser update(InfoUser infoUser);

    InfoUser findByEmail(String email);

    InfoUser findById(String id);

    List<InfoUser> findByIdIsIn(List<String> ids);

    List<InfoUser> findAllByIdIsNotIn(List<String> ids);

    @Deprecated
    List<InfoUser> findAllByTerm(String term);
}
