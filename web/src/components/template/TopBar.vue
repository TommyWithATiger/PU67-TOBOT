<template>
  <div class="header-content">
    <h1><router-link to="/">Home</router-link></h1>
    <div>
      <div v-if="authenticated">
        <span>{{ state.user.username }}</span>
        <LogoutBtn />
      </div>
      <div v-else>
        <router-link to="/login">
          <button>Logg inn</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import LogoutBtn from 'components/auth/LogoutBtn'

export default {
  name: 'header',
  computed: {
    authenticated () {
      return this.$store.state.user.authenticated
    },
    state () {
      return this.$store.state
    }
  },
  components: {
    LogoutBtn
  }
}
</script>

<style scoped>
.header-content {
  padding: 16px;
  background-color: #eee;
  flex: 0 0 64px;
}
</style>
