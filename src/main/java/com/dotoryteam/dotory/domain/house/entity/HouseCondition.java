package com.dotoryteam.dotory.domain.house.entity;

import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "house_condition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseCondition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_condition_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id", nullable = false)
    private Condition condition;

    @Builder
    public HouseCondition(House house, Condition condition) {
        this.house = house;
        this.condition = condition;
    }

    // 연관관계 편의 메서드
    public void setHouse(House house) {
        if (this.house != null) {
            this.house.getHouseConditions().remove(this);
        }
        this.house = house;
        house.getHouseConditions().add(this);
    }

    public void setCondition(Condition condition) {
        if (this.condition != null) {
            this.condition.getHouseConditions().remove(this);
        }
        this.condition = condition;
        condition.getHouseConditions().add(this);
    }

    // 정적 팩토리 메서드
    public static HouseCondition of(House house, Condition condition) {
        HouseCondition houseCondition = HouseCondition.builder()
                .house(house)
                .condition(condition)
                .build();
        house.getHouseConditions().add(houseCondition);
        condition.getHouseConditions().add(houseCondition);
        return houseCondition;
    }
}