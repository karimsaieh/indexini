<template>
  <v-chart theme="macarons" style="width:100%" :options="polar" />

</template>

<script>
export default {
  name: 'LineChart',
  props: {
    data: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      polar: {
        xAxis: {
          data: this.data.map(item => item.x),
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['recherches']
        },
        series: [
          {
            name: 'recherches',
            smooth: true,
            type: 'line',
            itemStyle: {
              normal: {
                color: '#3888fa',
                lineStyle: {
                  color: '#3888fa',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            data: this.data.map(item => item.y),
            animationDuration: 2800,
            animationEasing: 'quadraticOut'
          }]
      }

    }
  }
}
</script>
