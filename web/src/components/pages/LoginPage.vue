<template>
  <div class="page-content">
    <h1>{{ title }}</h1>
    <router-link to="/">Go back to front page</router-link>
  </div>
</template>

<script>
export default {
  name: 'loginpage',
  data () {
    return {
      title: 'Login'
    }
  }
}
</script>

<style scoped>
.page-content {
  padding: 16px;
  flex: 1;
}
</style>
