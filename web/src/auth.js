import { api } from 'api'

export const auth = {
  /**
   * Fetch token by using credentials.
   * @param {object} creds Object which contains user credentials.
   * @param {object} ctx Context making it possible to access store and router.
   * @param {string} redirect Optional path to redirect. Default root.
   * @param {function} callback Optional callback if spescified.
   * @param {function} error Optional error handler if spescified.
   */
  login (creds, ctx, redirect = '/', callback, error) {
    if (creds.username.length) {
      api.postUserLogin(ctx, {
        username: creds.username,
        password: creds.password
      }, data => {
        if (data.message) {
          this.errorHandler(data, error)
        } else {
          if (callback) callback(data)
          localStorage.setItem('app_token', data.token)
          localStorage.setItem('username', data.username)
          if (ctx) ctx.$store.state.user.authenticated = true
          if (ctx) ctx.$store.state.user.username = data.username
          if (ctx) ctx.$router.push(redirect)
        }
      }, err => {
        this.errorHandler(err, error)
      })
    }
  },

  /**
   * Signing in.
   * If success - make user authenticated.
   * If redirect spesified - redirect.
   * @param {object} creds Object which contains user credentials.
   * @param {object} ctx Context making it possible to access store and router.
   * @param {string} redirect Optional path to redirect. Default root.
   * @param {function} callback Optional callback if spescified.
   * @param {function} error Optional error handler if spescified.
   */
  signup (creds, ctx, redirect = '/', callback, error) {
    if (creds.username.length) {
      api.postUserLogin(ctx, {
        username: creds.username,
        password: creds.password
      }, data => {
        if (data.message) {
          this.errorHandler(data, error)
        } else {
          if (callback) callback(data)
          localStorage.setItem('app_token', data.token)
          localStorage.setItem('username', data.username)
          if (ctx) ctx.$store.state.user.authenticated = true
          if (ctx) ctx.$store.state.user.username = data.username
          if (ctx) ctx.$router.push(redirect)
        }
      }, err => {
        this.errorHandler(err, error)
      })
    }
  },

  /**
   * Fetch error handler.
   * @param {object} error Error from feedback.
   * @param {function} callback The function to return to.
   * @returns {string} The message to tell user what happened.
   */
  errorHandler (error, callback = null) {
    let msg = ''
    if (error && error.message) {
      switch (error.message) {
        case 'Failed to fetch':
          msg = 'Kunne ikke koble til serveren.'
          break
        case 'Unexpected end of JSON input':
          msg = 'Feil brukernavn eller passord.'
          break
        default:
          msg = 'Klarte ikke å logge inn.'
          break
      }
    }

    if (callback) callback(msg)
    return msg
  },

  /**
   * To log out, we just need to remove the token.
   * @param {object} ctx Context making it possible to access store and router.
   */
  logout (ctx) {
    try {
      localStorage.removeItem('app_token')
      localStorage.removeItem('username')
    } catch (exception) {}
    if (ctx) ctx.$store.state.user.authenticated = false
  },

  /**
   * Testing if the user is authenticated.
   * @param {object} ctx Context making it possible to access store and router.
   */
  checkAuth (ctx) {
    try {
      let jwt = localStorage.getItem('app_token')
      if (ctx) ctx.$store.state.user.authenticated = !!jwt
    } catch (exception) {
      if (ctx) ctx.$store.state.user.authenticated = false
    }
  },

  /**
   * Returning if the user is authenticated.
   * @param {function} success If the auth succeeded.
   * @param {function} failed If the auth failed.
   * @returns {boolean} Token isset.
   */
  isAuth (success, failed) {
    try {
      if (localStorage.getItem('app_token')) {
        api.checkUser(null, (data) => {
          if (data.logged_in) success()
          else failed()
        }, () => {
          failed()
        })
      } else {
        failed()
      }
    } catch (exception) {
      failed()
    }
  },

  /**
   * Returning if the user has a token.
   * @returns {boolean} Token isset.
   */
  hasToken () {
    try {
      return !!localStorage.getItem('app_token')
    } catch (exception) {
      return false
    }
  },

  /**
   * Returning the username if the user is authenticated.
   * @returns {string} Username.
   */
  getUsername () {
    try {
      return !!localStorage.getItem('app_token') && localStorage.getItem('username')
    } catch (exception) {
      return false
    }
  },

  /**
   * Returning the usertype if the user is authenticated.
   * @returns {string} Usertype.
   */
  getUsertype () {
    try {
      return !!localStorage.getItem('app_token') && localStorage.getItem('usertype')
    } catch (exception) {
      return false
    }
  },

  /**
   * Returning if the usertype is admin.
   * @returns {string} Is admin.
   */
  isAdmin () {
    return this.getUsertype() === 'admin'
  },

  /**
   * Returning if the usertype is teacher.
   * @returns {string} Is teacher.
   */
  isTeacher () {
    return this.getUsertype() === 'teacher'
  },

  /**
   * Returning if the usertype is student.
   * @returns {string} Is student.
   */
  isStudent () {
    return this.getUsertype() === 'student'
  },

  /**
   * Get the auth token saved in localStorage.
   * @returns {string} The token in plain text.
   */
  getToken () {
    try {
      return localStorage.getItem('app_token')
    } catch (exception) {
      return ''
    }
  },

  /**
   * The object to be passed as a header for authenticated requests.
   * @returns {object} Header object.
   */
  getAuthHeader () {
    try {
      return {
        'Authorization': 'Bearer ' + localStorage.getItem('app_token')
      }
    } catch (exception) {
      return {
        'Authorization': 'Failed'
      }
    }
  }
}
