<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import axios from 'axios'

// create an axios instance

export default {
  name: 'App',
  created() {
    const service = axios.create({
      baseURL: 'http://pfe.localhost:8080/api/v1', // api 的 base_url
      timeout: 5000 // request timeout
    })
    service.get('/files').then(res => {
      console.log(res)
    })
  }
}
</script>
