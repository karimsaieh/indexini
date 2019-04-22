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

export function indexingStats(data) {
  return request({
    url: `${FILE_BASE_URL}/func/indexingStats`,
    method: 'get'
  })
}

export function findAll(query) {
  return request({
    url: `${FILE_BASE_URL}`,
    method: 'get',
    params: query
  })
}

export function readFile(url) {
  return `${FILE_BASE_URL}/func/readFile?url=${url}`
}

export function deleteFileByUrl(url) {
  return request({
    url: `${FILE_BASE_URL}?url=${url}`,
    method: 'delete'
  })
}

export function deleteMultipleFilesByUrl(data) {
  return request({
    url: `${FILE_BASE_URL}/deletes`,
    method: 'delete',
    data
  })
}

