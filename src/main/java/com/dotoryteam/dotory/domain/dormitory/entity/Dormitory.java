package com.dotoryteam.dotory.domain.dormitory.entity;

import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dormitory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dormitory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "dormitory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomType> roomTypes = new ArrayList<>();

    @Builder
    public Dormitory(String name, String description, String code) {
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public void updateInfo(String name, String description) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
    }
}