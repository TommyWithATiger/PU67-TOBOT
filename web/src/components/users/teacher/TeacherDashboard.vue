<template>
  <div>
    <h1 v-if="username">Hei, {{ username }}!</h1>
    <h1 v-else>Hei</h1>
    <StudentStatistics />
  </div>
</template>

<script>
import StudentStatistics from 'components/users/teacher/StudentStatistics'

export default {
  name: 'teacerdashboard',
  computed: {
    username () {
      return this.$store.state.user.username
    }
  },
  components: {
    StudentStatistics
  }
}
</script>

<style scoped>
</style>
