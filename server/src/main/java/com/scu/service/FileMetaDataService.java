package com.scu.service;

import com.scu.entity.FileMetaData;

import java.util.List;

public interface FileMetaDataService {
    FileMetaData findByFileHash(String fileHash);

    void save(FileMetaData fileMetaData);
}
