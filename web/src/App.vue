<template>
  <div id="app" :class="`app secondary-background-color secondary-color ${theme}`">
    <TopBar></TopBar>
    <router-view></router-view>
  </div>
</template>

<script>
import TopBar from 'components/template/TopBar'

export default {
  name: 'app',
  computed: {
    theme () {
      return this.$store.state.theme
    }
  },
  components: {
    TopBar
  }
}
</script>

<style>
:root {
  --primary-color: #9ff;
  --primary-color-dark: #0ff;
  --secondary-color: #444;
  --secondary-color-dark: #eee;

  --primary-background-color: #fff;
  --primary-background-color-dark: #333;
  --secondary-background-color: #eee;
  --secondary-background-color-dark: #444;
}

.primary-background-color, .primary-background-color.light { background-color: #9ff; background-color: var(--primary-background-color); }
.dark .primary-background-color, .primary-background-color.dark { background-color: #0ff; background-color: var(--primary-background-color-dark); }

.secondary-background-color, .secondary-background-color.light { background-color: #eee; background-color: var(--secondary-background-color); }
.dark .secondary-background-color, .secondary-background-color.dark { background-color: #444; background-color: var(--secondary-background-color-dark); }

.primary-color, .primary-color.light { color: #9ff; color: var(--primary-color); }
.dark .primary-color, .primary-color.dark { color: #0ff; color: var(--primary-color-dark); }

.secondary-color, .secondary-color.light { color: #eee; color: var(--secondary-color); }
.dark .secondary-color, .secondary-color.dark { color: #444; color: var(--secondary-color-dark); }

.app {
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
.header-container,
.page-content {
  max-width: 960px;
  width: 100%;
  margin: auto;
}
.header-container {
  max-width: 1200px;
}
</style>
