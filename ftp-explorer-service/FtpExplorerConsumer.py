import os
import pika
from constants import RabbitMqConstants


class FtpExplorerConsumer:

    def __init__(self, callback):
        self.callback = callback
        # disabling heartbeat: https://stackoverflow.com/questions/14572020/handling-long-running-tasks-in-pika-rabbitmq
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["pfe_rabbitmq_host"],
                                                                            heartbeat_interval=0))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue=RabbitMqConstants.FTP_EXPLORER_QUEUE)
        self.channel.basic_consume(self.callback, queue=RabbitMqConstants.FTP_EXPLORER_QUEUE, no_ack=True)
        self.channel.start_consuming()
