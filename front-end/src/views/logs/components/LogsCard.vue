<template>
  <div>
    <div>

      <el-card>
        <div class="logs-container">
          <div v-for="(notif, index) in getNotifications" :key="index" :style="{color: notif.color}">
            >
            {{ notif.msg }}
            <br>
          </div>
        </div>
      </el-card>

    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'LogsCard',
  computed: {
    // mix the getters into computed with object spread operator
    ...mapGetters('notification', [
      'getNotifications'
    ])
  }
}
</script>

<style lang="scss" scoped>
.logs-container {
    background-color: #1f1c1a;
    height: 500px;
    padding:10px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: #f8fff8;
    font-weight: 700;
    font-family: monospace;
    overflow-y:auto;

}
</style>
