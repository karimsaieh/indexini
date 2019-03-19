from pathlib import Path


class FileProcessor:

    def get_timestamp_and_uuid(self, file):
        url = Path(file[0])
        url_parts = url.parts
        file_name = url_parts[-1]
        uuid = url_parts[-2]
        timestamp = url_parts[-3]
        return file[0], file_name, uuid, timestamp, file[1], file[2], file[3], file[4], file[5], file[6],
