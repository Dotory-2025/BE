package com.dotoryteam.dotory.domain.dormitory.entity;

import com.dotoryteam.dotory.global.common.BaseEntity;
import com.dotoryteam.dotory.domain.member.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id", nullable = false)
    private Dormitory dormitory;

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Gender sex;

    @Builder
    public RoomType(Dormitory dormitory, Integer capacity, Gender sex) {
        this.dormitory = dormitory;
        this.capacity = capacity;
        this.sex = sex;
    }

    public void changeDormitory(Dormitory dormitory) {
        if (this.dormitory != null) {
            this.dormitory.getRoomTypes().remove(this);
        }
        this.dormitory = dormitory;
        dormitory.getRoomTypes().add(this);
    }

    public void updateCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}