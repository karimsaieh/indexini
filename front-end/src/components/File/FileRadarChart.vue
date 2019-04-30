<template>
  <div id="chart">

    <apexchart
      type="radar"
      height="280"

      :options="chartOptions"
      :series="series"
    />
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
      topic: '',
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
          },
          plotOptions: {
            radar: {
              size: 140,
              polygons: {
                strokeColor: '#e9e9e9',
                fill: {
                  colors: ['#f8f8f8', '#fff']
                }
              }
            }
          }
        },
        markers: {
          size: 4,
          hover: {
            size: 7
          }
        },
        yaxis: {
          show: false,
          max: 100

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
                const topic = this.ldaTopicsDescription.find(x => +x.id === index)
                // console.log(topic.description)
                this.topic = topic.description.join(', ')
                this.topic = series.series[0][series.dataPointIndex] + '% ' + this.topic
                this.$emit('update:topic', this.topic)
                console.log()
                return ''
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

