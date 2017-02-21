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
     */
    logout () {
      auth.logout(this)
    }
  }
}
</script>

<style scoped>
</style>
