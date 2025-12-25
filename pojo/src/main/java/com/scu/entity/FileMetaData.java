package com.scu.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fileMetaData")
@Data
public class FileMetaData {

    @Id
    private String id;

    private String fileMongoId;
    private String fileHash;
}
