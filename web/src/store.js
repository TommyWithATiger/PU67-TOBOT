import Vue from 'vue'
import Vuex from 'vuex'

import { auth } from 'auth'

Vue.use(Vuex)

let theme = 'light'
let hue = 0x00ffff

try {
  if (localStorage.getItem('theme') === null) {
    localStorage.setItem('theme', theme)
  }

  if (localStorage.getItem('hue') === null) {
    localStorage.setItem('hue', hue)
  }

  theme = localStorage.getItem('theme')
  hue = localStorage.getItem('hue')
} catch (err) {}

export const store = new Vuex.Store({
  state: {
    user: {
      authenticated: auth.checkAuth(),
      username: '',
      usertype: '',
      email: ''
    },
    theme: theme,
    hue: hue
  }
})
