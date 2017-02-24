<template>
  <div class="page-content">
    <h1>{{ title }}</h1>
    <div>
      <input @keydown.enter="login" v-model="creds.username" placeholder="Brukernavn ..." type="text" />
      <input @keydown.enter="login" v-model="creds.password" placeholder="Passord ..." type="password" />
      <button @click="login">Logg inn</button>
      <span>{{ feedback }}</span>
    </div>
  </div>
</template>

<script>
import { auth } from 'auth'

export default {
  name: 'loginpage',
  data () {
    return {
      title: 'Login',
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
        this.feedback = 'Sjekker validering ...'
        auth.login(this.creds, this, this.$route.query.redirect || '/', () => {
          this.successHandler('Valid login.')
        }, (err) => {
          this.errorHandler(err || 'Feil brukernavn eller passord.')
        })
      }
    }
  }
}
</script>

<style scoped>
</style>
