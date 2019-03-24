<template>
  <div>

    <div class="app-container">

      dasgboard container
      <div style="border: 1px solid; height: 200px; width:200px">
        <file-preview
          :image-height="200"
          :image-width="200"
          image-src="https://images.pexels.com/photos/248159/pexels-photo-248159.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
          icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        />
      </div>

      <!-- <file-thumbnail
        image-src=""
        image-width="90"
        image-height="120"
        icon-src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Vue.js_Logo_2.svg/888px-Vue.js_Logo_2.svg.png"
        icon-width="50"
      /> -->
      <!-- <file-summary
        summary="text summaryd"
      /> -->
      <!-- <file-word-cloud :words="words" style="width:600px; height:300px; border: 1px solid" /> -->
      <!-- <file-radar-chart :series-data="seriesData" :lda-topics-descritpion="ldaTopicsDescritpion" /> -->
      <file-similar-files />

      <!-- <file-search-card /> -->
      <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
      <br><br><br><br><br><br><br><br><br><br>
      <br><br><br><br><br><br><br><br><br><br>

    </div>
  </div></template>

<script>

import FilePreview from './Components/FilePreview'
// import FileThumbnail from './Components/FileThumbnail'
// import FileSummary from './Components/FileSummary'
// import FileWordCloud from './Components/FileWordCloud'
// import FileRadarChart from './Components/FileRadarChart'
// import FileSearchCard from './Components/FileSearchCard'
import FileSimilarFiles from './Components/FileSimilarfiles'
export default {
  name: 'Dashboard',
  components: {
    FilePreview,
    // FileThumbnail,
    // FileSummary
    // FileWordCloud
    // FileRadarChart
    // FileSearchCard,
    FileSimilarFiles

  },
  data: function() {
    return {
      words: [['romance', 19], ['brother', 10], ['football', 100], ['horror', 3], ['fantasy', 7], ['adventure', 3]],
      seriesData: [0.10, 0.505, 0.95, 0.5, 0.8],
      ldaTopicsDescritpion: [
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
