package com.scu;

import com.scu.util.GridFsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class TestMongo {

    @Autowired
    GridFsUtils gridFsUtils;


    @Test
    public void test() throws IOException {
//        String id = gridFsUtils.upload("test.png", new FileInputStream("E:\\MyMindHive\\MyProjects\\MaterialSCU\\MaterialPlatform\\server\\src\\test\\java\\com\\scu\\img.png"));
      String id="694bb173a21e8a3ae1c46157";
//        InputStream download = gridFsUtils.download(id);
        InputStream download=gridFsUtils.downloadById( id);
        // 指定本地保存路径
        Path targetPath = Paths.get("./test_downloaded.png");

// 将 InputStream 写入文件
        try {
            Files.copy(download, targetPath);
            System.out.println("文件已保存到: " + targetPath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流（重要！）
            try {
                if (download != null) download.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




}
