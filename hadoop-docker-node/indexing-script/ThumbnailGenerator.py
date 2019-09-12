from pdf2image import convert_from_bytes
import io
import PyPDF2
import base64


class ThumbnailGenerator:

    def get_thumbnail(self, binary, content_type):
        base64_string = ""
        try:
            if content_type == "application/pdf":
                file_binary_stream = io.BytesIO(binary)
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
                base64_string = "data:image/png;base64," + base64.b64encode(im_bytes.getvalue()).decode()
            elif content_type == "image/jpeg":
                # TODO: reduce image size jpeg
                base64_string = base64.b64encode(binary).decode()
            elif content_type == "image/png":
                # TODO: reduce image size png
                base64_string = base64.b64encode(binary).decode()
            # base64_string = "dummy thumbnail, set in thumbnail generator in spark script"
        except Exception as e:
            print("an exception in my thumbnail generator scrip", str(e))
            base64_string = ""
        return base64_string
        # value = "<img src=data:image/png;base64," + base64.b64encode(im_bytes.getvalue()).decode() + "></img>"
        # with open("i.html", "w") as text_file:
        #     print("{}".format(value), file=text_file)
