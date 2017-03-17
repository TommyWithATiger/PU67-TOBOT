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

  --button-background-color: #0df;
  --button-background-color-dark: #0df;
  --button-hover-background-color: #0bd;
  --button-hover-background-color-dark: #0bd;
  --button-disabled-background-color: #999;
  --button-disabled-background-color-dark: #999;

  --input-background-color: #ddd;
  --input-background-color-dark: #555;
  --input-color: #555;
  --input-color-dark: #ddd;
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

.dark button,
.dark input[type=submit],
.dark input[type=reset] {
  background-color: #0df;
  background-color: var(--button-background-color-dark);
}

button,
input[type=submit],
input[type=reset] {
  background-color: #0df;
  background-color: var(--button-background-color);
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 3px;
  outline: none;
  box-shadow: 0 0 8px -2px #000;
  transition-property: background-color box-shadow;
  transition-duration: .2s;
}

.dark button:hover,
.dark input[type=submit]:hover,
.dark input[type=reset]:hover {
  background-color: #0bd;
  background-color: var(--button-hover-background-color-dark);
}

button:hover,
input[type=submit]:hover,
input[type=reset]:hover {
  background-color: #0bd;
  background-color: var(--button-hover-background-color);
  transition-duration: .05s;
}

button:active,
input[type=submit]:active,
input[type=reset]:active {
  box-shadow: none;
  transition-duration: .05s;
}

.dark button[disabled],
.dark input[type=submit][disabled],
.dark input[type=reset][disabled] {
  background-color: #0bd;
  background-color: var(--button-background-color-dark);
}
button[disabled],
input[type=submit][disabled],
input[type=reset][disabled] {
  background-color: #0bd;
  background-color: var(--button-background-color);
}

.dark input[type=text],
.dark input {
  background-color: #999;
  background-color: var(--input-background-color-dark);
  color: #555;
  color: var(--input-color-dark);
}

input[type=text],
input {
  background-color: #555;
  background-color: var(--input-background-color);
  color: #999;
  color: var(--input-color);
  outline: none;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
}
</style>
