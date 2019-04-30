<template>
  <div>
    <vue-word-cloud
      v-loading="loading"
      :words="words"
      :color="pickColor"
      :spacing="spacing"
      :progress.sync="progress"
      :rotation="rotation"
      :font-family="fontFamily"
      :animation-duration="animationDuration"
      :font-size-ratio="fontSizeRatio"
      :enter-animation="enterAnimation"
      :leave-animation="leaveAnimation"
    />
  </div>
</template>

<script>
import VueWordCloud from 'vuewordcloud'

export default {
  name: 'FileWordCloud',
  components: {
    VueWordCloud

  },
  props: {
    words: {
      type: Array,
      default: function() { return [['nothingTOshow', 1]] }
    }
  },

  data: function() {
    return {
      loading: true,
      progress: 0,
      enterAnimation: 'animated flipInX',
      leaveAnimation: 'animated flipOutX',
      spacing: 1,
      rotation: 0,
      animationDuration: 1500,
      fontSizeRatio: 7,
      fontFamily: 'Russo One',
      colors: [
        '#d99cd1', '#c99cd1', '#b99cd1', '#a99cd1',
        '#403030', '#f97a7a', '#31a50d', '#d1b022',
        '#74482a', '#ffd077', '#3bc4c7', '#3a9eea',
        '#ff4e69', '#461e47']
    }
  },
  watch: {
    progress: function(currentProgress, previousProgres) {
      if (previousProgres) {
        this.loading = false
      }
    }
  },
  methods: {
    pickColor: function() {
      return this.colors[Math.floor(Math.random() * this.colors.length)]
    }
  }

}
</script>
