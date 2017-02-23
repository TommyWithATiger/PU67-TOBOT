<template>
  <div class="page-content">
    <h1>Topics</h1>
    <h2>Add topic</h2>
    <p>
      <label>Tittel</label>
      <input v-model="topic.title" type="text" />
      <br>
      <label>Institusjon</label>
      <input v-model="topic.institution" type="text" />
      <br>
      <label>Fagkode</label>
      <input v-model="topic.subjectCode" type="text" />
      <br>
      <label>Beskrivelse</label>
      <input v-model="topic.description" type="text" />
      <button @click="addTopic">Legg til</button>
    </p>
  </div>
</template>

<script>
// import { api } from 'api'

export default {
  name: 'topicpage',
  data () {
    return {
      topic: {
        title: '',
        institution: '',
        subjectCode: '',
        description: ''
      }
    }
  },
  created () {
  },
  computed: {
  },
  methods: {
    addTopic () {
    }
  }
}
</script>

<style scoped>
</style>
