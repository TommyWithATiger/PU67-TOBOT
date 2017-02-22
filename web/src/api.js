import { API_URL } from './constants'

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
  postUser (ctx, data, callback) {
    // Here we can inject token.

    let req = {
      body: JSON.stringify(data),
      method: 'POST',
      mode: 'no-cors'
    }

    fetch(`${API_URL}/user/login`, req)
    .then(res => res.json())
    .then(callback)
    .catch(console.log)
  }
}
