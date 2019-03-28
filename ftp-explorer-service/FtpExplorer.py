import ftplib
from urllib.parse import urlsplit
from utils import change_file_name
from constants import NotificationConstants


# TODO: SAME FILE NAME || keep directory as it is
class FtpExplorer:
    def __init__(self, publish_method, file_types, depth, url, bulk_save_operation_timestamp, bulk_save_operation_uuid):
        self.publish_method = publish_method
        self.file_types = file_types
        self.depth = depth
        self.url = url
        self.bulk_save_operation_timestamp = bulk_save_operation_timestamp
        self.bulk_save_operation_uuid = bulk_save_operation_uuid
        self.hostname = "{0.netloc}".format(urlsplit(self.url))

    def traverse(self, ftp, depth, path, files_found):
        if depth == 0:
            return None
        paths = (path for path in ftp.nlst() if path not in ('.', '..'))
        for entry in paths:
            try:
                # print(path, entry, depth)
                ftp.cwd(entry)
                self.traverse(ftp, depth - 1, path + "/" + entry, files_found)
                ftp.cwd("..")
            except ftplib.error_perm:
                # print(" ------- ")
                if entry.endswith(self.file_types):
                    file_url = "ftp://" + self.hostname + path+"/" + entry
                    if file_url not in files_found.values():
                        new_file_name = change_file_name(entry, files_found)
                        files_found[new_file_name] = file_url
                        print("file found", file_url)
                        payload = {
                            "name": new_file_name,
                            "url": file_url,
                            "bulkSaveOperationTimestamp": self.bulk_save_operation_timestamp,
                            "bulkSaveOperationUuid": self.bulk_save_operation_uuid
                        }
                        notification_payload = {
                            "event": NotificationConstants.FILE_FOUND,
                            "fileUrl": file_url,
                            "fileName": new_file_name
                        }
                        self.publish_method(payload, notification_payload)

    def start(self):
        try:
            ftp = ftplib.FTP(self.hostname)
            ftp.connect()
            ftp.login()
            ftp.set_pasv(True)
            directory = "{0.path}".format(urlsplit(self.url)).split("/")
            current_path = ""
            for sub_dir in directory:
                if sub_dir:
                    try:
                        ftp.cwd(sub_dir)
                        current_path = current_path + "/" + sub_dir
                    except ftplib.error_perm:
                        print("errooor in ftp explorer . start in ftp.cwd")
            self.traverse(ftp, self.depth, current_path, {})
        except:
            print("failed to connect to ftp server , ftpexplorer.start")
