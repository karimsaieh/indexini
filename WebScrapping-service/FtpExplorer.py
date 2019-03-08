import ftplib
from urllib.parse import urlsplit


# TODO: SAME FILE NAME || keep directory as it is
class FtpExplorer:
    def __init__(self, publish_method, file_types, depth, url, bulk_save_operation_timestamp, bulk_save_operation_uuid):
        self.publish_method = publish_method
        self.file_types = file_types
        self.depth = depth
        self.url = url
        self.bulk_save_operation_timestamp = bulk_save_operation_timestamp
        self.bulk_save_operation_uuid = bulk_save_operation_uuid
        self.routing_key = self.bulk_save_operation_timestamp + "-" + self.bulk_save_operation_uuid
        self.hostname = "{0.netloc}".format(urlsplit(self.url))

    def traverse(self, ftp, depth, path):
        if depth == 0:
            return None
        paths = (path for path in ftp.nlst() if path not in ('.', '..'))
        for entry in paths:
            try:
                # print(path, entry, depth)
                ftp.cwd(entry)
                self.traverse(ftp, depth - 1, path + "/" + entry)
                ftp.cwd("..")
            except ftplib.error_perm:
                # print(" ------- ")
                if entry.endswith(self.file_types):
                    file_url = "ftp://" + self.hostname + path+"/" + entry
                    print("file found", file_url)
                    payload = {"fileName": entry, "url": file_url}
                    self.publish_method(self.routing_key, payload, payload)

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
            self.traverse(ftp, self.depth, current_path)
        except:
            print("failed to connect to ftp server , ftpexplorer.start")

# ftpppp =FtpExplorer(None, (".pdf",".doc",".zip",".tar.gz"), 1, "ftp://echanges.dila.gouv.fr/AMF/041/2014/02/ezre", "jjj", "jj")
# ftpppp.start()