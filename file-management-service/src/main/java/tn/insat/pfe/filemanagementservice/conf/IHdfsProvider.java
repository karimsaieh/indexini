package tn.insat.pfe.filemanagementservice.conf;

import org.apache.hadoop.conf.Configuration;

public interface IHdfsProvider {
    Configuration getConf();
    String getSaveDirectory();
}
