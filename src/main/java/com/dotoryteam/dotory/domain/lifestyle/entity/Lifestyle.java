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

    @Column(nullable = false , unique = true)
    private String code;

    @Builder
    public Lifestyle(String name , String code) {
        this.name = name;
        this.code = code;
    }

    public void updateLifestyle(String lifestyleName , String lifestyleCode) {
        if (lifestyleName != null) this.name = lifestyleName;
        if (lifestyleCode != null) this.code = lifestyleCode;
    }
}
