<template>
  <div class="fixed-width-page-parent-container">
    <div class="fixed-width-page-container">
      <upload-manual-form />
      <br>
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
