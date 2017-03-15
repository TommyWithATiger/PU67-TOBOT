<template>
  <div class="page-content">
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
