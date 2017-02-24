<template>
	<div class="page-content">
		<h1> {{subject.subjectCode}} - {{subject.title}} </h1>
		<h1>Tilknyttede temaer</h1>
		<div v-if="related_topics.length">
			<div class="topic-info topic-info-header">
        		<div class="topic-title"> Tittel </div>
       		 	<div class="topic-description"> Beskrivelse </div>
      		</div>
      		<div v-for="t in related_topics" class="topic-info">
        		<div class="topic-title"> {{ t.title }} </div>
        		<div class="topic-description"> {{ t.description }} </div>
      		</div>
		</div>
		<div v-else>
			Dette emnet er ikke knyttet til noen temaer
		</div>
		<h1>Legge til temaer</h1>
		<div v-if="topics.length">
			<div class="topic-info topic-info-header">
        		<div class="topic-title"> Tittel </div>
       		 	<div class="topic-description"> Beskrivelse </div>
       		 	<div class="topic-relate"> </div>
      		</div>
      		<div v-for="t in topics">
      			<div v-if="!isRelated(t)" class="topic-info">
        			<div class="topic-title"> {{ t.title }} </div>
        			<div class="topic-description"> {{ t.description }} </div>
        			<div @click="relateTopic(t)" class="topic-relate"> Knytt til </div>
        		</div>
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
      related_topics: [],
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
    api.getRelatedTopics(this, (data) => {
      this.related_topics = data.related_topics
    }, () => {

    }, this.$route.params.id)
  },
  computed: {
  },
  methods: {
    relateTopic (topic) {
      api.relateSubjectTopic(this, topic, this.subject, () => {
        location.reload()
      }, () => {})
    },
    isRelated (topic) {
      for (var index = 0; index < this.related_topics.length; index++) {
        if (this.related_topics[index].id === topic.id) {
          return true
        }
      }
      return false
    }
  }
}
</script>

<style scoped>

.topic-title, .topic-description, .topic-relate {
  padding-right: 20px;
  flex-grow: 1;
  width: 120px;
}

.topic-relate {
	flex-grow: 0.3;
	text-decoration: underline;
	color: #0645AD;
	cursor: pointer;
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
