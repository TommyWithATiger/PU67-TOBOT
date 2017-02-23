import { API_URL } from './constants'
// const USER_URL = `${API_URL}/user/`
const LOGIN_URL = `${API_URL}/user/login`
// const TOPIC_GET_URL = `${API_URL}/topic/get`
const TOPIC_ADD_URL = `${API_URL}/topic/create`

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

    this.postRequest(LOGIN_URL, req, callback, error)
  }

  /**
   * Add topic.
   * @param {object} ctx Context.
   * @param {object} data The data to post in request.
   */
  addTopic (ctx, topic, callback, error) {
    // Here we can inject token.

    let data = {
      title: topic.title
      description: topic.description
    }

    let req = {
      body: JSON.stringify(data),
      method: 'POST',
      mode: 'no-cors'
    }

    postRequest (TOPIC_ADD_URL, req, callback, error) {
  }

  /**
   * General function for posting requests to the API.
   * @param {string} url URL address to send to.
   * @param {object} req Data to send with the post request.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   */
  postRequest (url, req, callback, error) {
    // ctx.$http.post(LOGIN_URL, req)
    fetch(url, req) // Does not work in IE, needs polyfill.
    .then(res => res.json())
    .then(callback)
    .catch(error)
  }
}
