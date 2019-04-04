from LdaTopicsDescriptionProducer import LdaTopicsDescriptionProducer
import json


class LdaTopicsDescriptionRepository:

    def save_lda_topics(self, topics_descriptions):
        lda_topics_description_producer = LdaTopicsDescriptionProducer()
        payload = []
        for index, topic in enumerate(topics_descriptions):
            print(index, topic)
            topic_dic = {}
            topic_dic["id"] = index
            topic_dic["description"] = topics_descriptions[index]
            payload.append(topic_dic)
        lda_topics_description_producer.publish(json.dumps(payload))
        lda_topics_description_producer.close_connection()


