package com.wowls.dms.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

/**     기상청 초단기실황
 *      PTY	강수형태	코드값	4
 *     	T1H	기온	℃	10
 *      RN1	1시간 강수량	mm	8
 *      REH	습도	%	8
 *      UUU	동서바람성분	m/s	12
 *      VVV	남북바람성분	m/s	12
 *      VEC	풍향	0	10
 *      WSD	풍속	1	10
 *
 *      - 하늘상태(SKY) 코드 : 맑음(1), 구름많음(3), 흐림(4)    * 구름조금(2) 삭제 (2019.06.4)
 *      - 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
 *                           여기서 비/눈은 비와 눈이 섞여 오는 것을 의미 (진눈개비)
 * 동서바람성분(UUU) : 동(+표기), 서(-표기)
 * 남북바람성분(VVV) : 북(+표기), 남(-표기)
 */
@Builder
@Getter
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private int SKY; // 추후 추가
    private int PTY;
    private float T1H;
    private float RN1;
    private float REH;
    private float UUU;
    private float VVV;
    private int VEC;
    private int WSD;
}
