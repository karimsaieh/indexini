package tn.insat.pfe.filemanagementservice.clients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface IFileHdfsClient {
    void addFile(InputStream fileInputStream,String fileName,  String bulkSaveOperationTimestamp, String bulkSaveOperationUuid) throws IOException;
    InputStream readFile(String url) throws IOException;
    void deleteFile(String file);
    void mkdir(String dir);
}
