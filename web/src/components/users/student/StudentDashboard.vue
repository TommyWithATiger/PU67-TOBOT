<template>
  <div>
  	<h1 v-if="username">Hi, {{ username }}!</h1>
    <h1 v-else>Hi</h1>
    <StudentParticipating />
  </div>
</template>

<script>
import StudentParticipating from 'components/users/SubjectParticipating'

export default {
  name: 'studentdashboard',
  computed: {
    username () {
      return this.$store.state.user.username
    }
  },
  components: {
    StudentParticipating
  }
}
</script>

<style scoped>
</style>
