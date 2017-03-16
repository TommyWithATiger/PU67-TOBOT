import Vue from 'vue'
import Vuex from 'vuex'

import { auth } from 'auth'

Vue.use(Vuex)

let theme = 'light'

try {
  if (localStorage.getItem('theme') === null) {
    localStorage.setItem('theme', theme)
  }

  theme = localStorage.getItem('theme')
} catch (err) {}

export const store = new Vuex.Store({
  state: {
    user: {
      authenticated: auth.checkAuth(),
      username: '',
      usertype: '',
      email: ''
    },
    theme: theme
  }
})
