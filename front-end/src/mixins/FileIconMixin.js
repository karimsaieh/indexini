var FileIconMixin = {
  methods: {
    contentTypeIcon(contentType) {
      if (contentType === 'application/pdf') {
        return require('@/assets/icons/pdf.png')
      } else {
        return require('@/assets/icons/not_ready_yet.png')
      }
    }
  }
}

export default FileIconMixin
