<template>
  <div>
    <div class="page-container">
      <router-link :to="{ name: 'Dashboard', query: { plan: 'private' }, params: { disableTransition: true } }">
        Go to Foo
      </router-link>
      <br>
      <router-link to="/?q=6">Go to Bar</router-link>
      dasgboard container
      <!-- <file-search-card /> -->
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
      <br>
    </div>
  </div>
</template>

<script>
// import FilePreview from './Components/FilePreview'
// import FileThumbnail from './Components/FileThumbnail'
// import FileSummary from './Components/FileSummary'
// import FileWordCloud from './Components/FileWordCloud'
// import FileRadarChart from './Components/FileRadarChart'
// import FileSimilarFiles from './Components/FileSimilarfiles'
// import FileSearchCard from './Components/FileSearchCard'
export default {
  name: 'Dashboard',
  components: {
    // FilePreview,
    // FileThumbnail,
    // FileSummary
    // FileWordCloud
    // FileRadarChart
    // FileSearchCard
    // FileSimilarFiles
  },
  data: function() {
    return {
      words: [
        ['romance', 19],
        ['brother', 10],
        ['football', 100],
        ['horror', 3],
        ['fantasy', 7],
        ['adventure', 3]
      ],
      seriesData: [0.1, 0.505, 0.95, 0.5, 0.8],
      ldaTopicsDescritpion: [
        {
          id: '0',
          description: [
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
          id: '4',
          description: [
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
          id: '3',
          description: [
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
          id: '1',
          description: [
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
        },
        {
          id: '2',
          description: [
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
