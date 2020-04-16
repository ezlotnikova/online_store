package com.gmail.ezlotnikova.repository.util;

import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    public int getQueryStartPosition(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }

}