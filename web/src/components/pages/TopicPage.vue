<template>
  <div class="page-content">
    <h1>Temaer</h1>
    <h2>Legg til tema</h2>
    <p>
      <label>Tittel</label>
      <input v-model="topic.title" type="text" />
      <br>
      <label>Beskrivelse</label>
      <input v-model="topic.description" type="text" />
      <button @click="addTopic">Legg til</button>
      <span>{{ addFeedback }}</span>
    </p>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'topicpage',
  data () {
    return {
      topic: {
        title: '',
        description: ''
      },
      addFeedback: ''
    }
  },
  created () {
  },
  computed: {
  },
  methods: {
    addTopic () {
      api.addTopic(this, topic, () => {
        this.addFeedback = 'Lagt til i database.'
      }, () => {
        this.addFeedback = 'Feilet med å legge til.'
      })
    }
  }
}
</script>

<style scoped>
</style>
