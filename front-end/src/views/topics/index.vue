<template>
  <div class="fixed-width-page-parent-container">
    <div class="fixed-width-page-container">
      <el-card>
        <topics-table />
      </el-card>
    </div>
  </div>
</template>

<script>
import TopicsTable from './components/TopicsTable'
export default {
  components: {
    TopicsTable
  }
}
</script>

<style scoped>

</style>

