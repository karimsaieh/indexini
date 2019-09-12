<template>
  <div :style="image">
    <img :src="imageSrc" style="visibility: hidden;width:100%">

  </div>

</template>

<script>

export default {
  name: 'FilePreview',
  props: {

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
        backgroundPositionY: 'top',
        backgroundPositionX: 'center',
        backgroundRepeat: 'no-repeat',
        backgroundSize: 'contain',
        width: '100%',
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

