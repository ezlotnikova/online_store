package com.gmail.ezlotnikova.repository.util;

import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    public static int getQueryStartPosition(int pageNumber, int pageSize) {
        /* pageNumber variable passed this method is extracted from Pageable object,
        where page numeration starts from zero, so no need to extract 1 from pageNumber here */
        return (pageNumber) * pageSize;
    }

}