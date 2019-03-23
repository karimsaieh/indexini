import tika
from tika import parser


class Parser:
    def __init__(self, ):
        # tika.initVM()
        None

    def parse_file(self, binary):
        parsed = parser.from_buffer(binary, "http://localhost:9998/tika")
        content_type = parsed["metadata"]["Content-Type"]
        content_type = content_type[1] if isinstance(content_type, list) else content_type
        # TODO: what to do in elastic search & in the ui when the index is empty ?
        #  Answer: it disappear forever ... what to do then ?
        content = parsed["content"] if parsed["content"] is not None else ""
        return content, content_type


