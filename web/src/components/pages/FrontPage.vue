<template>
  <div class="page-content">
    <h1 v-if="username">Hei, {{ username }}!</h1>
    <h1 v-else>Velkommen</h1>
    <p v-if="usertype">Du har rollen som {{ usertype }}</p>
  </div>
</template>

<script>
import { auth } from 'auth'

export default {
  name: 'frontpage',
  data () {
    return {
      username: '',
      usertype: ''
    }
  },
  created () {
    this.username = auth.getUsername()
    this.usertype = auth.getUsertype()
  }
}
</script>

<style scoped>
</style>
