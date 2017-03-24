import Vue from 'vue'
import Router from 'vue-router'

import FrontPage from 'components/pages/FrontPage'
import LoginPage from 'components/pages/LoginPage'
import RestrictedPage from 'components/pages/RestrictedPage'
import UserPage from 'components/pages/UserPage'
import TopicsPage from 'components/pages/TopicsPage'
import SubjectsPage from 'components/pages/SubjectsPage'
import TopicPage from 'components/pages/TopicPage'
import SearchPage from 'components/pages/SearchPage'
import RegisterPage from 'components/pages/RegisterPage'
import RequestResetPage from 'components/pages/RequestResetPage'
import PasswordResetPage from 'components/pages/ResetPasswordPage'
import RelateSubjectTopicPage from 'components/pages/RelateSubjectTopicPage'
import ReferencePage from 'components/pages/ReferencePage'
import UploadPDFPage from 'components/pages/UploadPDFPage'
import ExerciseCreationPage from 'components/pages/ExerciseCreationPage'
import ReferenceUpload from 'components/pages/ReferenceUploadPage'
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
    if (/^\/login|^\/register/.test(from.path)) {
      store.state.user.username = ''
      store.state.user.usertype = ''
    }

    auth.isAuth(() => {
      if (/^\/login|^\/register|^\/restricted/.test(from.path)) {
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
          if (store.state.user.usertype === 'undefined' || store.state.user.usertype === null) {
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
      name: 'Subject',
      component: SubjectsPage,
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
      path: '/reference/:id',
      name: 'Reference',
      component: ReferencePage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/reference',
      name: 'ReferenceUpload',
      component: ReferenceUpload,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/topic',
      name: 'Topics',
      component: TopicsPage,
      meta: { users: [ 'Admin', 'Teacher' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/topic/:id',
      name: 'Topic',
      component: TopicPage,
      meta: { users: [ 'Admin', 'Teacher' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/search',
      name: 'SearchPage',
      component: SearchPage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/search/:term',
      name: 'Search',
      component: SearchPage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
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
      path: '/upload',
      name: 'UploadPDF',
      component: UploadPDFPage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/exercise/create',
      name: 'ExerciseCreate',
      component: ExerciseCreationPage,
      meta: { users: [ 'Admin', 'Teacher', 'Student' ] },
      beforeEnter: requireAuth
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginPage
    },
    {
      path: '/register',
      name: 'Register',
      component: RegisterPage
    },
    {
      path: '/reset/request',
      name: 'RequestReset',
      component: RequestResetPage
    },
    {
      path: '/reset/:token/:email',
      name: 'PasswordReset',
      component: PasswordResetPage
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
