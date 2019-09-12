<template>
  <v-chart theme="macarons" style="width:100%" :options="polar" />
</template>

<script>
export default {
  name: 'PieChart',
  props: {
    data: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      polar: {

        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'horizontal',
          bottom: 20,
          data: this.data.map(x => x.name)

        },
        series: [
          {
            roseType: 'radius',
            radius: [20, 110],
            name: 'type',
            type: 'pie',
            center: ['50%', '45%'],
            data: this.data,
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
    }
  }
}
</script>

