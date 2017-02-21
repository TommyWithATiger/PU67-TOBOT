import Vue from 'vue'
import Vuex from 'vuex'

import { auth } from 'auth'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    user: {
      authenticated: auth.checkAuth(),
      username: ''
    }
  }
})
