<template>
  <div class="fixed-width-page-parent-container">
    <div class="fixed-width-page-container">
      <div>

        <el-row :gutter="20">
          <el-col :sm="18">
            <el-input
              v-model="query.query"
              clearable
              @keyup.enter.native="doSearch()"
              @clear="doSearch()"
            />
          </el-col>
          <el-col :sm="6">
            <el-button type="primary" icon="el-icon-search" @click="doSearch()">Search</el-button>
          </el-col>
        </el-row>

        <div class="tip">
          <template v-if="Object.keys(suggestions).length !== 0">
            Essayez avec:
            <span v-for="(value, path, index) in suggestions" :key="`suggestions-${index}`" class="link-type">
              <span class="link-type" @click="searchUsingSuggestion(path)" v-html="value" />
            </span>
          </template>
        </div>

        <div v-loading="loading" style="margin-top:30px">
          <template v-if="filesData.length==0">
            <el-card>
              Aucune resultat
            </el-card>
          </template>
          <template v-else>
            <div v-for="(file, index) in filesData" :key="`files-sr-${index}`">
              {{ file.id }}
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

export default {
  components: {
    BackToTop
  },
  data() {
    return {
      filesData: [],
      suggestions: {},
      loading: false,
      cancelFind: null,
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
    if (this.$route.query.query !== undefined &&
    this.$route.query.page !== undefined &&
    this.$route.query.size !== undefined) {
      this.loading = true
      this.query = Object.assign({}, this.$route.query)
      const query = Object.assign({}, this.query)
      query.page -= 1
      find(query).then((result) => {
        this.setData(result)
      })
    }
  },

  beforeRouteUpdate(to, from, next) {
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
    find(query, source.token).then((result) => {
      this.setData(result)
      next()
    })
  },

  methods: {
    setData(result) {
      this.filesData = result.data.fileGetDtosPage.content
      this.total = result.data.fileGetDtosPage.totalElements
      this.query.size = result.data.fileGetDtosPage.pageable.pageSize
      this.query.page = result.data.fileGetDtosPage.pageable.pageNumber + 1
      this.suggestions = result.data.suggestionsList
      this.loading = false
    },
    doSearch() {
      this.query.page = 1
      this.$router.replace({ query: this.query })
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
    }
  }
}
</script>

<style scoped>
.tip{
  font-size: 12px;
  color: #606266;
  height:15px;
}
</style>
