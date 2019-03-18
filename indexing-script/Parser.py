import tika
from tika import parser


class Parser:
    def __init__(self, ):
        # tika.initVM()
        None

    def parse_file(self, file):
        # file[0] = url, file[1]= b'content"
        print("file path -> " + file[0])
        parsed = parser.from_buffer(file[1], "http://localhost:9998/tika")
        content_type = parsed["metadata"]["Content-Type"]
        content_type = content_type[1] if isinstance(content_type, list) else content_type
        # TODO: what to do in elastic search & in the ui when the index is empty ?
        #  does it appear in every request ?
        #  or does it disappear forever ?
        content = parsed["content"] if parsed["content"] is not None else ""
        return file[0], file[1], content, content_type


