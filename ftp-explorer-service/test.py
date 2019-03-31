import ftplib
ftp = ftplib.FTP("echanges.dila.gouv.fr")
ftp.connect()
ftp.login()
ftp.cwd("AMF")
ftp.cwd("936")
ftp.connect()
ftp.login()
ftp.set_pasv(True)
while 1:
    paths = (path for path in ftp.nlst() if path not in ('.', '..'))
    print(paths)
