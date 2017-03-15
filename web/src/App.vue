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

.page-content {
  padding: 16px;
  flex: 1;
}

.error {
  color: #a00;
}

.content-container,
.page-content {
  max-width: 960px;
  width: 100%;
  margin: auto;
}
</style>
