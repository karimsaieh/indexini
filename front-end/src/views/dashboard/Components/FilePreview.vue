<template>
  <div :style="image">
    <img :style="icon" :src="iconSrc">
  </div>
</template>

<script>

export default {
  name: 'FilePreview',
  props: {
    imageWidth: {
      type: String,
      default: '0'
    },
    imageHeight: {
      type: String,
      default: '0'
    },
    imageSrc: {
      type: String,
      default: ''
    },
    iconSrc: {
      type: String,
      default: ''
    }
  },
  computed: {
    image: function() {
      return {
        backgroundImage: `url(\'${this.imageSrc}\')`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        backgroundSize: 'contain',
        width: this.imageWidth + 'px',
        height: this.imageHeight + 'px',
        overflow: 'hidden',
        position: 'relative',
        cursor: 'pointer'
      }
    },
    icon: function() {
      return {
        position: 'absolute',
        maxWidth: '50px',
        maxHeight: '50px',
        bottom: '1px',
        right: '1px'
      }
    }
  }
}
</script>

<style scoped>
</style>

