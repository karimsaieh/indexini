<template>
  <div class="notif">

    <template v-if="show === true">
      <el-button size="mini" icon="fas el-icon-fa-eye-slash" @click="show = !show" />
      <template v-if="getCurrentNotification != undefined">
        {{ getCurrentNotification.msg }}
      </template>
    </template>
    <template v-else>
      <el-button size="mini" icon="fas el-icon-fa-eye" @click="show = !show" />
    </template>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'FooterNotificationBarMini',
  data() {
    return {
      show: true
    }
  },
  computed: {
    ...mapGetters('notification', [
      'getCurrentNotification'
    ])
  }

}
</script>

<style lang="scss" scoped>
.notif {
  width:100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
