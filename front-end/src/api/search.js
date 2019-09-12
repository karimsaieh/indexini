import request from '@/utils/request'

const VUE_APP_SEARCH_HOST = process.env.VUE_APP_SEARCH_HOST
const SEARCH_BASE_URL = `${VUE_APP_SEARCH_HOST}/search-ms/api/v1/search`

export function getLdaTopics() {
  return request({
    url: `${SEARCH_BASE_URL}/ldaTopics`,
    method: 'get'
  })
}

export function findAllSortBy(params) {
  return request({
    url: `${SEARCH_BASE_URL}`,
    method: 'get',
    params
  })
}

export function find(params, cancelToken) {
  return request({
    url: `${SEARCH_BASE_URL}`,
    method: 'get',
    params,
    cancelToken
  })
}

export function history(params) {
  return request({
    url: `${SEARCH_BASE_URL}/history`,
    method: 'get',
    params
  })
}

export function countSearch() {
  return request({
    url: `${SEARCH_BASE_URL}/countSearch`,
    method: 'get'
  })
}

export function historyAgg(params) {
  return request({
    url: `${SEARCH_BASE_URL}/historyAgg`,
    method: 'get',
    params
  })
}

export function countLdaTopics() {
  return request({
    url: `${SEARCH_BASE_URL}/countLdaTopics`,
    method: 'get'
  })
}
