import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import i18n from './lang' // internationalization
import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log

import * as filters from './filters' // global filters
// my filters
import * as myfilters from './filters/global'

import ECharts from 'vue-echarts' // refers to components/ECharts.vue in webpack
Vue.component('v-chart', ECharts)
require('echarts/theme/macarons') // echarts theme

import VueApexCharts from 'vue-apexcharts'
Vue.component('apexchart', VueApexCharts)
import VueImg from 'v-img'
const vueImgConfig = {
  // Use `alt` attribute as gallery slide title
  altAsTitle: true
}
Vue.use(VueImg, vueImgConfig)
/**
 * If you don't want to use mock-server
 * you want to use mockjs for request interception
 * you can execute:
 *
 * import { mockXHR } from '../mock'
 * mockXHR()
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})
// register my global filters
Object.keys(myfilters).forEach(key => {
  Vue.filter(key, myfilters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
