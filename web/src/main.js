import Vue from 'vue'
import Rx from 'rxjs/Rx'
import VueRx from 'vue-rx'
import VueResource from 'vue-resource'
import Highcharts from 'highcharts/js/highcharts'
import 'highcharts/css/highcharts.css'
import VueHighcharts from 'vue-highcharts'
import { router } from './router'
import { store } from './store'
import { auth } from './auth'

import App from './App'

Vue.use(VueRx, Rx)
Vue.use(VueResource)
Vue.use(VueHighcharts, { Highcharts: Highcharts })

/* eslint-disable no-new */
export const app = new Vue({
  el: '#app',
  data: { auth },
  store,
  router,
  template: '<App/>',
  components: { App }
})
