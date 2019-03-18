from pyspark import SparkContext
from pyspark.sql import SQLContext
from pyspark.sql.functions import col


class SparkUtils:
    def __init__(self, master, app_name):
        # TODO: master stays local even when executing in a cluster ?
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
