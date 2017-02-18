<template>
  <div id="app" class="app">
    <Header></Header>
    <router-view></router-view>
  </div>
</template>

<script>
import Header from 'components/template/Header'

export default {
  name: 'app',
  components: {
    Header
  }
}
</script>

<style>
.app {
  background-color: #ddd;
  color: #333;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>
