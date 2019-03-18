from pdf2image import convert_from_bytes
import io
import PyPDF2
import base64


class ThumbnailGenerator:

    def get_thumbnail(self, file):
        content_type = file[3]
        base64_string = ""
        file_binary = file[1]
        if content_type == "application/pdf":
            file_binary_stream = io.BytesIO(file_binary)
            src_pdf = PyPDF2.PdfFileReader(file_binary_stream)
            dst_pdf = PyPDF2.PdfFileWriter()
            dst_pdf.addPage(src_pdf.getPage(0))
            pdf_bytes = io.BytesIO()
            dst_pdf.write(pdf_bytes)
            pdf_bytes.seek(0)
            # dpi = 100 to reduce image size (& quality)
            images = convert_from_bytes(pdf_bytes.read(),  dpi=100)
            im_bytes = io.BytesIO()
            images[0].save(im_bytes, format='PNG')
            base64_string = base64.b64encode(im_bytes.getvalue()).decode()
        elif content_type == "image/jpeg":
            # TODO: reduce image size jpeg
            base64_string = base64.b64encode(file_binary).decode()
        elif content_type == "image/png":
            # TODO: reduce image size png
            base64_string = base64.b64encode(file_binary).decode()
        return file[0], file[2], base64_string, file[3] # returns url, content, thumbnail, content_type
        # value = "<img src=data:image/png;base64," + base64.b64encode(im_bytes.getvalue()).decode() + "></img>"
        # with open("i.html", "w") as text_file:
        #     print("{}".format(value), file=text_file)
