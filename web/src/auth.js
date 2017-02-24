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
      }, (data) => {
        if (data.message && data.message === 'Failed to fetch') {
          error('Kunne ikke koble til serveren.')
        } else {
          if (callback) callback(data)
          localStorage.setItem('app_token', data.token)
          localStorage.setItem('username', data.username)
          if (ctx) ctx.$store.state.user.authenticated = true
          if (ctx) ctx.$store.state.user.username = creds.username
          if (ctx) ctx.$router.push(redirect)
        }
      }, error)
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
      }, (data) => {
        if (data.message && data.message === 'Failed to fetch') {
          error('Kunne ikke koble til serveren.')
        } else {
          if (callback) callback(data)
          localStorage.setItem('app_token', data.token)
          localStorage.setItem('username', data.username)
          if (ctx) ctx.$store.state.user.authenticated = true
          if (ctx) ctx.$store.state.user.username = creds.username
          if (ctx) ctx.$router.push(redirect)
        }
      }, error)
    }
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
   */
  isAuth () {
    try {
      return !!localStorage.getItem('app_token')
    } catch (exception) {
      return false
    }
  },

  /**
   * Returning the username if the user is authenticated.
   */
  getUsername () {
    try {
      return !!localStorage.getItem('app_token') && localStorage.getItem('username')
    } catch (exception) {
      return false
    }
  },

  /**
   * The object to be passed as a header for authenticated requests.
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
