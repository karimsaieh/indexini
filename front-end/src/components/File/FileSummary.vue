<template>
  <div class="summary">
    <p v-for="(summ, index) in summary" :key="`summary-gg-${index}`">

      {{ summ }}

    </p>
  </div>
</template>

<script>
export default {
  name: 'FileSummary',
  props: {
    summary: {
      type: Array,
      default: function() {
        return []
      }
    }
  }
}
</script>

<style scoped>
.summary {
}
</style>
