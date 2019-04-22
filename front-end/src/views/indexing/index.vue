<template>
  <div>

    <div class="page-container">
      <indexing-stats
        :files="indexingStats.files"
        :not-indexed="indexingStats.notIndexed"
        :number-of-jobs="sparkstats.numberOfJobs"
        :last-job-date="sparkstats.lastJobDate"
        :current-suggestion-precision="sparkstats.currentSuggestionPrecision"
        :current-topics-number="sparkstats.currentTopicsNumber"
      />
      <br>

      <indexing-form
        :files="indexingStats.files"
        :current-suggestion-precision="sparkstats.currentSuggestionPrecision"
        :current-topics-number="sparkstats.currentTopicsNumber"
        @onIndexingSubmit="handleIndexingSubmit"
      />
    </div>

  </div>
</template>

<script>
import IndexingForm from './components/IndexingForm'
import IndexingStats from './components/IndexingStats'
import { indexingStats } from '@/api/file'
import { sparkStats } from '@/api/spark-manager'

export default {
  components: {
    IndexingForm,
    IndexingStats
  },
  data: function() {
    return {
      indexingStats: {
        files: 0,
        notIndexed: 0
      },
      sparkstats: {
        numberOfJobs: 0,
        lastJobDate: null,
        currentSuggestionPrecision: 0,
        currentTopicsNumber: 2
      }
    }
  },
  created() {
    indexingStats().then((result) => {
      this.indexingStats = Object.assign({}, this.indexingStats, result.data)
    })
    sparkStats().then((result) => {
      this.sparkstats = Object.assign({}, this.sparkstats, result.data)
      this.sparkstats.lastJobDate = new Date(this.sparkstats.lastJobDate)
    })
  },
  methods: {
    handleIndexingSubmit: function() {
      sparkStats().then((result) => {
        this.sparkstats = Object.assign({}, this.sparkstats, result.data)
        this.sparkstats.lastJobDate = new Date(this.sparkstats.lastJobDate)
      })
    }
  }
}
</script>

<style scoped>

</style>
