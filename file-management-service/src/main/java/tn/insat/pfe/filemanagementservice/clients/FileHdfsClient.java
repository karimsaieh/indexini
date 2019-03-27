package tn.insat.pfe.filemanagementservice.clients;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.conf.IHdfsProvider;

import javax.annotation.PostConstruct;
import java.io.*;

@Component
public class FileHdfsClient implements IFileHdfsClient{

    private Configuration conf;
    private IHdfsProvider hdfsProvider;
    private FileSystem fileSystem;
    @Autowired
    public FileHdfsClient(IHdfsProvider hdfsProvider) {
        this.hdfsProvider = hdfsProvider;
    }

    @PostConstruct
    public void init() throws IOException {
        this.conf  = this.hdfsProvider.getConf();
        this.fileSystem = FileSystem.get(conf);
    }

    @Override
    public void addFile(InputStream fileInputStream, String fileName, String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException {
        //create save directory if it doesn't exist
        String saveDirectoryPathString = String.format("%s/%s/%s/", this.hdfsProvider.getSaveDirectory(), bulkSaveOperationTimestamp, bulkSaveOperationUuid);
        Path saveDirectoryPath = new Path(saveDirectoryPathString);
        if (!this.fileSystem.exists(saveDirectoryPath)) {
            this.mkdir(this.hdfsProvider.getSaveDirectory());
        }
        FSDataOutputStream out = this.fileSystem.create(new Path(saveDirectoryPathString + fileName));
        InputStream in = new BufferedInputStream(fileInputStream);
        byte[] b = new byte[1024];
        int numBytes = 0;
        while ((numBytes = in.read(b)) > 0) {
            out.write(b, 0, numBytes);
        }
        in.close();
        out.close();
    }

    @Override
    public InputStream readFile(String url) throws IOException {
        Path path = new Path(url);
        if (!this.fileSystem.exists(path)) {
            System.out.println("File " + url + " does not exists");
            return null;
        }

        FSDataInputStream in = this.fileSystem.open(path);
        return in.getWrappedStream();
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
