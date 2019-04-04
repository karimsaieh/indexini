from FileIndexProducer import FileIndexProducer
import json


class FileIndexRepository:

    def save_df(self, df):
        df.foreachPartition(lambda p: self.save_partition(p))

    def save_partition(self, partition):
        file_index_producer = FileIndexProducer()
        for file_index in partition:
            print("saving to ES" + file_index["url"])
            file_index = file_index.asDict()
            payload = {
                "id": file_index["url"],
                "bulkSaveOperationTimestamp": file_index["timestamp"],
                "bulkSaveOperationUuid": file_index["uuid"],
                "fileName": file_index["file_name"],
                "text": file_index["pre_processed_text"],
                "summary": file_index["summary"],
                "mostCommonWords": file_index["most_common"],
                "contentType": file_index["content_type"],
                "kmeansPrediction": file_index["kmeans_prediction"],
                "bisectingKmeansPrediction": file_index["bisecting_kmeans_prediction"],
                "thumbnail": file_index["thumbnail"],
            }

            ldaTopics = {}
            for index, topic_value in enumerate(file_index["lda_topics"].values.tolist()):
                ldaTopics[index] = topic_value
            payload["ldaTopics"] = ldaTopics
            file_index_producer.publish(json.dumps(payload))

        file_index_producer.close_connection()
