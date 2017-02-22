<template>
  <button @click="login">Logg inn</button>
</template>

<script>
import { auth } from 'auth'

export default {
  name: 'loginbutton',
  props: [ 'creds', 'redirect' ],
  methods: {
    /**
     * Send a request to the login URL and save the returned JWT.
     */
    login () {
      if (this.creds.username.length) {
        auth.login(this.creds, this, this.redirect)
      }
    }
  }
}
</script>

<style scoped>
</style>
