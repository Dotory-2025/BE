package com.dotoryteam.dotory.domain.lifestyle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lifestyle {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false , unique = true , length = 20)
    private String name;

    @Builder
    public Lifestyle(String name) {
        this.name = name;
    }

    public void updateLifestyle(String lifestyleName) {
        this.name = lifestyleName;
    }
}
