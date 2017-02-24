<template>
	<div class="page-content">
		<h1> {{subject.subjectCode}} - {{subject.title}} </h1>
		<h1>Temaer</h1>
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
		<div v-else>
			Ingen temaer tilgjenglig
		</div>
		<p class="error">{{ getFeedback }}</p>
	</div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'relatetopicsubjectpage',
  data () {
    return {
      topic: {
        title: '',
        description: ''
      },
      addFeedback: '',
      topics: [],
      getFeedback: '',
      subject: {
        title: '',
        description: '',
        institution: '',
        subjectCode: '',
        id: this.$route.params.id
      }
    }
  },
  created () {
    api.getTopics(this, (data) => {
      console.log(data)
      this.topics = data.topics
    }, () => {
      this.topics = []
    })
    api.getSubjectID(this, (data) => {
      this.subject = data
    }, () => {
      window.location.href = '/subject'
    }, this.$route.params.id)
  },
  computed: {
  },
  methods: {
    relateTopic () {
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
