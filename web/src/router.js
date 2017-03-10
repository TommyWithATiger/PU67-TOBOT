import Vue from 'vue'
import Router from 'vue-router'

import FrontPage from 'components/pages/FrontPage'
import LoginPage from 'components/pages/LoginPage'
import RestrictedPage from 'components/pages/RestrictedPage'
import UserPage from 'components/pages/UserPage'
import TopicPage from 'components/pages/TopicPage'
import SubjectPage from 'components/pages/SubjectPage'
import RelateSubjectTopicPage from 'components/pages/RelateSubjectTopicPage'
import { auth } from 'auth'
import { api } from 'api'
import { store } from 'store'

Vue.use(Router)

/**
 * Function to check auth on spesific paths. Handling redirection.
 * @param {object} to The path to go to.
 * @param {object} from The path to go from.
 * @param {object} next The function which completes the redirection.
 */
function requireAuth (to, from, next) {
  if (auth.hasToken()) {
    if (/^\/login/.test(from.path)) {
      store.state.user.username = ''
      store.state.user.usertype = ''
    }

    auth.isAuth(() => {
      if (/^\/login|^\/restricted/.test(from.path)) {
        api.getUser(this, (data) => {
          store.state.user.username = data.username
          store.state.user.usertype = data.userType
          store.state.user.email = data.email

          if (to.meta.users.indexOf(data.userType) !== -1) {
            next()
          } else {
            next(getRestrictedRoute(to))
          }
        })
      } else {
        api.getUser(this, (data) => {
          store.state.user.username = data.username
          store.state.user.usertype = data.userType
          store.state.user.email = data.email
        })

        if (to.meta.users.indexOf(store.state.user.usertype) !== -1) {
          next()
        } else {
          if (store.state.user.usertype === 'undefined') {
            api.getUser(this, (data) => {
              store.state.user.username = data.username
              store.state.user.usertype = data.userType
              store.state.user.email = data.email

              if (to.meta.users.indexOf(store.state.user.usertype) !== -1) {
                next()
              } else {
                next(getRestrictedRoute(to))
              }
            })
          } else {
            next(getRestrictedRoute(to))
          }
        }
      }
    }, () => {
      next(getLoginRoute(to))
    })
  } else {
    next(getLoginRoute(to))
  }
}

/**
 * Get the next route for restricted login.
 * @param {object} to The route to go to. Needs fullPath.
 */
function getRestrictedRoute (to) {
  return {
    path: '/restricted',
    query: {
      redirect: to.fullPath
    }
  }
}

/**
 * Get the next route for login.
 * @param {object} to The route to go to. Needs fullPath.
 */
function getLoginRoute (to) {
  return {
    path: '/login',
    query: {
      redirect: to.fullPath
    }
  }
}

export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: FrontPage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/subject',
      name: 'Subjects',
      component: SubjectPage,
      meta: { users: [ 'Admin', 'Teacher' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/topic',
      name: 'Topics',
      component: TopicPage,
      meta: { users: [ 'Admin', 'Teacher' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/subject/:id',
      name: 'RelateSubjectTopic',
      component: RelateSubjectTopicPage,
      meta: { users: [ 'Admin', 'Teacher' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/user/:user',
      name: 'User',
      component: UserPage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginPage
    },
    {
      path: '/restricted',
      name: 'Restricted',
      component: RestrictedPage
    },
    {
      path: '/*',
      name: 'Default',
      component: FrontPage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: (to, from, next) => {
        next('/')
      }
    }
  ]
})
