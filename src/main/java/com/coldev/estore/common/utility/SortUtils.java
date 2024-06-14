package com.coldev.estore.common.utility;

import com.coldev.estore.common.enumerate.SortType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SortUtils {
    public static Pageable getPagination(int pageSize, int pageNo, SortType sortType, String sortBy) {
        Pageable pageable;
        if (sortBy == null || sortBy.isBlank() || sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNo, pageSize);
        } else if (sortType == SortType.DESC) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        } else if (sortType == SortType.ASC) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize);
        }
        return pageable;
    }

    public static Sort getSort(SortType sortType, String sortBy) {
        if (sortBy == null || sortBy.isBlank() || sortBy.isEmpty()) {
            return null;
        } else if (sortType == SortType.DESC) {
            return Sort.by(sortBy).descending();
        }
        return Sort.by(sortBy).ascending();
    }

    public static Sort getSort(String sortAttribute, SortType sortType, String defaultSortAttribute) {
        String finalSortAttribute = sortAttribute == null ? defaultSortAttribute : sortAttribute;

        if (sortType == null) {
            return Sort.by(finalSortAttribute).ascending();
        }

        return sortType == SortType.ASC ?
                Sort.by(finalSortAttribute).ascending() : Sort.by(finalSortAttribute).descending();

    }
}
