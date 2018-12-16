package com.apifuze.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataService {

    private final Logger log = LoggerFactory.getLogger(DataService.class);

    @Value("${OBN_HOME:}")
    private String OBN_HOME;

    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    private ObjectMapper objectMapper;


    public <T> T getSingleFromList(String dataName, Class<T> typeClass) {
        T data = null;
        List<T> dataList = getModelList(dataName, typeClass);
        if (!CollectionUtils.isEmpty(dataList)) {
            data = dataList.get(0);
        }
        return data;
    }


    public <T> List<T> getModelList(String dataName, Class<T> typeClass) {
        return getDataFoList(dataName, typeClass);
    }


    private File getOBNDataFile(String dataFileName) throws Exception {
        File file;
        String dataFilePath;
        if (StringUtils.isEmpty(OBN_HOME)) {
            dataFilePath = String.format("/data/%s.json", dataFileName);
            log.debug("OBN_HOME not configured attempting to load from default {}", dataFilePath);
            ClassPathResource classPathResource = new ClassPathResource(dataFilePath);
            file = File.createTempFile("temp", dataFileName);
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(classPathResource.getInputStream(), outputStream);
            outputStream.close();
        } else {
            dataFilePath = String.format("%s/data/%s.json", OBN_HOME, dataFileName);
            log.debug("attempting to load from default {}", dataFilePath);
            file = new File(dataFilePath);
        }
        if (file.exists()) {
            log.debug("Reading data from actual path {}", file.getAbsolutePath());
        }
        return file;
    }

    public <T> List<T> getDataFoList(String fileName, Class<T> type) {
        try {
            log.debug("Reading data from {} for {}", fileName, type.getSimpleName());
            File file = getOBNDataFile(fileName);
            if (file.exists()) {

                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
                return objectMapper.readValue(file, listType);


            } else {
                log.error("File not found for {} from {}", type, fileName);
            }
        } catch (Exception e) {
            log.warn("Cannot convert json data for {} using file {}", type, fileName);
            log.error(null, e);
        }
        return null;
    }


    public <T> T getData(String fileName, Class<T> type) {
        try {
            log.debug("Reading data from {} for {}", fileName, type.getSimpleName());
            File file = getOBNDataFile(fileName);
            if (file.exists()) {

                return objectMapper.readValue(file, type);


            } else {
                log.error("File not found for {} from {}", type, fileName);
            }
        } catch (Exception e) {
            log.warn("Cannot convert json data for {} using file {}", type, fileName);
            log.error(null, e);
        }
        return null;
    }


    @PostConstruct
    protected void init() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
