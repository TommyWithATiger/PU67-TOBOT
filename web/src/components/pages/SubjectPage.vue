<template>
  <div class="page-content">
    <h1>Emner</h1>
    <h2>Legg til emne</h2>
    <p>
      <label>Tittel</label>
      <input v-model="subject.title" type="text" />
      <br>
      <label>Institusjon</label>
      <input v-model="subject.institution" type="text" />
      <br>
      <label>Fagkode</label>
      <input v-model="subject.subjectCode" type="text" />
      <br>
      <label>Beskrivelse</label>
      <input v-model="subject.description" type="text" />
      <button @click="addSubject">Legg til</button>
    </p>
  </div>
</template>

<script>
// import { api } from 'api'

export default {
  name: 'subjectpage',
  data () {
    return {
      subject: {
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
    addSubject () {
      // api.addSubject()
    }
  }
}
</script>

<style scoped>
</style>
