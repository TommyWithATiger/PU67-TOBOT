import Vue from 'vue'
import Router from 'vue-router'

import FrontPage from 'components/pages/FrontPage'
import LoginPage from 'components/pages/LoginPage'
import UserPage from 'components/pages/UserPage'
import { auth } from 'auth'

Vue.use(Router)

function requireAuth (to, from, next) {
  // Remember to check token with server!
  if (auth.isAuth()) {
    next()
  } else {
    next({
      path: '/login',
      query: {
        redirect: to.fullPath
      }
    })
  }
}

export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: FrontPage,
      beforeEnter: requireAuth
    },
    {
      path: '/user/:user',
      name: 'User',
      component: UserPage,
      beforeEnter: requireAuth
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginPage
    },
    {
      path: '/*',
      name: 'Default',
      component: FrontPage,
      beforeEnter: (to, from, next) => {
        next('/')
      }
    }
  ]
})
