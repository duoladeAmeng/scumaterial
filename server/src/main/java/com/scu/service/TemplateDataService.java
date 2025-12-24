package com.scu.service;

import com.scu.dto.TemplateDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TemplateDataService {


    void saveTemplateDataSingle(String data, List<MultipartFile> files);

    void saveTemplateDataBatch(MultipartFile excel, List<MultipartFile> files, Long templateId);
}
