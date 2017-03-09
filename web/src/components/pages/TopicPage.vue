<template>
  <div class="page-content" v-if="title">
    <h1>{{ title }}</h1>
    <p>{{ description }}</p>
  </div>
  <div class="page-content" v-else-if="loading">
    <h1>Laster inn...</h1>
  </div>
  <div class="page-content" v-else>
    <h1>Fant ingen emne.</h1>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'topicpage',
  data () {
    return {
      loading: true,
      title: '',
      description: ''
    }
  },
  created () {
    api.getTopicById(this, this.$route.params.id, (data) => {
      this.title = data.title
      this.description = data.description
    }, () => {
      this.title = ''
      this.description = ''
    })
  }
}
</script>

<style scoped>
</style>
