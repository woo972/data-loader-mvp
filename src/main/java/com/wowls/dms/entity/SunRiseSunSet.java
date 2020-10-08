package com.wowls.dms.entity;

import lombok.Builder;
import lombok.Getter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

// 미사용
@Getter
@Builder
@Entity
public class SunRiseSunSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date sunRise;
    private Date sunSet;
}
