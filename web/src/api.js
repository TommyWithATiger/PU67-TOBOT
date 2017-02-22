import { API_URL } from './constants'
// const USER_URL = `${API_URL}/user/`
const LOGIN_URL = `${API_URL}/user/login`

export const api = {
  /**
   * Get the user from API
   * @param {object} ctx Context.
   * @param {string} username Username.
   */
  getUser (ctx, username) {
    // Return the user from GET request.
    return {
      username: 'ole',
      firstName: 'Ola',
      lastName: 'Nordmann'
    }
  },

  /**
   * Post a user to the API.
   * @param {object} ctx Context.
   * @param {object} data The data to post in request.
   */
  postUserLogin (ctx, data, callback, error) {
    // Here we can inject token.

    let req = {
      body: JSON.stringify(data),
      method: 'POST',
      mode: 'no-cors'
    }

    ctx.$http.post(LOGIN_URL, req)
    .then(res => res.json())
    .then(callback)
    .catch(error)
  }
}
