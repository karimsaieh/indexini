from FileIndexProducer import FileIndexProducer


class FileIndexRepository:

    def __init__(self):
        file_index_producer = FileIndexProducer()

    def save_df(self, df):
        df.foreachPartition(lambda p: self.save_partition(p))

    def save_partition(self, partition):
        for file_index in partition:
            print(file_index)
