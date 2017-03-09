import { auth } from './auth'

import { API_URL } from './constants'
const USER_INFO_URL = `${API_URL}/user/info`
const USER_CHECK_URL = `${API_URL}/user/check`
const LOGIN_URL = `${API_URL}/user/login`

const TOPIC_GET_URL = `${API_URL}/topic/get`
const TOPIC_GET_TITLE_URL = `${API_URL}/topic/get/?title=`
const TOPIC_ADD_URL = `${API_URL}/topic/create`

const TOPIC_RATE_URL = `${API_URL}/rating/rate`

const TOPIC_GET_RATED_URL = `${API_URL}/rating/get`

const SUBJECT_GET_URL = `${API_URL}/subject/get`
const SUBJECT_GET_TITLE_URL = `${API_URL}/subject/get/?title=`
const SUBJECT_GET_ID_URL = `${API_URL}/subject/get/?id=`
const SUBJECT_ADD_URL = `${API_URL}/subject/create`
const SUBJECT_GET_RELATED_URL = `${API_URL}/subject/related/?id=`

const SUBJECT_TOPIC_RELATE_URL = `${API_URL}/subject/topic/relate`

export const api = {
  /**
   * Get the user from API.
   * @param {object} ctx Context.
   */
  getUser (ctx, callback, error) {
    let req = {
      body: ' '
    }

    return this.postRequest(ctx, USER_INFO_URL, req, callback, error)
  },

  /**
   * Get the user from API.
   * @param {object} ctx Context.
   */
  checkUser (ctx, callback, error) {
    let req = {
      body: {
        username: auth.getUsername(),
        token: auth.getToken()
      }
    }

    return this.postRequest(ctx, USER_CHECK_URL, req, callback, error)
  },

  /**
   * Get all subjects from API.
   * @param {object} ctx Context.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  getSubjects (ctx, callback, error) {
    return this.getRequest(ctx, SUBJECT_GET_URL, callback, error)
  },

  /**
   * Get all subjects from API based on a search term.
   * @param {object} ctx Context.
   * @param {string} term The search term to search for.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  getSubjectsByTitle (ctx, term, callback, error) {
    return this.getRequest(ctx, SUBJECT_GET_TITLE_URL + term, callback, error)
  },

  /**
   * Get topics related to a subject from the API
   * @param {object} ctx Context.
   * @param {function} callback Handle the request output.
   * @param {integer} id The subject id
   * @returns {Promise} A promise from the request.
   */
  getRelatedTopics (ctx, callback, error, id) {
    return this.getRequest(ctx, SUBJECT_GET_RELATED_URL + id, callback, error)
  },

  /**
   * Get the subject with the given id form API.
   * @param {object} ctx Context.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @param {integer} id The subject id.
   * @returns {Promise} A promise from the request.
   */
  getSubjectID (ctx, callback, error, id) {
    return this.getRequest(ctx, SUBJECT_GET_ID_URL + id, callback, error)
  },

  /**
   * Get all topics from API.
   * @param {object} ctx Context.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  getTopics (ctx, callback, error) {
    return this.getRequest(ctx, TOPIC_GET_URL, callback, error)
  },

  /**
   * Get all topics from API based on a search term.
   * @param {object} ctx Context.
   * @param {string} term The search term to search for.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  getTopicsByTitle (ctx, term, callback, error) {
    return this.getRequest(ctx, TOPIC_GET_TITLE_URL + term, callback, error)
  },

  /**
   * Post a user to the API.
   * @param {object} ctx Context.
   * @param {object} data The data to post in request.
   * @returns {Promise} A promise from the request.
   */
  postUserLogin (ctx, data, callback, error) {
    let req = {
      body: data
    }

    return this.postRequest(ctx, LOGIN_URL, req, callback, error)
  },

  /**
   * Add subject.
   * @param {object} ctx Context.
   * @param {object} subject The subject to post in request.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  addSubject (ctx, subject, callback, error) {
    let data = {
      title: subject.title,
      institution: subject.institution,
      subjectCode: subject.subjectCode,
      description: subject.description,
      username: auth.getUsername()
    }

    let req = {
      body: data
    }

    return this.postRequest(ctx, SUBJECT_ADD_URL, req, callback, error)
  },

  /**
   * Add topic.
   * @param {object} ctx Context.
   * @param {object} topic The topic to post in request.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  addTopic (ctx, topic, callback, error) {
    let data = {
      title: topic.title,
      description: topic.description,
      username: auth.getUsername()
    }

    let req = {
      body: data
    }

    return this.postRequest(ctx, TOPIC_ADD_URL, req, callback, error)
  },

  /**
   * Rate a topic
   * @param {object} ctx Context.
   * @param {number} id The topic id to rate.
   * @param {string} rating The rating to give the topic.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  rateTopic (ctx, id, rating, callback, error) {
    let data = {
      topicID: id,
      rating: rating
    }

    let req = {
      body: data
    }

    return this.postRequest(ctx, TOPIC_RATE_URL, req, callback, error)
  },

  /**
   * Get all topics rated by the user
   * @param {object} ctx Context.
   * @param {function} callback Hande the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  getRatedTopics (ctx, callback, error) {
    let req = {
      body: ' '
    }

    return this.postRequest(ctx, TOPIC_GET_RATED_URL, req, callback, error)
  },

  /**
   * Relate subject and topic
   * @param {object} ctx Context.
   * @param {object} topic The topic to relate
   * @param {object} subject The subject to relate
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  relateSubjectTopic (ctx, topic, subject, callback, error) {
    let data = {
      subjectID: subject.id,
      topicID: topic.id
    }

    let req = {
      body: data
    }

    return this.postRequest(ctx, SUBJECT_TOPIC_RELATE_URL, req, callback, error)
  },

  /**
   * General function for posting requests to the API.
   * @param {string} url URL address to send to.
   * @param {object} req Data to send with the POST-request.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  postRequest (ctx, url, req, callback, error) {
    req.headers = Object.assign({}, req.headers || {}, {
      'Authorization': auth.getAuthHeader()['Authorization'],
      'X-Username': auth.getUsername()
    })

    req.method = 'POST'
    let h = req.headers
    req.method = 'POST'
    req.headers = new Headers(h)

    req.body = typeof req.body === 'string'
      ? req.body
      : JSON.stringify(req.body)

    // ctx.$http.post(url, req.body, req.headers)
    return fetch(new Request(url, req))
    .then(res => res.json())
    .then(callback)
    .catch(error)
  },

  /**
   * General function for getting requests from the API.
   * @param {string} url URL address to send to.
   * @param {function} callback Handle the request output.
   * @param {function} error Feedback error.
   * @returns {Promise} A promise from the request.
   */
  getRequest (ctx, url, callback, error) {
    return ctx.$http.get(url)
    .then(res => res.json())
    .then(callback)
    .catch(error)
  }
}
