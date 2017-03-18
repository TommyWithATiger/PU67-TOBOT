<template>
  <div class="page-content">
    <h1>Not allowed. Log in with another user:</h1>
    <div>
      <input @keydown.enter="login" v-model="creds.username" placeholder="Username ..." type="text" />
      <input @keydown.enter="login" v-model="creds.password" placeholder="Password ..." type="password" />
      <button @click="login">Login</button>
      <span>{{ feedback }}</span>
    </div>
  </div>
</template>

<script>
import { auth } from 'auth'

export default {
  name: 'restrictedpage',
  data () {
    return {
      creds: {
        username: '',
        password: ''
      },
      feedback: ''
    }
  },
  methods: {
    successHandler (feedback) {
      this.feedback = feedback
    },
    errorHandler (feedback) {
      this.feedback = feedback
    },
    /**
     * Send a request to the login URL and save the returned JWT.
     */
    login () {
      if (this.creds.username.length) {
        this.feedback = 'Validating ...'
        auth.login(this.creds, this, this.$route.query.redirect || '/', () => {
          this.successHandler('Valid login.')
        }, (err) => {
          if (typeof err === 'string') this.errorHandler(err)
        })
      }
    }
  }
}
</script>

<style scoped>
</style>
