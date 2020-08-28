package com.nguyenhai.demo.Service.Implement;

import com.google.common.collect.Lists;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Exception.EmailExistsException;
import com.nguyenhai.demo.Exception.InfoUserNotFoundException;
import com.nguyenhai.demo.Repository.InfoUserRepository;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Service.InfoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "infoUserService")
public class InfoUserServiceImpl implements InfoUserService {

    private InfoUserRepository infoUserRepository;

    @Autowired
    public InfoUserServiceImpl(InfoUserRepository infoUserRepository) {
        this.infoUserRepository = infoUserRepository;
    }

    @Override
    public InfoUser save(InfoUser infoUser) {
        infoUserRepository.findByEmail(infoUser.getEmail())
                .ifPresent(e -> { throw new EmailExistsException(e.getEmail()); });
        return infoUserRepository.save(infoUser);
    }

    @Override
    public InfoUser update(InfoUser infoUser) {
        return infoUserRepository.save(infoUser);
    }

    @Override
    public InfoUser findByEmail(String email) {
        return infoUserRepository.findByEmail(email)
                .orElseThrow(() -> new InfoUserNotFoundException(email));
    }

    @Override
    public InfoUser findById(String id) {
        return infoUserRepository.findById(id)
                .orElseThrow(InfoUserNotFoundException::new);
    }

    @Override
    public List<InfoUser> findByIdIsIn(List<String> ids) {
        return infoUserRepository.findByIdIsIn(ids);
    }

    @Override
    public List<InfoUser> findAllByIdIsNotIn(List<String> ids) {
        return infoUserRepository.findAllByIdIsNotIn(ids);
    }

    @Override
    public List<InfoUser> findAllByTerm(String term) {
        return infoUserRepository.findAll().stream()
                .filter(infoUser -> {
                    String fullName = infoUser.getFullName().toLowerCase();
                    return fullName.contains(term.trim().toLowerCase());
                }).collect(Collectors.toList());
    }

}
