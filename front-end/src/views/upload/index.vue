<template>
  <div>
    <div class="page-container">
      <upload-manual-form />
      <upload-ingestion-by-url-form />

    </div>
  </div>
</template>

<script>
import UploadIngestionByUrlForm from './components/UploadIngestionByUrlForm.vue'
import UploadManualForm from './components/UploadManualForm.vue'

export default {
  name: 'Upload',
  components: {
    UploadIngestionByUrlForm,
    UploadManualForm
  },
  data: function() {
    return {
    }
  }
}
</script>
