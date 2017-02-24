<template>
  <div class="page-content">
    <h1>Temaer</h1>
    <h2>Legg til tema</h2>
    <p class="topic-add-fields">
      <label>Tittel: </label>
      <input @keydown.enter="addTopic" v-model="topic.title" type="text" />
      <br>
      <label>Beskrivelse: </label>
      <input @keydown.enter="addTopic" v-model="topic.description" type="text" />
      <button @click="addTopic">Legg til</button>
      <span class="error">{{ addFeedback }}</span>
    </p>
    <h2>Alle temaer</h2>
    <div v-if="topics.length">
      <div class="topic-info topic-info-header">
        <div class="topic-title"> Tittel </div>
        <div class="topic-description"> Beskrivelse </div>
      </div>
      <div v-for="t in topics" class="topic-info">
        <div class="topic-title"> {{ t.title }} </div>
        <div class="topic-description"> {{ t.description }} </div>
      </div>
    </div>
    <div v-else><span v-if="!getFeedback.length">Ingen emner.</span></div>
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
      getFeedback: 'Laster inn ...'
    }
  },
  created () {
    api.getTopics(this, (data) => {
      this.topics = data.topics
      this.getFeedback = ''
    }, () => {
      this.topics = []
      this.getFeedback = 'Klarte ikke å hente temaer.'
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
.topic-add-fields > label {
  width: 5em;
  display: inline-block;
}

.topic-add-fields > button {
  display: block;
  width: 100px;
  height: 25px;
  border: 1px solid #666;
  border-radius: 4px;
  background-color: #e9e9e9;
  margin-top: 8px;
  margin-left: 30px;
}

.topic-add-fields > button:hover {
  border: 1px solid #333;
  background-color: #e1e1e1;
}

.topic-title, .topic-description {
  padding-right: 20px;
  flex-grow: 1;
  width: 120px;
}

.topic-description {
  flex-grow: 3;
}

.topic-info {
  display: flex;
  display: -webkit-flex;
  flex-direction: row;
  webkit-flex-direction: row;
  flex-wrap: wrap;
  webkit-flex-wrap: wrap;
  flex-grow: 0;
  webkit-flex-grow: grow;
  padding-left: 15px;
  padding-right: 15px;
  border-bottom: 1px solid #d6d6d6;
  max-width: 1500px;
  background-color: #f9f9f9;
  padding-top: 1px;
  padding-bottom: 2px;
}

.topic-info-header {
  font-weight: bold;
  font-size: 1.2em;
  background-color: #eeeeee;
}
</style>
