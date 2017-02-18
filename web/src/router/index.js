import Vue from 'vue'
import Router from 'vue-router'
import FrontPage from 'components/pages/FrontPage'
import LoginPage from 'components/pages/LoginPage'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: FrontPage
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginPage
    }
  ]
})
