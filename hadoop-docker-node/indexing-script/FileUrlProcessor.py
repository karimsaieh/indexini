from pathlib import Path


class FileUrlProcessor:

    def get_timestamp_and_uuid(self, url):
        url = Path(url)
        url_parts = url.parts
        file_name = url_parts[-1]
        uuid = url_parts[-2]
        timestamp = url_parts[-3]
        return timestamp, uuid, file_name

    def normalizeUrl(self, url):
        #remove hdfs://localhost from url
        return url[16:]
