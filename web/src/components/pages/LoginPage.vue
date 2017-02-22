<template>
  <div class="page-content">
    <h1>{{ title }}</h1>
    <div>
      <input v-model="creds.username" placeholder="Brukernavn ..." type="text" />
      <input v-model="creds.password" placeholder="Passord ..." type="text" />
      <LoginBtn :creds="creds" :redirect="redirectUrl" :error="errorHandler" :success="successHandler" />
      <span>{{ feedback }}</span>
    </div>
  </div>
</template>

<script>
import LoginBtn from 'components/auth/LoginBtn'

export default {
  name: 'loginpage',
  data () {
    return {
      title: 'Login',
      creds: {
        username: '',
        password: ''
      },
      feedback: ''
    }
  },
  computed: {
    // Adding redirect URL after login is verified
    // if it exists. Else go to root folder.
    redirectUrl () {
      return this.$route.query.redirect || '/'
    }
  },
  methods: {
    successHandler (feedback) {
      this.feedback = feedback
    },
    errorHandler (feedback) {
      this.feedback = feedback
    }
  },
  components: {
    LoginBtn
  }
}
</script>

<style scoped>
</style>
