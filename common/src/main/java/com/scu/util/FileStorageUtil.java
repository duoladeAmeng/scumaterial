package com.scu.util;

// FileStorageService.java


import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileStorageUtil {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    /**
     * 保存文件到 GridFS
     *
     * @param file 文件
     * @param filename 自定义文件名（可包含扩展名）
     * @return 文件 ID（ObjectId 字符串）
     */
    public String storeFile(MultipartFile file, String filename) throws IOException {
        InputStream inputStream = file.getInputStream();
        // 可以设置元数据（metadata）
        return gridFsTemplate.store(inputStream, filename, file.getContentType()).toString();
    }

    /**
     * 根据文件名读取文件
     *
     * @param filename 文件名
     * @return GridFsResource，可用于获取 InputStream
     */
    public GridFsResource getFile(String filename) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(filename)));
        if (gridFSFile == null) {
            throw new RuntimeException("File not found: " + filename);
        }
        return gridFsTemplate.getResource(gridFSFile);
    }

    /**
     * 根据 ObjectId 读取文件
     */
    public GridFsResource getFileById(String id) {
        return gridFsTemplate.getResource(id);
    }
}