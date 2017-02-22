<template>
  <div class="page-content">
    <h1>{{ fullName }}</h1>
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
  computed: {
    fullName () {
      return `${this.firstName} ${this.lastName}`
    }
  }
}
</script>

<style scoped>
</style>
