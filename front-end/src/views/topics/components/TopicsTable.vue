<template>
  <div>

    <el-table
      v-loading="loading"
      :data="tableData"
    >
      <el-table-column
        label="Topics"
      >
        <template slot-scope="{row}">
          <span v-for="(word, index) in row.description" :key="index">
            <el-button
              size="mini"
              style="margin:5px;"
              @click="searchByWord(word)"
            >{{ word }}</el-button>
          </span>
        </template>

      </el-table-column>

      <el-table-column
        label="top docs"
        fixed="right"
        width="120"
      >
        <template slot-scope="{row}">
          <el-button
            size="mini"
            icon="fas el-icon-fa-list-ol"
            @click="sortFilesByTopic(row.id)"
          />
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>

import { getLdaTopics } from '@/api/search'

export default {
  name: 'TopicsTable',
  data() {
    return {
      tableData: [],
      loading: false
    }
  },
  created() {
    this.loading = true
    getLdaTopics().then((result) => {
      // console.log(result.data)
      this.tableData = result.data
      this.loading = false
    }).catch(err => {
      this.loading = false
      console.error(err)
    })
  },
  methods: {
    searchByWord: function(word) {
      this.$router.push({ name: 'Search', query: { query: word, size: 10, page: 1 }})
    },
    sortFilesByTopic: function(id) {
      this.$router.push({ name: 'SortedFilesByTopic', query: { id }})
    }
  }
}
</script>

<style>

</style>

