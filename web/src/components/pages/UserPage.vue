<template>
  <div class="page-content">
    <h1 v-if="fullName.length">{{ fullName }}</h1>
    <h1 v-else>User has no name</h1>
    <p>Username: {{ user.username }}</p>
  </div>
</template>

<script>
export default {
  name: 'userpage',
  data () {
    return {
      user: {
        username: '',
        firstName: '',
        lastName: ''
      }
    }
  },
  created () {
    this.user.username = this.$store.state.user.username
  },
  computed: {
    fullName () {
      return `${this.user.firstName || ''} ${this.user.lastName || ''}`.trim()
    }
  }
}
</script>

<style scoped>
</style>
