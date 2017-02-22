<template>
  <div id="app" class="app">
    <TopBar></TopBar>
    <router-view></router-view>
  </div>
</template>

<script>
import TopBar from 'components/template/TopBar'

export default {
  name: 'app',
  components: {
    TopBar
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
