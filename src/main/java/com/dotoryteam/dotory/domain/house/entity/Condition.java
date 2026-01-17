package com.dotoryteam.dotory.domain.house.entity;

import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "condition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Condition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "condition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseCondition> houseConditions = new ArrayList<>();

    @Builder
    public Condition(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public void updateInfo(String name, String code) {
        if (name != null) this.name = name;
        if (code != null) this.code = code;
    }
}