package com.dotoryteam.dotory.domain.auth.entity;

import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "term_title", nullable = false, length = 100)
    private String termTitle;

    @Column(name = "term_content", nullable = false , columnDefinition = "TEXT")
    private String termContent;

    @Column(name = "is_require" , nullable = false)
    private boolean isRequire;

    @Column(name = "term_version", nullable = false, length = 20)
    private String termVersion;

    @Builder
    public Terms(String name, String termContent, boolean isRequire, String version) {
        this.termTitle = name;
        this.termContent = termContent;
        this.isRequire = isRequire;
        this.termVersion = version;
    }

    //약관 수정
    public void updateTermContent(String termContent) {
        this.termContent = termContent;
    }
}
