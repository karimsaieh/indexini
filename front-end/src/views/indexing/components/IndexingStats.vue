<template>
  <div>

    <el-tag type="info">Fichier:
      <countTo :start-val="0" :end-val="files" :duration="3000" />
    </el-tag>
    <el-tag type="success">Indexés:
      <countTo :start-val="0" :end-val="indexed" :duration="3000" />
    </el-tag>
    <el-tag type="danger">Non Indexés:
      <countTo :start-val="0" :end-val="notIndexed" :duration="3000" />
    </el-tag>
    <br>

    <el-tag type="info">Dernière Indexation:
      {{ lastJobDate }}
    </el-tag>
    <el-tag type="info">Actual precision:
      <countTo :start-val="0" :end-val="currentSuggestionPrecision" :duration="3000" />%
    </el-tag>
    <el-tag type="info">Actual nbr  of topics:
      <countTo :start-val="0" :end-val="currentTopicsNumber" :duration="3000" />
    </el-tag>
    <el-tag type="info">nbr  of jobs:
      <countTo :start-val="0" :end-val="numberOfJobs" :duration="3000" />
    </el-tag>

  </div>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  name: 'IndexingStats',
  components: {
    CountTo
  },
  props: {
    notIndexed: {
      type: Number,
      default: 0
    },
    files: {
      type: Number,
      default: 0
    },
    numberOfJobs: {
      type: Number,
      default: 0
    },
    lastJobDate: {
      type: Date,
      default: new Date()
    },
    currentSuggestionPrecision: {
      type: Number,
      default: 0
    },
    currentTopicsNumber: {
      type: Number,
      default: 2
    }
  },
  data: function() {
    return {
    }
  },
  computed: {
    indexed: function() {
      return this.files - this.notIndexed
    }
  }

}
</script>

<style lang="scss" scoped>

</style>
