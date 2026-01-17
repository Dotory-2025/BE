package com.dotoryteam.dotory.domain.dormitory.entity;

import com.dotoryteam.dotory.global.common.BaseEntity;
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

    @Column(name = "sex", length = 1)
    private String sex; // 'M' or 'F'

    @Builder
    public RoomType(Dormitory dormitory, Integer capacity, String sex) {
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