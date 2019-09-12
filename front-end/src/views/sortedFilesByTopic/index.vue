<template>
  <div class="fixed-width-page-parent-container">
    <div class="fixed-width-page-container">
      <sorted-files-by-topic-table />
    </div>
  </div>
</template>

<script>
import SortedFilesByTopicTable from './components/SortedFilesByTopicTable'

export default {
  components: {
    SortedFilesByTopicTable
  }
}
</script>

<style scoped>

</style>
