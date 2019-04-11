package tn.insat.pfe.filemanagementservice.clients;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.conf.IHdfsProvider;

import javax.annotation.PostConstruct;
import java.io.*;

@Component
public class FileHdfsClient implements IFileHdfsClient{
    private static final Logger logger = LoggerFactory.getLogger(FileHdfsClient.class);
    private String logMsg;

    private IHdfsProvider hdfsProvider;
    private FileSystem fileSystem;
    @Autowired
    public FileHdfsClient(IHdfsProvider hdfsProvider) {
        this.hdfsProvider = hdfsProvider;
    }

    @PostConstruct
    public void init() throws IOException {
        Configuration conf  = this.hdfsProvider.getConf();
        this.fileSystem = FileSystem.get(conf);
    }

    @Override
    public void addFile(String directoryUrl, String fileName,InputStream fileInputStream) throws IOException {
        //create save directory if it doesn't exist
        Path directoryPath = new Path(directoryUrl);
        if (!this.fileSystem.exists(directoryPath)) {
            this.mkdir(this.hdfsProvider.getSaveDirectory());
        }
        try(FSDataOutputStream out = this.fileSystem.create(new Path(directoryUrl + "/" +fileName))) {
            InputStream in = new BufferedInputStream(fileInputStream);
            byte[] b = new byte[1024];
            int numBytes = 0;
            while ((numBytes = in.read(b)) > 0) {
                out.write(b, 0, numBytes);
            }
            in.close();
        }
    }

    @Override
    public InputStream readFile(String url) throws IOException {
        Path path = new Path(url);
        if (!this.fileSystem.exists(path)) {
            this.logMsg = "File " + url + " does not exists";
            logger.info(this.logMsg);
            return null;
        }

        try(FSDataInputStream in = this.fileSystem.open(path)) {
            return in.getWrappedStream();
        }
    }

    @Override
    public void delete(String url) throws IOException {
        Path path = new Path(url);
        if (!fileSystem.exists(path)) {
            this.logMsg = "File or Folder" + url + " does not exists";
            logger.info(this.logMsg);
            return;
        }
        this.fileSystem.delete(new Path(url), true);
    }

    @Override
    public void mkdir(String dir) {
        //this method is used in addFile to create a directory XD
        logger.info("---------------- mkdir");
    }
}
