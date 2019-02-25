from bs4 import BeautifulSoup
import requests
from urllib.parse import urlparse
import os


class WebScraper:
    def __init__(self, publish_method, file_types, depth, page_url, bulk_save_operation_timestamp, bulk_save_operation_uuid):
        self.publish_method = publish_method
        self.file_types = file_types
        self.depth = depth
        self.page_url = page_url
        self.bulk_save_operation_timestamp = bulk_save_operation_timestamp
        self.bulk_save_operation_uuid = bulk_save_operation_uuid

    def change_file_name(self, file_name, files):
        new_file_name = file_name
        file_name_without_extension, file_extension = os.path.splitext(file_name)
        if new_file_name in files:
            new_file_name = file_name_without_extension + " (1)" + file_extension
        i = 2
        while new_file_name in files:
            new_file_name = file_name_without_extension + " (" + str(i) + ")" + file_extension
            i += 1
        return new_file_name

    def find_files(self,root_page_url, page_url, depth, file_types, visited_pages_urls, files_found):
        if depth == 0:
                return None
        try:
            parsed_uri = urlparse(page_url)
            domain_name = "{uri.netloc}".format(uri=parsed_uri)
            if (page_url.startswith("https://") or page_url.startswith("http://")) and domain_name in "{uri.netloc}".format(uri=urlparse(root_page_url)):
                html_page = requests.get(page_url).content
                soup = BeautifulSoup(html_page, "lxml", from_encoding="iso-8859-15")
                for link in soup.findAll("a", href=True):
                    link_string = link.get("href")
                    if (link_string.startswith("https://") or link_string.startswith("http://")) and domain_name in "{uri.netloc}".format(uri=urlparse(link_string)):
                        if link_string.endswith(file_types):
                            file_name = os.path.basename(urlparse(link_string).path)
                            if link_string not in files_found.values():
                                new_file_name = self.change_file_name(file_name, files_found)
                                files_found[new_file_name] = link_string
                                print("found file: " + new_file_name + " --> " + link_string)
                                payload = {"fileName": new_file_name, "url": link_string}
                                self.publish_method(payload,payload)
                        elif link_string not in visited_pages_urls:
                            visited_pages_urls.append(link_string)
                            self.find_files(root_page_url, link_string, depth-1, file_types, visited_pages_urls, files_found)
        except Exception as x:
            print(x)

    def start(self):
        visited_pages_urls = [self.page_url]
        files_found = {}
        ext = os.path.splitext(os.path.basename(urlparse(self.page_url).path))
        root_page_url = self.page_url
        if ext[1] in ["", ".htm", ".html", ".php", ".asp", ".aspx", ".jsp", ".xhtml"]:
            self.find_files(root_page_url, self.page_url, self.depth, self.file_types, visited_pages_urls, files_found)
