package com.example.goodreads_finalproject.service;

import java.util.List;

public class PaginationUtils<T> {

    public List<T> searchData(List<T> data, Integer pageIndex, Integer pageSize) {
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = pageIndex * pageSize;
        int lastIndex = data.size() - 1;
        if (endIndex > lastIndex) {
            endIndex = lastIndex + 1;
        }
        return data.subList(startIndex, endIndex);
    }

    public int getPageNumber(List<T> data, Integer pageSize) {
        return (int) Math.ceil((float) data.size() / pageSize);
    }
}
