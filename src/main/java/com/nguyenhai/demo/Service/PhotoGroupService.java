package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.PhotoGroup;
import com.nguyenhai.demo.Response.PhotoResponse;

import java.util.List;

public interface PhotoGroupService {

    PhotoGroup save(PhotoGroup photoGroup);

    void deleteByPostId(String postId);

    List<PhotoResponse> findPhotos(String idUser, Integer page, Integer number);

}
