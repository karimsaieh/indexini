import logging
from logging import StreamHandler
import logstash
import os

host = os.environ['pfe_logstash_host']

test_logger = logging.getLogger('python-logstash-logger')
test_logger.setLevel(logging.INFO)
test_logger.addHandler(logstash.TCPLogstashHandler(host, 5000, version=1))
test_logger.addHandler(StreamHandler())


def info(msg, extra={}):
    extra['serviceName'] = 'web-scraping-service'
    test_logger.info(msg, extra=extra)


def warning(msg, extra={}):
    extra['serviceName'] = 'web-scraping-service'
    test_logger.warning(msg, extra=extra)


def error(msg, extra={}):
    extra['serviceName'] = 'web-scraping-service'
    test_logger.error(msg, extra=extra)

