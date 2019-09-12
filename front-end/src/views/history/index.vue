<template>
  <div class="fixed-width-page-parent-container">
    <div class="fixed-width-page-container">
      <div v-loading="loading">
        <el-card>

          <el-table
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
          <br>
          <el-row>
            <el-col :span="24">
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
            </el-col>
          </el-row>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { history } from '@/api/search'

export default {

  data() {
    return {
      historyData: [],
      query: {
        size: 10,
        page: 1
      },
      total: 0,
      pageSizes: [5, 10, 25, 50, 100, 200, 500],
      loading: false
    }
  },
  created() {
    this.refreshData()
  },
  methods: {
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
      const query = Object.assign({}, this.query)
      query.page -= 1
      history(query).then((result) => {
        this.historyData = result.data.content
        this.total = result.data.totalElements
        this.query.size = result.data.pageable.pageSize
        this.query.page = result.data.pageable.pageNumber + 1
        this.loading = false
      })
    },
    doSearch(history) {
      this.$router.push({ name: 'Search', query: { query: history, size: 10, page: 1 }})
    }
  }
}
</script>

<style lang="scss" scoped>

</style>

