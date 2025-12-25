package com.scu.service.impl;

import com.scu.entity.FileMetaData;
import com.scu.service.FileMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class FileMetaDataServiceImpl implements FileMetaDataService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public FileMetaData findByFileHash(String fileHash) {
        Query query = Query.query(Criteria.where("fileHash").is(fileHash));
        return mongoTemplate.findOne(query, FileMetaData.class);
    }

    @Override
    public void save(FileMetaData fileMetaData) {
        mongoTemplate.save(fileMetaData);
    }
}
