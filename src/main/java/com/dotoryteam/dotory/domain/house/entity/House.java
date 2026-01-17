package com.dotoryteam.dotory.domain.house.entity;

import com.dotoryteam.dotory.domain.dormitory.entity.RoomType;
import com.dotoryteam.dotory.global.common.BaseEntity;
import com.dotoryteam.dotory.domain.house.enums.Semester;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "house")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class House extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_id")
    private Long id;

    @Column(name = "house_key", nullable = false, unique = true, updatable = false, columnDefinition = "UUID")
    private UUID houseKey;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    private Semester semester;

    @Column(name = "is_recruiting")
    @ColumnDefault("true")
    private Boolean isRecruiting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseCondition> houseConditions = new ArrayList<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseMember> houseMembers = new ArrayList<>();

    @Builder
    public House(String title, String description, Semester semester, RoomType roomType) {
        this.houseKey = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.semester = semester;
        this.roomType = roomType;
        this.isRecruiting = true;
    }

    // 비즈니스 메서드
    public void updateInfo(String title, String description, Semester semester) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (semester != null) this.semester = semester;
    }

    public void closeRecruiting() {
        this.isRecruiting = false;
    }

    public void openRecruiting() {
        this.isRecruiting = true;
    }

    public boolean isFull() {
        return houseMembers.size() >= roomType.getCapacity();
    }

    public boolean isOwner(Long memberId) {
        return houseMembers.stream()
                .anyMatch(hm -> hm.getMemberId().equals(memberId) && hm.getIsOwner());
    }
}