<template>
  <button @click="login">Logg inn</button>
</template>

<script>
import { auth } from 'auth'

export default {
  name: 'loginbutton',
  props: [ 'creds', 'redirect', 'error', 'success' ],
  methods: {
    /**
     * Send a request to the login URL and save the returned JWT.
     */
    login () {
      if (this.creds.username.length) {
        auth.login(this.creds, this, this.redirect, () => {
          if (this.success) this.success('Valid login.')
        }, () => {
          if (this.error) this.error('Feil brukernavn eller passord.')
        })
      }
    }
  }
}
</script>

<style scoped>
</style>
