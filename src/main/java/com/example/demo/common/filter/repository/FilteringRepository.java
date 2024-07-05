package com.example.demo.common.filter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilteringRepository<T, F> {
    Page<T> findAllByFilter(F filterDto, Pageable pageable);
}
