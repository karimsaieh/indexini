package tn.insat.pfe.filemanagementservice.clients;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.*;

@Component
public class FileHdfsClient implements IFileHdfsClient{
    @Value("${hdfs.path}")
    private String hdfsPath;
    @Value("${hdfs.save-directory}")
    private String saveDirectory;
    private Configuration conf;
    @PostConstruct
    public void init() {
        this.conf = new Configuration();
        this.conf.set("fs.default.name", this.hdfsPath);
    }

    @Override
    public void addFile(InputStream fileInputStream, String fileName) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        //create save directory if it doesn't exist
        Path saveDirectoryPath = new Path(this.saveDirectory);
        if (fileSystem.exists(saveDirectoryPath)) {
            this.mkdir(this.saveDirectory);
        }
        FSDataOutputStream out = fileSystem.create(new Path(this.saveDirectory + fileName));
        InputStream in = new BufferedInputStream(fileInputStream);
        byte[] b = new byte[1024];
        int numBytes = 0;
        while ((numBytes = in.read(b)) > 0) {
            out.write(b, 0, numBytes);
        }
        in.close();
        out.close();
        fileSystem.close();
    }

    @Override
    public void readFile(String file) {
        System.out.println("--------------- read");
    }

    @Override
    public void deleteFile(String file) {
        System.out.println("--------------- delete");
    }

    @Override
    public void mkdir(String dir) {
        System.out.println("--------------- mkdir");
    }
}
