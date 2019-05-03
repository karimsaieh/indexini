<template>
  <div>
    <div v-loading="loading">

      <el-table
        height="359"
        :data="historyData"
        style="width: 100%"
      >
        <el-table-column
          prop="date"
          label="Date"
          width="200"
        >
          <template slot-scope="{row}">
            {{ row.date | formatDateFromISO8601 }}
          </template>

        </el-table-column>

        <el-table-column
          prop="history"
          label="Recherche"
        >
          <template slot-scope="{row}">
            <span class="link-type" @click="doSearch(row.history)"> {{ row.history }} </span>
          </template>
        </el-table-column>
      </el-table>

    </div>
  </div>
</template>

<script>
import { history } from '@/api/search'

export default {
  name: 'DashboardHistoryTable',
  data() {
    return {
      historyData: [],
      loading: false
    }
  },
  created() {
    this.loading = true
    const query = { page: 0, size: 10 }
    history(query).then((result) => {
      this.historyData = result.data.content
      this.loading = false
    }).catch(() => {
      this.loading = false
    })
  },
  methods: {
    doSearch(history) {
      this.$router.push({ name: 'Search', query: { query: history, size: 10, page: 1 }})
    }
  }
}
</script>

<style lang="sass" scoped>

</style>
