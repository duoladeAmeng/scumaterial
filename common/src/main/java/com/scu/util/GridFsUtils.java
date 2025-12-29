package com.scu.util;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GridFsUtils {

    private final MongoTemplate mongoTemplate;

    public GridFsUtils(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 获取 GridFSBucket（默认 bucket 名为 'fs'）
     */
    private  GridFSBucket getBucket() {
        return GridFSBuckets.create(mongoTemplate.getDb());
    }

    /**
     * 上传文件（InputStream → GridFS）
     * @return 文件 ObjectId 字符串
     */
    public String upload(String filename, InputStream inputStream) throws IOException {
        ObjectId id = getBucket().uploadFromStream(filename, inputStream);
        return id.toHexString();
    }

    /**
     * 下载文件（通过文件名）
     */
    public InputStream download(String filename) throws IOException {
        var file = getBucket().find(com.mongodb.client.model.Filters.eq("filename", filename)).first();
        if (file == null) {
            throw new IOException("File not found: " + filename);
        }
        return getBucket().openDownloadStream(file.getObjectId());
    }

    /**
     * 下载文件（通过 ObjectId 字符串）
     */
    public  InputStream downloadById(String id) throws IOException {
        return getBucket().openDownloadStream(new ObjectId(id));
    }

    /**
     * 将文件流写入 OutputStream（常用于 Controller 返回文件）
     */
    public void writeToOutputStream(String filename, OutputStream out) throws IOException {
        try (InputStream in = download(filename)) {
            in.transferTo(out);
        }
    }

    /**
     * 删除文件（按文件名）
     */
    public void delete(String filename) {
        getBucket().find(com.mongodb.client.model.Filters.eq("filename", filename))
                .forEach(f -> getBucket().delete(f.getObjectId()));
    }

    /**
     * 删除文件（按 ID）
     */
    public void deleteById(String id) {
        getBucket().delete(new ObjectId(id));
    }


    /**
     * 根据 ObjectId 获取 GridFS 文件的元数据（包括 filename）
     */
    public GridFSFile getFileMetadataById(String id) {
        ObjectId objectId = new ObjectId(id);
        return getBucket().find(com.mongodb.client.model.Filters.eq("_id", objectId)).first();
    }
}