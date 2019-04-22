<template>
  <div>
    <el-card shadow="always">
      <div slot="header" class="clearfix">
        <span>Indexer</span>
      </div>
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label-width="190px" label="Pertinence de suggestion" prop="suggestionPrecision">
          <el-slider v-model="form.suggestionPrecision" :format-tooltip="suggestionExplanation" />
        </el-form-item>

        <el-form-item label-width="190px" label="Nombre de Topics" prop="topicsNumber">
          <el-input-number v-model="form.topicsNumber" :min="2" :max="maxTopics" />

        </el-form-item>

        <el-form-item style="margin-left: 70px;">
          <el-button type="primary" @click="submitForm('form')">Confirmer</el-button>
          <el-button @click="resetForm('form')">réinitialiser</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { submitJob } from '@/api/spark-manager.js'
export default {
  name: 'IndexingForm',
  props: {
    files: {
      type: Number,
      default: 0
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
      form: {
        suggestionPrecision: 0,
        topicsNumber: 2
      },
      maxTopics: 40
    }
  },
  watch: {
    currentSuggestionPrecision: function(val) {
      this.form.suggestionPrecision = val
    },
    currentTopicsNumber: function(val) {
      this.form.topicsNumber = val
    }
  },
  methods: {
    suggestionExplanation: function(value) {
      if (this.form.suggestionPrecision < 30) {
        return `${value}: Vous auriez plus de suggestions,  moins pertinentes`
      } else if (this.form.suggestionPrecision > 70) {
        return `${value}: Vous auriez moins de suggestions,  plus pertinents`
      } else {
        return ''
      }
    },
    resetForm: function(formName) {
      this.$refs[formName].resetFields()
    },
    submitForm: function(formName) {
      const payload = Object.assign({}, this.form, { files: this.files })
      submitJob(payload)
        .then(result => {
          this.$message({
            showClose: true,
            message: 'Confirmé.',
            type: 'success'
          })
          // this.resetForm('form')
          this.$emit('onIndexingSubmit')
        })
        .catch(err => {
          if (err.response.status === 503) {
            this.$message({
              showClose: true,
              message: "Un autre job est en cours d'éxécution.",
              type: 'warning'
            })
          }
        })
    }
    // calculateKmeansK: function() {
    //   const k = Math.round((this.form.suggestionPrecision / 100) * (this.files / 2))
    //   return Math.max(2, k)
    // }
  }

}
</script>

<style scoped>

</style>
