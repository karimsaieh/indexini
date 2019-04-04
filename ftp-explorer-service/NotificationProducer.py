import pika
import os
from constants import RabbitMqConstants


class NotificationProducer:
    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["RABBITMQ_HOST"],
                                                                            heartbeat_interval=0))
        self.channel = self.connection.channel()
        self.channel.exchange_declare(exchange=RabbitMqConstants.NOTIFICATIONS_EXCHANGE, exchange_type="fanout")

    def publish(self, message):
        self.channel.basic_publish(exchange=RabbitMqConstants.NOTIFICATIONS_EXCHANGE, routing_key="", body=message)

    def close_connection(self):
        self.connection.close()