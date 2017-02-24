<template>
  <div class="page-content">
    <h1>Emner</h1>
    <h2>Legg til emne</h2>
    <p>
      <label>Tittel</label>
      <input @keydown.enter="addSubject" v-model="subject.title" type="text" />
      <br>
      <label>Institusjon</label>
      <input @keydown.enter="addSubject" v-model="subject.institution" type="text" />
      <br>
      <label>Fagkode</label>
      <input @keydown.enter="addSubject" v-model="subject.subjectCode" type="text" />
      <br>
      <label>Beskrivelse</label>
      <input @keydown.enter="addSubject" v-model="subject.description" type="text" />
      <button @click="addSubject">Legg til</button>
      <span class="error">{{ addFeedback }}</span>
    </p>
    <h2>Alle emner</h2>
    <div v-if="subjects.length">
      <div v-for="s in subjects">{{ s.subjectCode }} - {{ s.title }}</div>
    </div>
    <div v-else>Ingen emner.</div>
    <p class="error">{{ getFeedback }}</p>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'subjectpage',
  data () {
    return {
      subject: {
        title: '',
        institution: '',
        subjectCode: '',
        description: ''
      },
      addFeedback: '',
      subjects: [],
      getFeedback: ''
    }
  },
  created () {
    api.getSubjects(this, (data) => {
      console.log(data)
      this.subjects = data.subjects
      this.getFeedback = ''
    }, () => {
      this.subjects = []
      this.getFeedback = 'Klart ikke å koble til server.'
    })
  },
  computed: {
  },
  methods: {
    addSubject () {
      this.addFeedback = ''
      api.addSubject(this, this.subject, () => {
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
