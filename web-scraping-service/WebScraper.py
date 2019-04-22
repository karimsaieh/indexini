from bs4 import BeautifulSoup
import requests
from urllib.parse import urlparse
from urllib.parse import urljoin
import os
from utils import change_file_name
from constants import NotificationConstants
import LogstashLogger

class WebScraper:
    def __init__(self, publish_method, msg):
        self.publish_method = publish_method
        self.file_types = tuple(msg["fileTypes"])
        self.depth = msg["depth"]
        self.page_url = msg["url"]
        self.bulk_save_operation_timestamp = msg["bulkSaveOperationTimestamp"]
        self.bulk_save_operation_uuid = msg["bulkSaveOperationUuid"]
        self.source = msg["source"]

    def find_files(self, root_page_url, page_url, depth, file_types, visited_pages_urls, files_found):

        if depth < 0:
                return None
        try:
            current_link = urljoin(root_page_url, page_url)
            parsed_uri = urlparse(current_link)
            domain_name = "{uri.netloc}".format(uri=parsed_uri)
            if domain_name in "{uri.netloc}".format(uri=urlparse(root_page_url)):
                if current_link.endswith(file_types):
                    file_name = os.path.basename(urlparse(current_link).path)
                    if current_link not in files_found.values():
                        new_file_name = change_file_name(file_name, files_found)
                        files_found[new_file_name] = current_link
                        LogstashLogger.info("found file: " + new_file_name + " --> " + current_link)
                        payload = {
                            "name": new_file_name,
                            "url": current_link,
                            "bulkSaveOperationTimestamp": self.bulk_save_operation_timestamp,
                            "bulkSaveOperationUuid": self.bulk_save_operation_uuid,
                            "source": self.source
                        }
                        notification_payload = {
                            "event": NotificationConstants.FILE_FOUND,
                            "fileUrl": current_link,
                            "fileName": new_file_name
                        }
                        self.publish_method(payload, notification_payload)
                elif current_link not in visited_pages_urls:
                        visited_pages_urls.append(current_link)
                        html_page = requests.get(current_link).content
                        soup = BeautifulSoup(html_page, "lxml", from_encoding="iso-8859-15")
                        for link in soup.findAll("a", href=True):
                            link_string = link.get("href")
                            self.find_files(root_page_url, link_string, depth-1, file_types, visited_pages_urls, files_found)

        except Exception as x:
            LogstashLogger.info(x)

    def start(self):
        visited_pages_urls = []
        files_found = {}
        ext = os.path.splitext(os.path.basename(urlparse(self.page_url).path))
        root_page_url = self.page_url
        if ext[1] in ["", ".htm", ".html", ".php", ".asp", ".aspx", ".jsp", ".xhtml"]:
            self.find_files(root_page_url, self.page_url, self.depth, self.file_types, visited_pages_urls, files_found)
