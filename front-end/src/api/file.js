import request from '@/utils/request'

const VUE_APP_FILE_HOST = process.env.VUE_APP_FILE_HOST
const FILE_BASE_URL = `${VUE_APP_FILE_HOST}/files-ms/api/v1/files`

export function submitIngestionRequest(data) {
  return request({
    url: `${FILE_BASE_URL}/func/submitIngestionRequest`,
    method: 'post',
    data
  })
}
