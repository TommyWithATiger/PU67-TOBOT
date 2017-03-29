<template>
  <div class="page-content" v-if="title">
    <h1>{{ title }}</h1>
    <div v-if=""></div>
    <div class="page-content" v-if="type == 'Video'">
    <youtube :video-id="link"></youtube>
    </div>
    <div class="page-content" v-else-if="link">
    <a :href="link">{{ link }}</a>
    </div>
    <p>{{ description }}</p>
    <p>Reference type: {{ type }}</p>
  </div>
  <div class="page-content" v-else>
    <h1>Could not find referfence.</h1>
  </div>
</template>

<script src="vue-youtube-embed.js" charset="utf-8"></script>
<script>
import { api } from 'api'

export default {
  name: 'refrerencepage',
  data () {
    return {
      loading: true,
      title: '',
      description: ''
    }
  },
  watch: {
    '$route' () {
      this.updateData()
    }
  },
  created () {
    this.updateData()
  },
  methods: {
    updateData () {
      api.getReferenceById(this, this.$route.params.id, (data) => {
        this.title = data.title
        this.description = data.description
        this.type = data.type
        this.link = data.link
      }, () => {
        this.title = ''
        this.description = ''
        this.link = ''
        this.type = ''
      })
    }
  }
}
</script>

<style scoped>
</style>
