<template>
  <div :style="image" class="image">
    <img v-if="imageSrc" :style="icon" :src="iconSrc">
  </div>
</template>

<script>
export default {
  name: 'FileThumbnail',
  props: {
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
      const img = this.imageSrc || this.iconSrc
      return {
        backgroundImage: `url(\'${img}\')`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        backgroundSize: 'contain',
        width: '100px',
        height: '130px',
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
.image{
  border-radius: 3px;
  border: 1px  solid #eee;
}
</style>

