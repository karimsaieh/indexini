import ftplib
from urllib.parse import urlsplit
from utils import change_file_name
from constants import NotificationConstants
from socket import timeout
import LogstashLogger


# TODO: SAME FILE NAME || keep directory as it is
class FtpExplorer:
    def __init__(self, publish_method, msg):
        self.publish_method = publish_method
        self.file_types = tuple(msg["fileTypes"])
        self.depth = msg["depth"]
        self.url = msg["url"]
        self.bulk_save_operation_timestamp = msg["bulkSaveOperationTimestamp"]
        self.bulk_save_operation_uuid = msg["bulkSaveOperationUuid"]
        self.source = msg["source"]
        self.hostname = "{0.netloc}".format(urlsplit(self.url))
        self.ftp = None

    def traverse(self, depth, path, files_found):
        LogstashLogger.info("IN: " + path + " . " + str(depth))
        if depth == 0:
            return None
        # LogstashLogger.info('y')
        # self.ftp.connect()
        # LogstashLogger.info('y')
        # self.ftp.login()
        # LogstashLogger.info('y')
        # self.ftp.set_pasv(True)
        # LogstashLogger.info('y')
        # self.browse_to_ftp_dir_and_get_path(path)
        LogstashLogger.info('y')
        # nlst hangs often :/, solution => right below
        success = False
        while not success:
            try:
                path_generator = self.ftp.nlst()
                success = True
            except timeout as e:
                LogstashLogger.info(str(e) + "----" + str(type(e)))
                self.ftp = ftplib.FTP(self.hostname, timeout=10)
                self.ftp.connect(timeout=10)
                self.ftp.login()
                self.ftp.set_pasv(True)
                self.browse_to_ftp_dir_and_get_path(path)

        LogstashLogger.info('y')
        paths = (path for path in path_generator if path not in ('.', '..'))
        LogstashLogger.info(paths)
        for entry in paths:
            LogstashLogger.info(entry)
            try:
                # LogstashLogger.info(path, entry, depth)
                self.ftp.cwd(entry)
                # LogstashLogger.info("DIRECTORY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " + "ftp://" + self.hostname + path+"/" + entry)
                self.traverse(depth - 1, path + "/" + entry, files_found)
                self.ftp.cwd("..")
            except ftplib.error_perm:
                # LogstashLogger.info(" ------- ")
                # LogstashLogger.info("FILE ---------6> " + "ftp://" + self.hostname + path+"/" + entry)
                if entry.endswith(self.file_types):
                    file_url = "ftp://" + self.hostname + path+"/" + entry
                    if file_url not in files_found.values():
                        new_file_name = change_file_name(entry, files_found)
                        files_found[new_file_name] = file_url
                        LogstashLogger.info("file found "+ file_url)
                        payload = {
                            "name": new_file_name,
                            "url": file_url,
                            "bulkSaveOperationTimestamp": self.bulk_save_operation_timestamp,
                            "bulkSaveOperationUuid": self.bulk_save_operation_uuid,
                            "source": self.source
                        }
                        notification_payload = {
                            "event": NotificationConstants.FILE_FOUND,
                            "fileUrl": file_url,
                            "fileName": new_file_name
                        }
                        self.publish_method(payload, notification_payload)

    def start(self):
        try:
            self.ftp = ftplib.FTP(self.hostname, timeout=10)
            self.ftp.connect(timeout=10)
            self.ftp.login()
            self.ftp.set_pasv(True)
            current_path = self.browse_to_ftp_dir_and_get_path(self.url)
            # self.depth = 300000000
            self.traverse(self.depth, current_path, {})
            LogstashLogger.info("done")
        except Exception as e:
            LogstashLogger.info("karim: failed to connect to ftp server , ftpexplorer.start " + str(type(e)) + str(e))

    def browse_to_ftp_dir_and_get_path(self, url):
        directory = "{0.path}".format(urlsplit(url)).split("/")
        current_path = ""
        for sub_dir in directory:
            if sub_dir:
                try:
                    self.ftp.cwd(sub_dir)
                    current_path = current_path + "/" + sub_dir
                except ftplib.error_perm:
                    LogstashLogger.info("errooor in ftp explorer . start in ftp.cwd")
        return current_path
