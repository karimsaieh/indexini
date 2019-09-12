import request from '@/utils/request'

const VUE_APP_SPARK_MANAGER_HOST = process.env.VUE_APP_SPARK_MANAGER_HOST
const SPARK_MANAGER_BASE_URL = `${VUE_APP_SPARK_MANAGER_HOST}/spark-mg-ms/api/v1/spark-manager`

export function submitJob(data) {
  return request({
    url: `${SPARK_MANAGER_BASE_URL}/func/submitJob`,
    method: 'post',
    data
  })
}

export function sparkStats(data) {
  return request({
    url: `${SPARK_MANAGER_BASE_URL}/func/sparkStats`,
    method: 'get'
  })
}

