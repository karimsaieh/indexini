<template>
  <div class="fixed-width-page-parent-container">
    <div class="fixed-width-page-container">
      <div>
        <el-row :gutter="20">
          <el-col :sm="18">
            <el-autocomplete
              ref="autocomplete"
              v-model="query.query"
              style="width:100%"
              :debounce="0"
              :hide-loading="true"
              :fetch-suggestions="searchAsYouType"
              clearable
              :trigger-on-focus="false"
              @keyup.enter.native="doSearch()"
              @clear="doSearch()"
              @select="doSearch()"
            >
              <template slot-scope="{ item }" class="search-as-you-type-suggestion">
                <div class="search-as-you-type-suggestion" v-html="item.highlight" />
              </template>
            </el-autocomplete>
          </el-col>
          <el-col :sm="6">
            <el-button type="primary" icon="el-icon-search" @click="doSearch()">Rechercher</el-button>
          </el-col>
        </el-row>

        <div class="tip">
          <template v-if="suggestions!=null && Object.keys(suggestions).length !== 0">
            Essayez avec:
            <span
              v-for="(value, path, index) in suggestions"
              :key="`suggestions-${index}`"
              class="suggestion-highlight link-type"
            >
              <span class="link-type" @click="searchUsingSuggestion(path)" v-html="value" />
            </span>
          </template>
        </div>
        <div v-loading="loading" style="margin-top:30px">
          <template v-if="filesData.length==0">
            <el-card>Aucune resultat</el-card>
          </template>
          <template v-else>
            <div
              v-for="(file) in filesData"
              :key="`files-sr-${file.id}`"
              style="margin-top:30px;margin-bottom:30px"
            >
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

            <el-pagination
              background
              :current-page.sync="query.page"
              :page-sizes="pageSizes"
              :page-size="query.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </template>
        </div>

        <back-to-top :visibility-height="300" :back-position="50" transition-name="fade" />
      </div>
    </div>
  </div>
</template>

<script>
import { find } from '@/api/search'
import axios from 'axios'
import BackToTop from '@/components/BackToTop'
import FileSearchCard from '@/components/File/FileSearchCard'

export default {
  components: {
    BackToTop,
    FileSearchCard
  },
  data() {
    return {
      filesData: [],
      ldaTopicsDescription: [],
      suggestions: {},
      loading: false,
      cancelFind: null,
      cancelSearchAsYouType: null,
      query: {
        query: '',
        size: 10,
        page: 0
      },
      total: 0,
      pageSizes: [1, 2, 5, 10, 25, 50, 100, 200, 500]
    }
  },

  created() {
    if (
      this.$route.query.query !== undefined &&
      this.$route.query.page !== undefined &&
      this.$route.query.size !== undefined
    ) {
      const CancelToken = axios.CancelToken
      const source = CancelToken.source()
      this.cancelFind = source.cancel
      this.loading = true
      this.query = Object.assign({}, this.$route.query)
      const query = Object.assign({}, this.query)
      query.page -= 1
      find(query).then(result => {
        this.setData(result)
      })
    }
  },

  beforeRouteUpdate(to, from, next) {
    if (
      to.query.query !== undefined &&
      to.query.page !== undefined &&
      to.query.size !== undefined
    ) {
      if (this.loading === true) {
        this.cancelFind('canceling current search on favor of a new one')
      }
      const CancelToken = axios.CancelToken
      const source = CancelToken.source()
      this.loading = true
      this.cancelFind = source.cancel
      this.query = Object.assign({}, to.query)
      const query = Object.assign({}, this.query)
      query.page -= 1
      find(query, source.token).then(result => {
        this.setData(result)
        next()
      })
    } else {
      next()
    }
  },

  methods: {
    setData(result) {
      this.filesData = result.data.fileGetDtosPage.content
      this.total = result.data.fileGetDtosPage.totalElements
      this.query.size = result.data.fileGetDtosPage.pageable.pageSize
      this.query.page = result.data.fileGetDtosPage.pageable.pageNumber + 1
      this.suggestions = result.data.suggestionsList
      this.ldaTopicsDescription = result.data.ldaTopicsDescriptionGetDtosList
      this.loading = false
    },
    doSearch() {
      this.query.page = 1
      this.$router.replace({ query: this.query })
      this.$refs.autocomplete.suggestions = []
    },
    searchUsingSuggestion(suggestion) {
      this.query.query = suggestion
      this.doSearch()
    },
    handleCurrentChange(page) {
      console.log('handle currentg page')
      this.$router.replace({ query: this.query })
    },
    handleSizeChange(size) {
      console.log('handle size change')
      this.query.size = size
      this.$router.replace({ query: this.query })
    },
    searchAsYouType(queryString, cb) {
      if (this.cancelSearchAsYouType != null) {
        this.cancelSearchAsYouType('canceling search as you type')
      }
      const CancelToken = axios.CancelToken
      const source = CancelToken.source()
      this.cancelSearchAsYouType = source.cancel

      const query = { searchAsYouType: queryString }

      find(query, source.token).then(result => {
        const suggestions = result.data
        this.cancelSearchAsYouType = null
        cb(suggestions)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.tip {
  font-size: 12px;
  color: #606266;
  height: 15px;
}
.search-as-you-type-suggestion {
  /deep/ em {
    font-weight: bold;
    font-style: normal;
  }
}
.suggestion-highlight {
  /deep/ em {
    font-weight: bold;
    font-style: normal;
  }
}
</style>
