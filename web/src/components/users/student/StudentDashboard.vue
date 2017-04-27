<template>
  <div>
  	<h1 v-if="username">Hi, {{ username }}!</h1>
    <h1 v-else>Hi</h1>
    <SubjectParticipating />
  </div>
</template>

<script>
import SubjectParticipating from 'components/users/SubjectParticipating'

export default {
  name: 'studentdashboard',
  computed: {
    username () {
      return this.$store.state.user.username
    }
  },
  components: {
    SubjectParticipating
  }
}
</script>

<style scoped>
</style>
