package com.wowls.dms.provider;

import com.wowls.dms.dto.WeatherDataSourceDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CsvDataReaderTest {

    CsvDataReader csvDataReader;

    @Before
    public void setUp(){
        csvDataReader = new CsvDataReader();
    }

    @Test(expected = NullPointerException.class)
    public void readData_fileNotFindException_withNullPath() {
        // given
        String path = null;
        Class clazz = WeatherDataSourceDto.class;
        // when
        csvDataReader.readData(path, clazz);
    }
    @Test(expected = NullPointerException.class)
    public void readData_fileNotFindException_withNullClass() {
        // given
        String path = "";
        Class clazz = WeatherDataSourceDto.class;
        // when
        csvDataReader.readData(path, clazz);
    }
}