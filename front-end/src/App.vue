<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'App',
  created: function() {
    const _this = this
    const evtSource = new EventSource('http://localhost:3010/notifs-ms/api/v1/notifs/stream')
    evtSource.onmessage = function(e) {
      _this.updateCurrentNotification(JSON.parse(e.data))
    }
  },
  methods: {
    ...mapActions('notification', [
      'updateCurrentNotification'
    ])
  }
}
</script>
