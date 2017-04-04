<template>
  <div class="page-content">
    <h1>Subjects</h1>
    <div v-if="canAdd()">
      <h2>Add subject</h2>
      <p class="subject-add-fields">
        <label>Title: </label>
        <input @keydown.enter="addSubject" v-model="subject.title" type="text" />
        <br>
        <label>Institution: </label>
        <input @keydown.enter="addSubject" v-model="subject.institution" type="text" />
        <br>
        <label>Subject code: </label>
        <input @keydown.enter="addSubject" v-model="subject.subjectCode" type="text" />
        <br>
        <label>Description: </label>
        <input @keydown.enter="addSubject" v-model="subject.description" type="text" />
        <div>
        <button @click="addSubject">Add</button>
        <span class="error">{{ addFeedback }}</span>
        </div>
      </p>
    </div>
    <h2>All subjects</h2>
    <div v-if="subjects.length">
      <div class="subject-info subject-info-header">
        <div class="subject-code">Subject code</div>
        <div class="subject-title">Title</div>
        <div class="subject-description">Description</div>
        <div class="subject-relate">Link</div>
      </div>
      <div v-for="s in subjects" class="subject-info">
        <div class="subject-code">{{ s.subjectCode }}</div>
        <div class="subject-title">{{ s.title }}</div>
        <div class="subject-description">{{ s.description }}</div>
        <div class="subject-relate"><router-link class="colored-link" :to="'/subject/' + s.id">details</router-link></div>
      </div>
    </div>
    <div v-else><span v-if="!getFeedback.length">Not subjects.</span></div>
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
    },
    canAdd () {
      return this.$store.state.user.usertype !== 'Student'
    }
  }
}
</script>

<style scoped>
.subject-add-fields > label {
  width: 5em;
  display: inline-block;
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

.subject-info:nth-child(even) {
  background-color: #ccc;
  background-color: var(--n-color-3);
  border-radius: 4px;
}

.subject-info {
  display: flex;
  flex-flow: row wrap;
  padding: 10px 12px;
}

.subject-info-header {
  font-weight: bold;
  font-size: 1.2em;
}
</style>
