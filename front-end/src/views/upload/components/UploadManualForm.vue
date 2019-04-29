<template>
  <div>
    <i class="fas el-icon-fa-camera" />
    <el-upload
      ref="upload"
      v-loading="loading"
      element-loading-background="rgba(0, 0, 0, 0.1)"
      class="upload-demo"
      action="http://localhost:3011/files-ms/api/v1/files"
      :auto-upload="false"
      name="multipartFile"
      :on-success="handleSuccess"
      :on-error="handleError"
      :multiple="true"
      :file-list="fileList"
      :data="data"
      :on-change="handleChange"
    >
      <el-button slot="trigger" size="small" type="primary">select fileS</el-button>
      <el-button
        :disabled="fileList.length == 0"
        style="margin-left: 10px;"
        size="small"
        type="success"
        @click="submitUpload"
      >upload to server</el-button>
      <div slot="tip" class="el-upload__tip">pdf for now</div>
    </el-upload>

  </div>
</template>

<script>
import fileTypesConsts from '@/consts/file-types.js'
import _ from 'lodash'

export default {
  name: 'UploadManualForm',
  data: function() {
    return {
      loading: false,
      fileList: [],
      data: {
        'bulkSaveOperationUuid': 'uuid',
        'bulkSaveOperationTimestamp': 54584
      },
      fileTypesConsts: fileTypesConsts
    }
  },
  methods: {
    submitUpload: function() {
      this.loading = true
      this.data.bulkSaveOperationUuid = this.uuidv4()
      this.data.bulkSaveOperationTimestamp = new Date().getTime()
      this.$refs.upload.submit()
    },
    handleSuccess: function(response, file, fileList) {
      let i = 0
      while (i < fileList.length && fileList[i].status === 'success') {
        i += 1
      }
      if (i === fileList.length) {
        this.$refs.upload.clearFiles()
        this.loading = false
      }
    },
    handleError: function() {
      this.loading = false
      this.$refs.upload.clearFiles()
    },
    uuidv4() {
      return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
      )
    },
    handleChange(file, fileList) {
      console.log(fileList)
      this.fileList = fileList.filter(item => ['application/pdf', 'text/plain'].includes(item.raw.type))
      this.fileList = _.uniqBy(this.fileList, 'name')
      console.log(fileList)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>

