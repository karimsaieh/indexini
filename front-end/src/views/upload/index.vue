<template>
  <div>
    <div class="page-container">

      <upload-ingestion-by-url-form />

    </div>
  </div>
</template>

<script>
import UploadIngestionByUrlForm from './components/UploadIngestionByUrlForm.vue'
export default {
  name: 'Upload',
  components: {
    UploadIngestionByUrlForm
  },
  data: function() {
    return {
    }
  }
}
</script>
