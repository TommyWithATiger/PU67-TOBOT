<template>
  <div class="page-content">
    <h1>{{ title }}</h1>
    <div>
      <input v-model="creds.username" placeholder="Brukernavn ..." type="text" />
      <input v-model="creds.password" placeholder="Passord ..." type="text" />
      <LoginBtn :creds="creds" :redirect="redirectUrl" />
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
      }
    }
  },
  computed: {

    // Adding redirect URL after login is verified
    // if it exists. Else go to root folder.
    redirectUrl () {
      return this.$route.query.redirect || '/'
    }
  },
  components: {
    LoginBtn
  }
}
</script>

<style scoped>
</style>
