<template>
  <div>

    <el-card shadow="always">
      <div style="height:30px;">
        <div style="float: right; ">
          <el-tooltip class="item" effect="dark" content="Télécharger" placement="top">
            <el-button style="padding:6px !important; margin:0px !important" @click="handleReadFile()">Ouvrir</el-button>

          </el-tooltip>

        </div>
        <div style="float: left; margin-right:20px; margin-top: 0px;">
          <img :src="contentTypeIcon(contentType)" class="content-type-icon">
        </div>
        <div>
          <div class="file-name link-type" @click="goToFileDetail(id)">{{ name }}</div>
          <div class="tip"> {{ bulkSaveOperationTimestamp | formatDateFromTimestampMilli }} </div>
        </div>

      </div>

      <div class="mydivider" />

      <!-- 22222222222 -->
      <el-row>
        <el-col style="height: 270px; overflow:auto" :span="18" class="row-col-border">
          <div style="margin:15px">
            <template v-if="includesHighlight === true">
              <div v-for="(highlight, index) in highlightList" :key="`hgh-${index}`">
                <div class="highlight" v-html="highlight" />
              </div>
            </template>
            <template v-else>
              <file-summary :summary="summary" />
            </template>
          </div>
        </el-col>

        <el-col style="height: 270px;" class="row-col-border" :span="6">
          <div style="vertical-align: top">
            <file-preview
              :alt="name"
              style="max-height:268px; width:100%"
              :image-src="thumbnail"
            />
          </div>

        </el-col>
      </el-row>

      <template v-if="includesHighlight === true">
        <div class="show-tip" @click="showSummaryClick" v-html="showSummaryMsg" />
        <el-row v-if="showSummary===true" class="row-col-border">
          <div style="overflow:auto; max-height:220px; margin:0px 15px 0px 15px; ">
            <file-summary :summary="summary" />
          </div>
        </el-row>
      </template>
      <!-- 33333333333333 -->

      <div class="show-tip" @click="showCloudClick" v-html="showCloudMsg" />
      <el-row v-if="showCloud===true">
        <el-col :sm="15" class="row-col-border" style="height:300px;">
          <file-word-cloud :words="words" style="height:290px;" />
        </el-col>
        <el-col :sm="9" class="row-col-border">
          <file-radar-chart
            style="height:300px; overflow:auto ;"
            :series-data="ldaTopics"
            :lda-topics-description="ldaTopicsDescription"
            :topic.sync="topic"
          />
        </el-col>
        <span class="tip">
          {{ topic }}
        </span>
      </el-row>

      <!-- 44444444444 -->
      <div class="show-tip" @click="showSimilarFilesClick" v-html="showSimilarFilesMsg" />
      <el-row v-if="showSimilarFiles===true" class="row-col-border">

        <file-similar-files :current-file-id="id" :bisecting-kmeans-prediction="bisectingKmeansPrediction" style="margin:5px;" />

      </el-row>
      <div style="float: right; ">

        <el-tooltip class="item" effect="dark" content="Supprimer" placement="top">
          <i class="fas el-icon-fa-trash remove-icon" @click="removeFile()" />
        </el-tooltip>
      </div>
    </el-card>

  </div>
</template>

<script>
import FilePreview from './FilePreview'
import FileSummary from './FileSummary'
import FileWordCloud from './FileWordCloud'
import FileRadarChart from './FileRadarChart'
import FileSimilarFiles from './FileSimilarfiles'

import { readFile, deleteFileByUrl } from '@/api/file'
import FileIconMixin from '@/mixins/FileIconMixin'

export default {
  name: 'FileSearchCard',
  components: {
    FilePreview,
    FileSummary,
    FileWordCloud,
    FileRadarChart,
    FileSimilarFiles
  },
  mixins: [FileIconMixin],
  props: {
    id: {
      type: String,
      default: ''
    },
    words: {
      type: Array,
      default: function() { return [] }
    },
    ldaTopics: {
      type: Array,
      default: function() { return [] }
    },
    ldaTopicsDescription: {
      type: Array,
      default: function() { return [] }
    },
    summary: {
      type: Array,
      default: function() {
        return []
      }
    },
    name: {
      type: String,
      default: ''
    },
    bulkSaveOperationTimestamp: {
      type: String,
      default: ''
    },
    contentType: {
      type: String,
      default: ''
    },
    highlightList: {
      type: Array,
      default: function() {
        return []
      }
    },
    thumbnail: {
      type: String,
      default: 'data:image/png;base64,'
    },
    bisectingKmeansPrediction: {
      type: Number,
      default: 0
    },
    includesHighlight: {
      type: Boolean,
      default: true
    }
  },
  data: function() {
    return {
      topic: '',
      showCloud: false,
      showSummary: false,
      showSimilarFiles: false

    }
  },
  computed: {
    showCloudMsg: function() {
      if (this.showCloud === true) {
        return `<i class="fas el-icon-fa-caret-up" /> WordCloud & Radar`
      } else {
        return `<i class="fas el-icon-fa-caret-down" /> WordCloud & Radar`
      }
    },
    showSummaryMsg: function() {
      if (this.showSummary === true) {
        return `<i class="fas el-icon-fa-caret-up" /> Sommaire`
      } else {
        return `<i class="fas el-icon-fa-caret-down" /> Sommaire`
      }
    },
    showSimilarFilesMsg: function() {
      if (this.showSimilarFiles === true) {
        return `<i class="fas el-icon-fa-caret-up" /> Fichier similaires`
      } else {
        return `<i class="fas el-icon-fa-caret-down" /> Fichiers similaires`
      }
    }
  },
  methods: {
    showCloudClick() {
      this.showCloud = !this.showCloud
      this.topic = ''
    },
    showSummaryClick() {
      this.showSummary = !this.showSummary
    },
    showSimilarFilesClick() {
      this.showSimilarFiles = !this.showSimilarFiles
    },
    handleReadFile() {
      window.open(readFile(encodeURIComponent(this.id)))
    },
    removeFile() {
      this.$confirm('Cela supprimera définitivement le fichier. Continuer?', 'Annuler', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }).then(() => {
        deleteFileByUrl(this.id).then((result) => {
          this.$router.go({ name: 'Search', query: this.query })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Suppression Annulé'
        })
      })
    },
    goToFileDetail(id) {
      this.$router.push({ name: 'FileDetail', query: { id: encodeURI(id) }})
    }
  }
}
</script>

<style lang="scss" scoped>

.right {
    width: 280px;
    float: right;
}

.left {
    float: none; /* not needed, just for clarification */
    /* the next props are meant to keep this block independent from the other floated one */
    width: auto;
    overflow: hidden;
}
.container {
   height: auto;
   overflow: hidden;
}
.tip{
  font-size: 12px;
  color: #606266;
  height:15px;
}
.show-tip{
  font-size: 14px;
  color: #606266;
  height:15px;
  font-weight: 900;
  cursor: pointer;
}
.row-col-border {
  border: 1px solid #eee;
}
.mydivider {
  background-color: #dcdfe6;
  position: relative;
  display: block;
  height: 1px;
  margin: 10px 0;
  width: 100%;
}
.file-name {
  font-weight: 600;
  color: #131212;
}
.content-type-icon {
  height: 30px;
  width: 30px;
  border-radius: 30px;
  border: 1px solid #eee
}

.content-type-title{
  height: 32px;
}

.download-icon {
  color: rgb(46, 88, 74);
  margin-right:20px;
  cursor: pointer;
}

.remove-icon {
color: #d35050;
cursor: pointer;
}

.highlight {
  /deep/ em {
    background-color: #fff6aa;
    border: 1px solid #e6d9c5;
    border-radius: 4px;
  }
}

</style>
