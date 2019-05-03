<template>
  <div>

    <div class="fixed-width-page-parent-container">
      <div class="fixed-width-page-container">

        <el-row :gutter="40">
          <el-col :span="6">
            <dashboard-card-big
              icon="fas el-icon-fa-search el-icon-fa-7x icon-file"
              color="#2ec7c9"
              title="Recherches"
              :duration="3000"
              :number="searchCount"
            />
          </el-col>

          <el-col :span="6">
            <dashboard-card-big
              title="Total des fichiers"
              icon="far el-icon-fa-file-alt el-icon-fa-7x icon-file"
              color="#8d98b3"
              :duration="4000"
              :number="indexingStats.files"
            />
          </el-col>

          <el-col :span="6">
            <dashboard-card-big
              title="Fichiers Non Indexés"
              icon="far el-icon-fa-file el-icon-fa-7x icon-file"
              color="#d87a80"
              :duration="2200"
              :number="indexingStats.notIndexed"
            />
          </el-col>

          <el-col :span="6">
            <dashboard-card-big
              title="Fichiers Indexés"
              icon="fas el-icon-fa-file-invoice el-icon-fa-7x icon-file"
              color="#97b552"
              :duration="2200"
              :number="indexingStats.files - indexingStats.notIndexed"
            />
          </el-col>

        </el-row>
        <br>
        <el-row :gutter="30">

          <el-col :span="7">
            <el-row>
              <el-col :span="24"><dashboard-card-small
                icon="fas el-icon-fa-hammer el-icon-fa-7x"
                color="#e5cf0d"
                title="Opérations d'indexation"
                :duration="1500"
                :number="sparkStats.numberOfJobs"
              /></el-col>
            </el-row>
            <br>
            <el-row>
              <el-col :span="24"><dashboard-card-small
                icon="fas el-icon-fa-calendar-day el-icon-fa-7x"
                color="#ffb980"
                title="Dernière Indexation"
                :duration="3000"
                :alt-text="sparkStats.lastJobDate | formatDateFromISO8601"
              /></el-col>
            </el-row>
            <br>
            <el-row>
              <el-col :span="24"><dashboard-card-small
                icon="fas el-icon-fa-percentage el-icon-fa-7x"
                color="#5ab1ef"
                title="Précsion de suggestion"
                :duration="2500"
                number-suffix="%"
                :number="sparkStats.currentSuggestionPrecision"
              /></el-col>
            </el-row>
            <br>
            <el-row>
              <el-col :span="24"><dashboard-card-small
                icon="fas el-icon-fa-list-ol el-icon-fa-7x"
                color="#b6a2de"
                title="Nombre de topics"
                :duration="3600"
                :number="sparkStats.currentTopicsNumber"
              /></el-col>
            </el-row>
          </el-col>

          <el-col :span="17">
            <el-card style="height:456px">
              <el-radio-group v-model="historyRangeOption" size="small" @change="onAggRangeChanged">
                <el-radio-button label="H" />
                <el-radio-button label="J" />
                <el-radio-button label="S" />
                <el-radio-button label="M" />
                <el-radio-button label="A" />
              </el-radio-group>
              <template v-if="historyAggData.length !==0">
                <line-chart :key="lineChartKey" :data="historyAggData" />
              </template>
            </el-card>
          </el-col>
        </el-row>

        <br>
        <el-row :gutter="30">
          <el-col :span="12">
            <el-card

              :body-style="zeroPaddingCardStyle"
              style="height:400px"
            >
              <template v-if="indexingStats.fileTypes.length!==0">
                <pie-chart :data="indexingStats.fileTypes" />
              </template>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <dashboard-history-table />
            </el-card>
          </el-col>
        </el-row>

      </div>
    </div>
    <br><br><br><br>
  </div>

</template>

<script>
import LineChart from './components/LineChart'
import { indexingStats } from '@/api/file'
import { sparkStats } from '@/api/spark-manager'
import { countSearch, history, historyAgg } from '@/api/search'
import DashboardCardBig from './components/DashboardCardBig'
import DashboardCardSmall from './components/DashboardCardSmall'
import PieChart from './components/PieChart'
import DashboardHistoryTable from './components/DashboardHistoryTable'

export default {
  name: 'Dashboard',
  components: {
    LineChart,
    DashboardCardBig,
    DashboardCardSmall,
    PieChart,
    DashboardHistoryTable
  },
  data() {
    return {
      indexingStats: {
        files: 0,
        notIndexed: 0,
        fileTypes: []
      },
      sparkStats: {
        numberOfJobs: 0,
        lastJobDate: 'nothing',
        currentTopicsNumber: 0,
        currentSuggestionPrecision: 0
      },
      lineChartData: {
        actualData: [120, 82, 91, 154, 162, 140, 140548485]
      },
      searchCount: 0,
      historyData: {},
      historyAggData: [],
      zeroPaddingCardStyle: {
        padding: '0px'
      },
      historyRangeOption: 'M',
      historyRange: 'lastMonth',
      lineChartKey: new Date().getTime()
    }
  },
  created() {
    indexingStats().then((result) => {
      this.indexingStats = Object.assign({}, this.indexingStats, result.data)
      this.indexingStats.fileTypes = this.indexingStats.fileTypes.map(x => {
        return {
          'name': x.contentType,
          'value': x.count
        }
      })
    })
    sparkStats().then((result) => {
      this.sparkStats = Object.assign({}, this.sparkstats, result.data)
      this.sparkStats.lastJobDate = new Date(this.sparkStats.lastJobDate)
    })
    countSearch().then((result) => {
      this.searchCount = result.data
    })
    history({ page: 0, size: 10 }).then((result) => {
      this.historyData = result.data
    })
    historyAgg({ range: this.historyRange }).then((result) => {
      this.historyAggData = result.data
      this.historyAggData = result.data.map(item => {
        item.x = new Date(item.x).getDay() - 2
        return item
      })
    })
  },
  methods: {
    onAggRangeChanged(to) {
      let dateFromatter = null
      if (to === 'H') {
        this.historyRange = 'lastHour'
        dateFromatter = (date) => date.getMinutes()
      }
      if (to === 'J') {
        this.historyRange = 'lastDay'
        dateFromatter = (date) => date.getHours()
      }
      if (to === 'S') {
        this.historyRange = 'lastWeek'
        dateFromatter = (date) => date.getDay() - 2
      }
      if (to === 'M') {
        this.historyRange = 'lastMonth'
        dateFromatter = (date) => date.getDay() - 2
      }
      if (to === 'A') {
        this.historyRange = 'lastYear'
        dateFromatter = (date) => date.getMonth() + 1
      }
      this.historyAggData = []
      historyAgg({ range: this.historyRange }).then((result) => {
        this.historyAggData = result.data.map(item => {
          item.x = dateFromatter(new Date(item.x))
          return item
        })
        this.lineChartKey = new Date().getTime()
      })
    }
  }
}

</script>

<style lang="scss" scoped>

</style>

