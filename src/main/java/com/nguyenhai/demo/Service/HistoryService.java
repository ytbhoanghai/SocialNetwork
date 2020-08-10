package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Response.CardHistoryResponse;

import java.util.List;

public interface HistoryService {

    List<CardHistoryResponse> getHistory(Integer page, Integer number, String email);

    List<CardHistoryResponse> getHistoryNext(String id, Integer number, String email);

}
