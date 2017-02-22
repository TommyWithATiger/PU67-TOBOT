<template>
  <div class="page-content">
    <h1 v-if="fullName.length">{{ fullName }}</h1>
    <h1 v-else>User has no name</h1>
    <p>Username: {{ username }}</p>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'userpage',
  data () {
    return {
      user: {
        firstName: '',
        lastName: ''
      }
    }
  },
  created () {
    api.getUser(this.user.username, (result) => {
      this.$store.state.user.username = result.username
      this.firstName = result.firstName
      this.lastName = result.lastName
    })
  },
  computed: {
    fullName () {
      return `${this.user.firstName || ''} ${this.user.lastName || ''}`.trim()
    },
    username () {
      return this.$store.state.user.username
    }
  }
}
</script>

<style scoped>
</style>
