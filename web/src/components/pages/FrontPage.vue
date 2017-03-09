<template>
  <div class="page-content">
    <h1 v-if="username">Hei, {{ username }}!</h1>
    <h1 v-else>Velkommen</h1>
    <p v-if="usertype">Du har rollen som {{ usertype }}</p>
    <StudentDashboard v-if="isStudent" />
    <AdminDashboard v-if="isAdmin" />
    <TeacherDashboard v-if="isTeacher" />
  </div>
</template>

<script>
import AdminDashboard from 'components/users/admin/AdminDashboard'
import StudentDashboard from 'components/users/student/StudentDashboard'
import TeacherDashboard from 'components/users/teacher/TeacherDashboard'

export default {
  name: 'frontpage',
  computed: {
    username () {
      return this.$store.state.user.username
    },
    usertype () {
      return this.$store.state.user.usertype
    },
    isAdmin () {
      return this.$store.state.user.usertype === 'Admin'
    },
    isTeacher () {
      return this.$store.state.user.usertype === 'Teacher'
    },
    isStudent () {
      return this.$store.state.user.usertype === 'Student'
    }
  },
  components: {
    AdminDashboard,
    StudentDashboard,
    TeacherDashboard
  }
}
</script>

<style scoped>
</style>
