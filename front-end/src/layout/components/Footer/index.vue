<template>
  <div class="wrapper">
    <div class="footer">
      <el-row :gutter="20">
        <el-col :span="24">
          <component :is="toggled" />
        </el-col>
        <!-- <el-col :span="4" align="start">
          <span class="toggle" @click="toggle()">
            {{ toggled }}
          </span>
        </el-col> -->
      </el-row>
    </div>
  </div>
</template>

<script>
import FooterNotificationBarMini from './components/FooterNotificationBarMini'
import FooterNotificationBarLarge from './components/FooterNotificationBarLarge'

export default {
  name: 'AppMain',
  components: {
    FooterNotificationBarMini,
    FooterNotificationBarLarge
  },
  data: function() {
    return {
      toggled: 'FooterNotificationBarMini'
    }
  },
  computed: {
  },
  methods: {
    toggle() {
      this.toggled = this.toggled === 'FooterNotificationBarMini' ? 'FooterNotificationBarLarge' : 'FooterNotificationBarMini'
    }
  }
}
</script>

<style scoped>

.wrapper {
  bottom: 0px;
  position: fixed;
  width:100%;
  background-color:#fcf9ff;
}
</style>

