<template>
  <div class="summary">
    text summary goes here{{ summary }}
  </div>
</template>

<script>
export default {
  name: 'FileSummary',
  props: {
    summary: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>
.summary {
  word-break: break-all;
}
</style>
