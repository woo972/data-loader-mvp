package com.wowls.dms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Entity: 테이블과 링크될 클래스 표기
 *          클래스의 카멜케이스 네이밍을 언더스코어 네이밍으로 매칭
 *          @Table(name ="")로 매핑할 수도 있음
 * Hibernate 사용시 기본 생성자가 있어야 함 -> @Builder 사용시 디폴트 생성자를 만들지 않음 -> @NoArgsConstructor / @AllArgsConstructor 모두 필요
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long weatherId;
    @Column(name = "weather_data")
    private String weatherData;
}
