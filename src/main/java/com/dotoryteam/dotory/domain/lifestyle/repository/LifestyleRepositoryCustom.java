package com.dotoryteam.dotory.domain.lifestyle.repository;

import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;

import java.util.List;
import java.util.Optional;

public interface LifestyleRepositoryCustom {
    List<Lifestyle> findAll();
    List<Lifestyle> findByCodesIn(List<String> codes);
    boolean existsByCode(String code);
    boolean existsByName(String name);
    Optional<Lifestyle> findByLifestyleId(Long id);
    void deleteByLifestyleId(Long id);
}
