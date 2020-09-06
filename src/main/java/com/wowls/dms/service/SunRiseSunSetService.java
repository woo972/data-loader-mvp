package com.wowls.dms.service;

import com.wowls.dms.entity.SunRiseSunSet;
import com.wowls.dms.provider.SunRiseSunSetDataProvider;
import com.wowls.dms.repository.SunRiseSunSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class SunRiseSunSetService {

    private SunRiseSunSetDataProvider sunRiseSunSetDataProvider;
    private SunRiseSunSetRepository sunRiseSunSetRepository;

    @Transactional
    public void save(){
        SunRiseSunSet sunRiseSunSet = sunRiseSunSetDataProvider.getSunRiseSunSet();
        sunRiseSunSetRepository.save(sunRiseSunSet);
    }
}
