<template>
  <div class="fixed-width-page-parent-container">
    <div class="fixed-width-page-container">
      <div v-loading="loading">

        <file-search-card
          :id="file.id"
          :words="file.mostCommonWords"
          :lda-topics="file.ldaTopics"
          :lda-topics-description="ldaTopicsDescription"
          :summary="file.summary"
          :name="file.fileName"
          :highlight-list="file.highlightList"
          :bulk-save-operation-timestamp="file.bulkSaveOperationTimestamp"
          :content-type="file.contentType"
          :thumbnail="file.thumbnail"
          :bisecting-kmeans-prediction="+file.bisectingKmeansPrediction"
        />

      </div>
    </div>
  </div>
</template>

<script>

import { find } from '@/api/search'
import FileSearchCard from '@/components/File/FileSearchCard'
import { getLdaTopics } from '@/api/search'

export default {
  components: {
    FileSearchCard
  },
  data() {
    return {
      file: {},
      loading: false,
      ldaTopicsDescription: []
    }
  },
  created() {
    const query = {
      id: this.$route.query.id
    }
    this.loading = true
    getLdaTopics().then((result) => {
      this.ldaTopicsDescription = result.data
      find(query).then((result) => {
        this.file = result.data
        this.loading = false
      })
    })
  }
}
</script>

<style lang="scss" scoped>

</style>

