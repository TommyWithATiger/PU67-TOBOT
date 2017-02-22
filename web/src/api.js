import { API_URL } from './constants'

export const api = {
  /**
   * Get the user from API
   * @param {object} ctx Context.
   * @param {string} username Username.
   */
  getUser (ctx, username) {
    // Return the user from GET request.
  },

  /**
   * Post a user to the API.
   * @param {object} ctx Context.
   * @param {object} data The data to post in request.
   */
  postUser (ctx, data, callback) {
    // Here we can inject token.
    ctx.$http.post(`${API_URL}/user/login`, data, (data) => {
      console.log(data)
      callback(data)
    })
  }
}
