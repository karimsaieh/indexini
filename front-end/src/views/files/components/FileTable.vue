<template>
  <div>
    <el-card>
      <el-input
        v-model="predicate.name"
        prefix-icon="el-icon-search"
        placeholder="Fichier"
        clearable
        style="width:20%"
        @clear="refreshData()"
      />
      {{ seachingMsg }}
      <br>
      <br>
      <el-table
        v-loading="loading"
        :data="tableData"
        highlight-current-row
        border
        @selection-change="handleSelectionChange"
      >

        <el-table-column
          type="selection"
          width="55"
        />

        <el-table-column
          prop="bulkSaveOperationTimestamp"
          label="Téleversement"
        >
          <template slot-scope="{row}">
            {{ row.bulkSaveOperationTimestamp | formatDateFromTimestampMilli }}
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="Name"
        >
          <template slot-scope="{row}">
            <span class="link-type" @click="goToFileDetailORreadFile(row)">{{ row.name }}</span>
          </template>

        </el-table-column>
        <el-table-column
          prop="indexed"
          label="indexed"
        >
          <template slot-scope="{row}">
            <span v-if="row.indexed === true">
              <el-tag type="success">indexé</el-tag>
            </span>
            <span v-else>
              <el-tag type="danger">Non indexé</el-tag>
            </span>
          </template>

        </el-table-column>
        <el-table-column
          prop="contentType"
          label="contentType"
        />
        <el-table-column
          prop="source"
          label="source"
        >
          <template slot-scope="{row}">
            <template v-if="row.source.startsWith('http') || row.source.startsWith('ftp')">
              <a class="link-type" :href="row.source" target="_blank"> {{ row.source }}</a>
            </template>
            <template v-else>
              <el-tag type="primary">{{ row.source }}</el-tag>
            </template>
          </template>
        </el-table-column>

        <el-table-column
          label="Operations"
        >

          <template slot-scope="{row}">
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="handleDeleteSingleFile(row)"
            />
          </template>
        </el-table-column>
      </el-table>
      <span v-if="selectedData.length != 0" class="link-type" @click="deleteMultiple">
        Supprimer la selection
      </span>
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
    </el-card>
  </div>
</template>

<script>
import _ from 'lodash'
import { findAll, readFile, deleteFileByUrl, deleteMultipleFilesByUrl } from '@/api/file'
export default {
  name: 'FileTable',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedData: [],
      total: 0,
      pageSizes: [5, 10, 25, 50, 100, 200, 500],
      query: {
        size: 10,
        page: 1
      },
      predicate: {
        name: ''
      },
      seachingMsg: '',
      debouncedRefreshData: null
    }
  },
  watch: {
    'predicate.name': function() {
      this.seachingMsg = "J'attends que vous arrêtiez de taper..."
      this.debouncedRefreshData()
    }
  },
  created() {
    this.refreshData()
    this.debouncedRefreshData = _.debounce(this.refreshData, 1000)
  },
  methods: {
    handleSelectionChange(val) {
      this.selectedData = val
    },
    goToFileDetailORreadFile(row) {
      if (row.indexed === true) {
        this.$router.push({ name: 'FileDetail', query: { id: encodeURI(row.location) }})
      } else {
        window.open(readFile(encodeURIComponent(row.location)))
      }
    },
    handleDeleteSingleFile(row) {
      this.loading = true
      deleteFileByUrl(row.location).then((result) => {
        this.$message({
          message: 'Confirmé.',
          type: 'success'
        })
        this.refreshData()
      })
    },
    deleteMultiple() {
      this.loading = true
      const urls = this.selectedData.map(elem => elem.location)
      deleteMultipleFilesByUrl(urls).then((result) => {
        this.$message({
          message: 'Confirmé.',
          type: 'success'
        })
        this.refreshData()
      })
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
      this.seachingMsg = ''
      this.loading = true
      const query = Object.assign({}, this.query, { sort: 'bulkSaveOperationTimestamp,desc' }, this.predicate)
      query.page -= 1
      findAll(query).then((result) => {
        this.tableData = result.data.content
        this.loading = false
        this.total = result.data.totalElements
        this.query.size = result.data.pageable.pageSize
        this.query.page = result.data.pageable.pageNumber + 1
      })
    }
  }

}
</script>

<style lang="scss" scoped>

</style>

