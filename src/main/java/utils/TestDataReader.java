package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class TestDataReader {


    public static <T> T readDataFromYMl(String ymlFilePath, Class<T> type) {

        T testData = null;
        try {
          log.info(ymlFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(ymlFilePath)));
            var om = new ObjectMapper(new YAMLFactory());
            testData=om.readValue(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return testData;
    }

}
