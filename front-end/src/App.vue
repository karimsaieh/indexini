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
    const evtSource = new EventSource(`${process.env.VUE_APP_NOTIF_HOST}/notifs-ms/api/v1/notifs/stream`)
    evtSource.onmessage = function(e) {
      _this.pushNotification(JSON.parse(e.data))
    }
            this.$notify({
          title: 'Info',
          message: "L'opération d'indexation s'est terminée",
          duration: 0
        });
 
  },
  methods: {
    ...mapActions('notification', [
      'pushNotification'
    ])
  }
}
</script>
