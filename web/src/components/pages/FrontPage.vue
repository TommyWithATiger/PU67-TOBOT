<template>
  <div class="page-content">
    <h1 v-if="username">Hei, {{ username }}!</h1>
    <h1 v-else>Velkommen</h1>
    <p v-if="usertype">Du har rollen som {{ usertype }}</p>
  </div>
</template>

<script>
export default {
  name: 'frontpage',
  computed: {
    username () {
      return this.$store.state.user.username
    },
    usertype () {
      return this.$store.state.user.usertype
    }
  }
}
</script>

<style scoped>
</style>
