import Vue from 'vue'
import Rx from 'rxjs/Rx'
import VueRx from 'vue-rx'
import { router } from './router'
import { store } from './store'
import { auth } from './auth'

import App from './App'

Vue.use(VueRx, Rx)

/* eslint-disable no-new */
export const app = new Vue({
  el: '#app',
  data: { auth },
  store,
  router,
  template: '<App/>',
  components: { App }
})
