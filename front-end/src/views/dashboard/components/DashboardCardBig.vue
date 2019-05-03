<template>
  <div>

    <el-card style="font-weight: bold">
      <div style="text-align:center">
        <div style="    font-size: 2.8rem;">
          <i :class="icon" :style="{color: color}" />
        </div>
        <div class="card-panel-text" style="margin-top: 10px">{{ title }}</div>
        <div class="card-panel-num">
          <count-to :start-val="0" :end-val="number" :duration="duration" />
        </div>

      </div>
    </el-card>

  </div>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  name: 'DashboardCardBig',
  components: {
    CountTo
  },
  props: {
    title: {
      type: String,
      default: ''
    },
    number: {
      type: Number,
      default: 0
    },
    duration: {
      type: Number,
      default: 2600
    },
    color: {
      type: String,
      default: ''
    },
    icon: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="scss" scoped>
.number {
  color: #666;
  font-weight: 900;
  font-size: 30px
}

.a-card {
  height: 100px;
}
  .icon-file {
    color: rebeccapurple
  }
  .card-icon {
    font-size: 2.8rem;
    float: left;
    margin: 5px 0px 10px 0px;
  }
  .card-description {
    float: right;
    font-weight: bold;

  }
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        color: #666;
        font-size: 20px;
      }
</style>

