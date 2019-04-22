<template>
  <div>
    <el-card shadow="always">

      <el-row>
        File title goes here

      </el-row>
      ___________<br>
      <el-row>
        <div class="container">
          <div class="right">
            <file-preview
              style="border: 1px solid;max-height:370px"
              image-src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDLe1cVbm0iWhd7Woe_u1DrWcef3HG3KIhe3xTUeYzAd0mwhRU"
              icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
            />
          </div>
          <div class="left">
            <file-summary class="left" style="border: 1px solid;" summary="rydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydrydryd" />          </div>

        </div>
      </el-row>

      <el-row>
        <el-col :span="14">
          <file-word-cloud :words="words" style="height:300px;" />
        </el-col>
        <el-col :span="10">
          <file-radar-chart :series-data="seriesData" :lda-topics-description="ldaTopicsDescription" />
        </el-col>
      </el-row>

      <file-similar-files />
    </el-card>

  </div>
</template>

<script>
import FilePreview from './FilePreview'
import FileSummary from './FileSummary'
import FileWordCloud from './FileWordCloud'
import FileRadarChart from './FileRadarChart'
import FileSimilarFiles from './FileSimilarfiles'

export default {
  name: 'FileSearchCard',
  components: {
    FilePreview,
    FileSummary,
    FileWordCloud,
    FileRadarChart,
    FileSimilarFiles
  },
  data: function() {
    return {
      words: [['gogo', 19], ['frr', 10], ['action', 100], ['horror', 650], ['racing', 7], ['adventure', 3],

        ['gogo', 19], ['frr', 10], ['action', 100], ['horror', 300], ['racing', 7], ['adventure', 3],
        ['gogo', 19], ['frr', 10], ['action', 100], ['horror', 3], ['racing', 700], ['adventure', 3],
        ['gogo', 19], ['frr', 10], ['action', 100], ['horror', 3], ['racing', 7], ['adventure', 200],
        ['gogo', 19], ['frr', 10], ['action', 100], ['horror', 3], ['racing', 7], ['adventure', 3]
      ],
      seriesData: [0.10, 0.505, 0.95, 0.5, 0.8],
      ldaTopicsDescription: [
        {
          'id': '0',
          'description': [
            'de',
            '0000est',
            'des',
            'donnees',
            'apprentissage',
            'le',
            'l',
            'et',
            'ou',
            'un'
          ]
        }, {
          'id': '4',
          'description': [
            'de',
            '0000est',
            'des',
            'donnees',
            'apprentissage',
            'le',
            'l',
            'et',
            'ou',
            'un'
          ]
        }, {
          'id': '3',
          'description': [
            'de',
            '0000est',
            'des',
            'donnees',
            'apprentissage',
            'le',
            'l',
            'et',
            'ou',
            'un'
          ]
        },
        {
          'id': '1',
          'description': [
            'de',
            'e111st',
            'des',
            'donnees',
            'apprentissage',
            'le',
            'l',
            'et',
            'ou',
            'un'
          ]
        }, {
          'id': '2',
          'description': [
            'de',
            'es2222t',
            'des',
            'donnees',
            'apprentissage',
            'le',
            'l',
            'et',
            'ou',
            'un'
          ]
        }
      ]
    }
  }
}
</script>

<style scoped>
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
</style>
