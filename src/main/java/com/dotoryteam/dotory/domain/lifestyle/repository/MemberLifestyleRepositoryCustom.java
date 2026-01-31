package com.dotoryteam.dotory.domain.lifestyle.repository;

import com.dotoryteam.dotory.domain.lifestyle.entity.MemberLifestyle;

import java.util.List;
import java.util.UUID;

public interface MemberLifestyleRepositoryCustom {
    List<String> findKeywordsByMemberKey(UUID memberKey);;
    void bulkDeleteByDeletedLifestyles(Long id);
}
