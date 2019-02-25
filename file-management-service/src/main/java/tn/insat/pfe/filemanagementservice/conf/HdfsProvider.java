package tn.insat.pfe.filemanagementservice.conf;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class HdfsProvider implements  IHdfsProvider{
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
    public Configuration getConf() {
        return this.conf;
    }

//    @Override
//    public String getHdfsPath() {
//        return this.hdfsPath;
//    }

    @Override
    public String getSaveDirectory() {
        return this.saveDirectory;
    }
}
