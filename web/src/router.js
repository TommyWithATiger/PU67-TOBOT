import Vue from 'vue'
import Router from 'vue-router'

import FrontPage from 'components/pages/FrontPage'
import LoginPage from 'components/pages/LoginPage'
import UserPage from 'components/pages/UserPage'
import TopicPage from 'components/pages/TopicPage'
import { auth } from 'auth'

Vue.use(Router)

/**
 * Function to check auth on spesific paths. Handling redirection.
 * @param {object} to The path to go to.
 * @param {object} from The path to go from.
 * @param {object} next The function which completes the redirection.
 */
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
      path: '/topic',
      name: 'Topics',
      component: TopicPage,
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
