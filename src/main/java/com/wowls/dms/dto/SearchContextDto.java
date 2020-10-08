package com.wowls.dms.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@RequiredArgsConstructor
public class SearchContextDto {
//    private PageRequest pageRequest;
    private int currentPage;
    private int sizeOfPage;

}
