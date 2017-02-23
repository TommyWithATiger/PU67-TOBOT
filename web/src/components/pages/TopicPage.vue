<template>
  <div class="page-content">
    <h1>Temaer</h1>
    <h2>Legg til tema</h2>
    <p>
      <label>Tittel</label>
      <input @keydown.enter="addTopic" v-model="topic.title" type="text" />
      <br>
      <label>Beskrivelse</label>
      <input @keydown.enter="addTopic" v-model="topic.description" type="text" />
      <button @click="addTopic">Legg til</button>
      <span class="error">{{ addFeedback }}</span>
    </p>
    <h2>Alle temaer</h2>
    <div v-if="topics.length">
      <div v-for="t in topics">{{ t.title }}</div>
    </div>
    <div v-else>Ingen temaer.</div>
    <p class="error">{{ getFeedback }}</p>
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
      addFeedback: '',
      topics: [],
      getFeedback: ''
    }
  },
  created () {
    api.getTopics(this, (data) => {
      console.log(data)
      this.topics = data.topics
    }, () => {
      this.topics = []
    })
  },
  computed: {
  },
  methods: {
    addTopic () {
      this.addFeedback = ''
      api.addTopic(this, this.topic, () => {
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
