<template>
  <div class="page-content">
    <h1>{{ title }}</h1>
    <div class="login-container">
      <input @keydown.enter="login" v-model="creds.username" placeholder="Brukernavn ..." type="text" />
      <input @keydown.enter="login" v-model="creds.password" placeholder="Passord ..." type="password" />
      <button @click="login" :disabled="creds.username.length && creds.password.length ? null : 'true'">Logg inn</button>
      <span>{{ feedback }}</span>
    </div>
    <a href="/register">Register account</a>
    <a href="/reset/request">Forgotten password?</a>
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
          if (typeof err === 'string') this.errorHandler(err)
        })
      }
    }
  }
}
</script>

<style scoped>
.page-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-bottom: 160px;
  align-items: center;
}

.login-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 240px;
}

.login-container > button {
  margin: 10px;
}

a {
  margin: 8px;
}
</style>
