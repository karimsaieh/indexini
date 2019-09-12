<template>
  <div v-loading="loading">

    <el-row :gutter="10">

      <el-col
        v-for="(file) in similarFilesData"
        :key="file.id"
        justify="center"
        align="middle"
        :xs="12"
        :sm="6"
        :md="4"
        :lg="3"
        :xl="3"
      >
        <div style="height:280px;" @click="goToFileDetail(file.id)">
          <file-thumbnail
            :image-src="file.thumbnail"
            :icon-src="contentTypeIcon(file.contentType)"
          />
          <div>
            <span class="link-type" style="word-break: break-all;">{{ file.fileName | wrapFileName }} </span>
          </div>
        </div>
      </el-col>

    </el-row>
    <el-row>
      <el-col justify="center" align="middle" style="margin-top:10px;">
        <el-pagination
          background
          :current-page.sync="query.page"
          :page-size="query.size"
          layout="total, prev, pager, next, jumper"
          :total="total"
          @current-change="handleCurrentChange"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import FileThumbnail from './FileThumbnail'
import { find } from '@/api/search'
import FileIconMixin from '@/mixins/FileIconMixin'

export default {
  name: 'FileSimilarFiles',
  filters: {
    wrapFileName: function(value) {
      const n = 250
      if (value.length > n) { return value.substring(0, n) + '...' } else { return value }
    }
  },
  components: {
    FileThumbnail
  },
  mixins: [FileIconMixin],
  props: {
    bisectingKmeansPrediction: {
      type: Number,
      default: 0
    },
    currentFileId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      similarFilesData: [],
      query: {
        page: 1,
        size: 8
      },
      total: 0,
      loading: false
    }
  },
  created() {
    this.refreshData()
  },
  methods: {
    handleCurrentChange() {
      this.refreshData()
    },
    refreshData() {
      this.loading = true
      const query = {
        by: 'bisectingKmeansPrediction',
        value: this.bisectingKmeansPrediction,
        size: this.query.size,
        page: this.query.page - 1,
        must: 'id',
        not: this.currentFileId
      }
      find(query).then((result) => {
        this.similarFilesData = result.data.content
        this.loading = false
        this.total = result.data.totalElements
        this.query.size = result.data.pageable.pageSize
        this.query.page = result.data.pageable.pageNumber + 1
      })
    },
    goToFileDetail(id) {
      this.$router.push({ name: 'FileDetail', query: { id: encodeURI(id) }})
    }

  }
}
</script>

<style scoped>

</style>

