<template>
  <div id="chart">
    <apexchart type="radar" :options="chartOptions" :series="series" />
  </div>
</template>

<script>
export default {
  name: 'FileRadarChart',
  props: {
    seriesData: {
      type: Array,
      default: function() {
        return []
      }
    },
    ldaTopicsDescription: {
      type: Array,
      default: function() {
        return []
      }
    }
  },
  data: function() {
    return {
      series: [
        {
          name: 'Topics',
          data: this.generatePourcentages()
        }
      ],
      chartOptions: {
        labels: this.generateLabels(),
        dataLabels: {
          style: {
            fontSize: '14px',
            fontFamily: 'Helvetica, Arial, sans-serif',
            colors: undefined
          }
        },
        markers: {
          size: 10,
          hover: {
            size: 15
          }
        },
        tooltip: {
          x: {
            show: false
          },
          y: {
            show: false,
            formatter: val => val + '%',
            title: {
              formatter: (seriesName, series) => {
                // TODO : enhancement:  could use cache
                const index = series.dataPointIndex
                const topic = this.ldaTopicsDescription[index]
                console.log(topic.description)
                return topic.description.join(', ')
              }
            }
          }
        }
      }
    }
  },
  methods: {
    generateLabels: function() {
      const labels = []
      for (let i = 0; i < this.seriesData.length; i++) {
        labels.push('Topic ' + (i + 1))
      }
      return labels
    },
    generatePourcentages: function() {
      return this.seriesData.map(x => Math.round(x * 100))
    }
  }
}
</script>
<style scoped>
</style>

