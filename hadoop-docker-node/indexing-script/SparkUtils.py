from pyspark import SparkContext
from pyspark.sql import SQLContext
from pyspark.sql.functions import col
import os


class SparkUtils:
    def __init__(self, master, app_name):
        if os.environ["pfe_env"] != "dev":
            self.sc = SparkContext(appName=app_name)
            self.sc.addFile('/FileProcessor.py')
            self.sc.addFile('/FileIndexProducer.py')
            self.sc.addFile('/FileIndexRepository.py')
            self.sc.addFile('/FileUrlProcessor.py')
            self.sc.addFile('/LdaTopicsDescriptionProducer.py')
            self.sc.addFile('/LdaTopicsDescriptionRepository.py')
            self.sc.addFile('/Parser.py')
            self.sc.addFile('/SparkProcessor.py')
            self.sc.addFile('/SparkUtils.py')
            self.sc.addFile('/TextMostCommonWordsExtractor.py')
            self.sc.addFile('/TextPreProcessor.py')
            self.sc.addFile('/TextSummarizer.py')
            self.sc.addFile('/thumbnail_temp.py')
            self.sc.addFile('/ThumbnailGenerator.py')
            self.sc.addFile('/NotificationConstants.py')
            self.sc.addFile('/RabbitMqConstants.py')
        else:
            self.sc = SparkContext(master=master, appName=app_name)
        self.sql_context = SQLContext(self.sc)

    # output rdd:(url, b'content")
    def read_files(self, path):
        return self.sc.binaryFiles(path)

    def rdd_to_df(self, rdd, schema):
        df = self.sql_context.createDataFrame(rdd, schema)
        return df

    def join_df(self, df0, df1, join_col, df0_selected_cols, df1_selected_cols):
        df0_selected_cols = ["df0."+x for x in df0_selected_cols]
        df1_selected_cols = ["df1."+x for x in df1_selected_cols]
        df0 = df0.alias('df0')
        df1 = df1.alias('df1')
        df = df0.join(df1, col("df0."+join_col) == col("df1."+join_col))\
            .select(df0_selected_cols + df1_selected_cols)
        return df
