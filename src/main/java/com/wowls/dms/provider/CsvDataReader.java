package com.wowls.dms.provider;

import lombok.extern.slf4j.Slf4j;
import com.opencsv.bean.CsvToBeanBuilder;
import com.wowls.dms.dto.CsvDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;

/**
 * Ref: http://opencsv.sourceforge.net/#reading_into_beans
 * Performance always being one of our top concerns, reading is multi-threaded. There are two performance choices left in your hands:
 * Time vs. memory: The classic trade-off. If memory is not a problem, read using CsvToBean.parse() or CsvToBean.stream(), which will read all beans at once and are multi-threaded. If your memory is limited, use CsvToBean.iterator() and iterate over the input. Only one bean is read at a time, making multi-threading impossible and slowing down reading, but only one object is in memory at a time (assuming you process and release the object for the garbage collector immediately).
 * Ordered vs. unordered. opencsv preserves the order of the data given to it by default. Maintaining order when using parallel programming requires some extra effort which means extra CPU time. If order does not matter to you, use CsvToBeanBuilder.withOrderedResults(false). The performance benefit is not large, but it is measurable. The ordering or lack thereof applies to data as well as any captured exceptions.
 */
// 미사용
@Slf4j
@Component
public class CsvDataReader {
    public List<CsvDto> readData(String path, Class clazz){
        return readData(path, clazz, 0);
    }

    public List<CsvDto> readData(String path, Class clazz, int skipLine){
        return readData(path, clazz, skipLine, ',');
    }

    public List<CsvDto> readData(String path, Class clazz, int skipLine, char separator){
        List<CsvDto> CsvDtos = Collections.emptyList();
        try {
            if(StringUtils.isNoneEmpty(path) && clazz != null){
                CsvDtos = new CsvToBeanBuilder(new FileReader(path))
                        .withType(clazz)
                        .withSkipLines(skipLine)
                        .withSeparator(separator)
                        .build()
                        .parse();
            }
        } catch (FileNotFoundException fileNotFoundException) {

        }
        return CsvDtos;
    }
}
