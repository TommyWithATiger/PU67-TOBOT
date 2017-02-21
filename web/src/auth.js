export const auth = {
  /**
   * Fetch token by using credentials.
   * @param {object} creds Object which contains user credentials.
   * @param {object} ctx Context making it possible to access store and router.
   * @param {string} redirect Optional path to redirect. Default root.
   */
  login (creds, ctx, redirect = '/') {
    if (creds.username.length) {
      localStorage.setItem('app_token', 'anId')
      localStorage.setItem('username', creds.username)
      if (ctx) ctx.$store.state.user.authenticated = true
      if (ctx) ctx.$store.state.user.username = creds.username
      if (ctx) ctx.$router.push(redirect)
    }
    /*
    ctx.$http.post(LOGIN_URL, creds, (data) => {
      try {
        localStorage.setItem('app_token', data.id_token)
      } catch (exception) {}

      ctx.$store.state.user.authenticated = true

      if (redirect) {
        Router.go(redirect)
      }

    }).error((err) => {
      this.error = err
    })
    */
  },

  /**
   * Signing in.
   * If success - make user authenticated.
   * If redirect spesified - redirect.
   * @param {object} creds Object which contains user credentials.
   * @param {object} ctx Context making it possible to access store and router.
   * @param {string} redirect Optional path to redirect. Default root.
   */
  signup (creds, ctx, redirect = '/') {
    if (creds.username.length) {
      localStorage.setItem('app_token', 'anId')
      localStorage.setItem('username', creds.username)
      if (ctx) ctx.$store.state.user.authenticated = true
      if (ctx) ctx.$store.state.user.username = creds.username
      if (ctx) ctx.$router.push(redirect)
    }
    /*
    ctx.$http.post(SIGNUP_URL, creds, (data) => {
      try {
        localStorage.setItem('app_token', data.id_token)
      } catch (exception) {}

      if (ctx) ctx.$store.state.user.authenticated = true

      if(redirect) {
        Router.go(redirect)
      }

    }).error((err) => {
      this.error = err
    })
    */
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
