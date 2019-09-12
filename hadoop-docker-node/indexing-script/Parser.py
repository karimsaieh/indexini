from tika import parser
import os

class Parser:
    def __init__(self, ):
        # tika.initVM()
        None

    def parse_file(self, binary):
        try:
            parsed = parser.from_buffer(binary, "http://" + os.environ["pfe_tika_host"] + ":9998/tika")
            content_type = parsed["metadata"]["Content-Type"]
            content_type = content_type[1] if isinstance(content_type, list) else content_type
            # TODO: what to do in elastic search & in the ui when the index is empty ?
            #  Answer: it disappear forever ... what to do then ?
            content = parsed["content"] if parsed["content"] is not None else ""
        except Exception as e:
            content = ""
            content_type = "UNKOWN"
            print("exception in parser.py" + str(e))
        return content, content_type


