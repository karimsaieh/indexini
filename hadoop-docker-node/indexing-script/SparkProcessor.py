from pyspark.ml.feature import HashingTF, IDF, CountVectorizer
from pyspark.ml.clustering import KMeans, LDA, BisectingKMeans
import math


class SparkProcessor:

    def calculate_hashingtf_idf(self, files_df):
        hashing_tf = HashingTF(inputCol="words", outputCol="rawFeatures", numFeatures=262144)
        featurized_data = hashing_tf.transform(files_df)
        idf = IDF(inputCol="rawFeatures", outputCol="features")
        idf_model = idf.fit(featurized_data)
        rescaled_data = idf_model.transform(featurized_data)
        return rescaled_data

    def calculate_count_vectorize_idf(self, df):
        count_vec = CountVectorizer(inputCol="words", outputCol="rawFeatures", vocabSize=262144)
        cv_model = count_vec.fit(df)
        featurized_data = cv_model.transform(df)
        vocab = cv_model.vocabulary
        idf = IDF(inputCol="rawFeatures", outputCol="features")
        idf_model = idf.fit(featurized_data)
        rescaled_data = idf_model.transform(featurized_data)
        return rescaled_data, vocab

    def do_bisecting_kmeans(self, rescaled_data):
        k = max(2, math.ceil(math.sqrt(rescaled_data.count()/2)))
        bisecting_kmeans = BisectingKMeans().setK(k).setSeed(1).setFeaturesCol("features")
        bisecting_kmeans_model = bisecting_kmeans.fit(rescaled_data)
        transformed_df = bisecting_kmeans_model.transform(rescaled_data).select("url", "prediction")
        return transformed_df

    def do_kmeans(self, rescaled_data):
        k = max(2, math.ceil(math.sqrt(rescaled_data.count()/2)))
        bisecting_kmeans = KMeans().setK(k).setSeed(1).setFeaturesCol("features")
        bisecting_kmeans_model = bisecting_kmeans.fit(rescaled_data)
        transformed_df = bisecting_kmeans_model.transform(rescaled_data).select("url", "prediction")
        return transformed_df

    def do_lda_with_hashingtf(self, rescaled_data):
        k = max(2, math.ceil(math.sqrt(rescaled_data.count()/2)))
        lda = LDA(k=k, seed=1, featuresCol="features")
        model = lda.fit(rescaled_data)
        transformed_df = model.transform(rescaled_data).select("url", "topicDistribution")
        return transformed_df

    def do_lda_with_count_vectorizer(self, rescaled_data, vocab):
        k = max(2, math.ceil(math.sqrt(rescaled_data.count()/2)))
        lda = LDA(k=k, seed=1, optimizer="em", featuresCol="features")
        lda_model = lda.fit(rescaled_data)
        transformed_df = lda_model.transform(rescaled_data).select("url", "topicDistribution")
        topics_description = lda_model.describeTopics().rdd\
            .map(lambda row: row['termIndices'])\
            .map(lambda idx_list: [vocab[idx] for idx in idx_list]) \
            .collect()
        return transformed_df, topics_description

    def process(self, df):
        rescaled_data_with_hashingtf_idf = self.calculate_hashingtf_idf(df)
        rescaled_data_with_count_vectorize_idf, vocab = self.calculate_count_vectorize_idf(df)

        kmeans_df = self.do_kmeans(rescaled_data_with_hashingtf_idf)
        bisecting_kmeans_df = self.do_bisecting_kmeans(rescaled_data_with_hashingtf_idf)
        lda_with_count_vectorizer_df, topics_descriptions = self.do_lda_with_count_vectorizer(rescaled_data_with_count_vectorize_idf, vocab)

        kmeans_df = kmeans_df.withColumnRenamed("prediction", "kmeans_prediction")
        bisecting_kmeans_df = bisecting_kmeans_df.withColumnRenamed("prediction", "bisecting_kmeans_prediction")
        lda_with_count_vectorizer_df = lda_with_count_vectorizer_df.withColumnRenamed("topicDistribution", "lda_topics")

        return kmeans_df, bisecting_kmeans_df, (lda_with_count_vectorizer_df, topics_descriptions)



