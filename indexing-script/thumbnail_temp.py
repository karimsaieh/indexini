from pdf2image import convert_from_bytes
import io
import PyPDF2
import base64
#
# file_binary = open("data/a.pdf", "rb").read()
# file_binary_stream = io.BytesIO(file_binary)
# src_pdf = PyPDF2.PdfFileReader(file_binary_stream)
# dst_pdf = PyPDF2.PdfFileWriter()
# dst_pdf.addPage(src_pdf.getPage(0))
# pdf_bytes = io.BytesIO()
# dst_pdf.write(pdf_bytes)
# pdf_bytes.seek(0)
# images = convert_from_bytes(pdf_bytes.read(), dpi=100)
# im_bytes = io.BytesIO()
# images[0].save(im_bytes, format='JPEG')
# base64_string = base64.b64encode(im_bytes.getvalue()).decode()

# from PIL import Image
#
# im = Image.open("data/a.png")
# im_bytes = io.BytesIO()
# im.save( im_bytes,  format="PNG", quality=200,optimize=True)
# base64_string = base64.b64encode(im_bytes.getvalue()).decode()
#
# value = "<img src=data:image/png;base64," + base64.b64encode(im_bytes.getvalue()).decode() + "></img>"
# with open("j.html", "w") as text_file:
#     print("{}".format(value), file=text_file)

file_binary = open("data/a.jpg", "rb").read()
base64_string = base64.b64encode(file_binary).decode()
value = "<img src=data:image/png;base64," + base64.b64encode(file_binary).decode() + "></img>"
with open("j.html", "w") as text_file:
    print("{}".format(value), file=text_file)