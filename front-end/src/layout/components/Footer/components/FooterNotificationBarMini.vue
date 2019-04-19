<template>
  <div class="notif">
    <el-button size="mini" icon="el-icon-delete" @click="deleteCurrentNotification" />
    {{ getCurrentNotification }}
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'FooterNotificationBarMini',
  computed: {
    ...mapGetters('notification', [
      'getCurrentNotification'
    ])
  },
  methods: {
    ...mapActions('notification', [
      'deleteCurrentNotification'
    ])
  }
}
</script>

<style lang="scss" scoped>
.notif {
}
</style>
