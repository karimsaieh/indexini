<template>
  <div>
    <el-card shadow="always">
      <div slot="header" class="clearfix">
        <span>Téléversement par Url</span>
        <el-button style="float: right; padding: 3px 0" type="text">Operation button</el-button>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="Url" prop="url">
          <el-input v-model="form.url" />
        </el-form-item>

        <el-form-item label="Types" prop="fileTypes">
          <el-select
            v-model="form.fileTypes"
            :rules="rules"
            style="width:60%"
            multiple
            filterable
            placeholder="Choisissez les types de fichiers"
          >
            <el-option
              v-for="item in typesOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="Profondeur" prop="depth">
          <el-input-number v-model="form.depth" controls-position="right" :min="1" :max="3" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">Confirmer</el-button>
          <el-button @click="resetForm('form')">réinitialiser</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { submitIngestionRequest } from '@/api/file'
import fileTypesConsts from '@/consts/file-types.js'

export default {
  name: 'UploadIngestionByUrlForm',
  data() {
    return {
      typesOptions: [],
      form: {
        url: '',
        fileTypes: [],
        depth: 1
      },
      rules: {
        url: [
          // eslint-disable-next-line no-undef
          { validator: this.validateUrl, required: true, trigger: 'blur' }
        ],
        fileTypes: [
          {
            required: true,
            message: 'Veuiller specifier au moins un type',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created: function() {
    fileTypesConsts.forEach(elm => {
      this.typesOptions.push({
        value: elm,
        label: elm.toUpperCase()
      })
    })
  },
  methods: {
    submitForm: function(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          submitIngestionRequest(this.form)
            .then(result => {
              this.$message({
                showClose: true,
                message: 'Confirmé.',
                type: 'success'
              })
              this.resetForm('form')
            })
            .catch(err => {
              console.error(err)
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm: function(formName) {
      this.$refs[formName].resetFields()
    },
    validateUrl: function(rule, value, callback) {
      if (!value) {
        return callback(new Error('Veuillez saisir un Url'))
      }
      if (!this.validateUrlRegex(value)) {
        callback(
          new Error('Veuillez saisir un Url valide ftp://... https://..')
        )
      } else {
        callback()
      }
    },
    validateUrlRegex: function(value) {
      return /^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:[/?#]\S*)?$/i.test(
        value
      )
    }
  }
}
</script>
