<template>
  <button @click="logout">Logg ut</button>
</template>

<script>
import { auth } from 'auth'

export default {
  name: 'logoutbutton',
  methods: {
    /**
     * To log out, we just need to remove the token.
     * After logout it redirects to /login.
     */
    logout () {
      auth.logout(this)
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
</style>
