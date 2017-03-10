<template>
  <div class="page-content">
    <h1>Emner</h1>
    <h2>Legg til emne</h2>
    <p class="subject-add-fields">
      <label>Tittel: </label>
      <input @keydown.enter="addSubject" v-model="subject.title" type="text" />
      <br>
      <label>Institusjon: </label>
      <input @keydown.enter="addSubject" v-model="subject.institution" type="text" />
      <br>
      <label>Fagkode: </label>
      <input @keydown.enter="addSubject" v-model="subject.subjectCode" type="text" />
      <br>
      <label>Beskrivelse: </label>
      <input @keydown.enter="addSubject" v-model="subject.description" type="text" />
      <button @click="addSubject">Legg til</button>
      <span class="error">{{ addFeedback }}</span>
    </p>
    <h2>Alle emner</h2>
    <div v-if="subjects.length">
      <div class="subject-info subject-info-header">
        <div class="subject-code">Emnekode</div>
        <div class="subject-title">Tittel</div>
        <div class="subject-description">Beskrivelse</div>
        <div class="subject-relate">Temaer</div>
      </div>
      <div v-for="s in subjects" class="subject-info">
        <div class="subject-code">{{ s.subjectCode }}</div>
        <div class="subject-title">{{ s.title }}</div>
        <div class="subject-description">{{ s.description }}</div>
        <div class="subject-relate"><router-link :to="{ name: 'RelateSubjectTopic', params: {id: s.id}}">Temaer</router-link></div>
      </div>
    </div>
    <div v-else><span v-if="!getFeedback.length">Ingen emner.</span></div>
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
      getFeedback: 'Laster inn ...'
    }
  },
  created () {
    api.getSubjects(this, (data) => {
      this.subjects = data.subjects
      this.getFeedback = ''
    }, () => {
      this.subjects = []
      this.getFeedback = 'Klarte ikke å hente emner.'
    })
  },
  methods: {
    addSubject () {
      this.addFeedback = ''
      api.addSubject(this, this.subject, (data) => {
        this.subjects.push(data)
        this.addFeedback = 'Lagt til i database.'
      }, () => {
        this.addFeedback = 'Feilet med å legge til.'
      })
    }
  }
}
</script>

<style scoped>
.subject-add-fields > label {
  width: 5em;
  display: inline-block;
}

.subject-add-fields > button {
  display: block;
  width: 100px;
  height: 25px;
  border: 1px solid #666;
  border-radius: 4px;
  background-color: #e9e9e9;
  margin-top: 8px;
  margin-left: 30px;
}

.subject-add-fields > button:hover {
  border: 1px solid #333;
  background-color: #e1e1e1;
}

.subject-title, .subject-description, .subject-code, .subject-relate {
  padding-right: 20px;
  flex-grow: 1;
  width: 120px;
}

.subject-relate {
  flex-grow: 0.3;
}

.subject-title {
  flex-grow: 2;
}

.subject-description {
  flex-grow: 4;
}

.subject-info {
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

.subject-info-header {
  font-weight: bold;
  font-size: 1.2em;
  background-color: #eeeeee;
}

</style>
