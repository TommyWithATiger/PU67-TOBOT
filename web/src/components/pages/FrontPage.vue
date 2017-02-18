<template>
  <div class="page-content">
    <h1>{{ title }}</h1>
  </div>
</template>

<script>
export default {
  name: 'frontpage',
  data () {
    return {
      title: 'This is TOBOT'
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
