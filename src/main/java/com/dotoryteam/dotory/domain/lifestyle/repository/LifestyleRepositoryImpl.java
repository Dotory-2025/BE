package com.dotoryteam.dotory.domain.lifestyle.repository;

import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.lifestyle.entity.QLifestyle;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.dotoryteam.dotory.domain.lifestyle.entity.QLifestyle.lifestyle;

@RequiredArgsConstructor
public class LifestyleRepositoryImpl implements LifestyleRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public List<Lifestyle> findAll() {
        return queryFactory.selectFrom(lifestyle).fetch();
    }

    public List<Lifestyle> findByCodesIn(List<String> codes) {
        return queryFactory.selectFrom(lifestyle)
                .where(codesIn(codes))
                .fetch();
    }

    private BooleanExpression codesIn(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return null;
        }
        return lifestyle.code.in(codes);
    }

    public boolean existsByCode(String code) {
        Integer cnt =  queryFactory.selectOne()
                .from(lifestyle)
                .where(lifestyle.code.eq(code))
                .fetchFirst();
        return cnt != null;
    }

    public boolean existsByName(String name) {
        Integer cnt =  queryFactory.selectOne()
                .from(lifestyle)
                .where(lifestyle.name.eq(name))
                .fetchFirst();
        return cnt != null;
    }

    public Optional<Lifestyle> findByLifestyleId(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(lifestyle)
                        .where(lifestyle.id.eq(id))
                        .fetchOne()
        );
    }

    public void deleteByLifestyleId(Long id) {
        queryFactory
                .delete(lifestyle)
                .where(lifestyle.id.eq(id))
                .execute();
    }
}
