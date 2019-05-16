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
        '#d99cd1', '#c99cd1', '#798cd1', '#a99cd1',
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
    pickColor: function([, weight]) {
      if (weight < 1) {
        return this.colors[0]
      }
      if (weight < 2) {
        return this.colors[1]
      }
      if (weight < 4) {
        return this.colors[2]
      }
      if (weight < 8) {
        return this.colors[3]
      }
      if (weight < 16) {
        return this.colors[4]
      }
      if (weight < 32) {
        return this.colors[5]
      }
      if (weight < 64) {
        return this.colors[6]
      }
      if (weight < 128) {
        return this.colors[7]
      }
      if (weight < 256) {
        return this.colors[8]
      }
      if (weight < 512) {
        return this.colors[9]
      }
      if (weight < 1024) {
        return this.colors[10]
      }
      if (weight < 2048) {
        return this.colors[11]
      }
      if (weight < 4096) {
        return this.colors[12]
      }
      return this.colors[13]
    }
  }

}
</script>
