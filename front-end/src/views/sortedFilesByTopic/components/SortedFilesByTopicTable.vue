<template>
  <div v-loading="loading">
    <span v-for="(word, index) in topicDescription" :key="`word-${index}`">
      <el-button
        size="mini"
        style="margin:10px"
        @click="searchByWord(word)"
      >{{ word }}</el-button>
    </span>

    <div v-for="(file, index) in filesData" :key="`file-${index}`">
      <el-card style="margin:10px;">
        <el-row>
          <el-col :sm="22">
            <span class="link-type" @click="goToFileCard(file.id)"> {{ file.fileName }} </span>
          </el-col>
          <el-col :sm="2" :class="percentageClass(file.ldaTopics[id])">
            {{ file.ldaTopics[id] | formatPercentage }}

          </el-col>

        </el-row>
      </el-card>
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

  </div>

</template>

<script>
import { findAllSortBy, getLdaTopics } from '@/api/search'

export default {
  name: 'SortedFilesByTopicTable',
  filters: {
    formatPercentage: function(value) {
      if (!value) return ''
      return `${Math.round(value * 100)}%`
    }
  },
  data() {
    return {
      loading: false,
      topicDescription: [],
      filesData: [],
      id: '',
      query: {
        size: 10,
        page: 1
      },
      total: 0,
      pageSizes: [1, 2, 5, 10, 25, 50, 100, 200, 500]
    }
  },
  created() {
    this.refreshData()
  },
  methods: {
    searchByWord: function(word) {
      alert('TODO: search by selected word: ' + word)
    },
    handleCurrentChange(page) {
      this.loading = true
      this.refreshData()
    },
    handleSizeChange(size) {
      this.loading = true
      this.query.size = size
      this.refreshData()
    },
    refreshData() {
      this.loading = true
      this.id = this.$route.query.id
      const query = Object.assign({}, this.query, { sortBy: `ldaTopics.${this.id}` }, this.predicate)
      query.page -= 1
      getLdaTopics().then((result) => {
        findAllSortBy(query).then((result) => {
          this.loading = false
          this.filesData = result.data.content
          this.total = result.data.totalElements
          this.query.size = result.data.pageable.pageSize
          this.query.page = result.data.pageable.pageNumber + 1
        })
        this.topicDescription = result.data.find(topic => topic.id === this.id).description
      })
    },
    percentageClass(value) {
      value = value * 100

      if (value <= 10) {
        return 'i1'
      } else if (value <= 20) {
        return 'i2'
      } else if (value <= 30) {
        return 'i3'
      } else if (value <= 40) {
        return 'i4'
      } else if (value <= 50) {
        return 'i5'
      } else if (value <= 60) {
        return 'i6'
      } else if (value <= 70) {
        return 'i7'
      } else if (value <= 80) {
        return 'i8'
      } else if (value <= 90) {
        return 'i9'
      } else if (value <= 100) {
        return 'i10'
      }
    },
    goToFileCard(id) {
      alert(`go to file card ${id}`)
    }
  }
}
</script>

<style lang="scss"
          scoped
        >

.i10 {
  color: #047400;
  font-size:17px;
  font-style: italic;
  font-weight: 900;
}

.i9 {
  color: #047400;
  font-size:17px;
  font-style: italic;
  font-weight: 900;
}

.i8 {
  color: #047400;
  font-size:17px;
  font-style: italic;
  font-weight: 900;
}

.i7 {
  color: #ffd900;
  font-size:16px;
  font-style: italic;
  font-weight: 900;
}

.i6 {
  color: #ffd900;
  font-size:16px;
  font-style: italic;
  font-weight: 900;
}

.i5 {
  color: #ffd900;
  font-size:16px;
  font-style: italic;
  font-weight: 900;
}

.i4 {
  color: #b30000;
  font-size:15px;
  font-style: italic;
}

.i3 {
  color: #b30000;
  font-size:15px;
  font-style: italic;
}

.i2 {
  color: #b30000;
  font-size:15px;
  font-style: italic;
}

.i1 {
  color: #b30000;
  font-size:15px;
  font-style: italic;
}
</style>

