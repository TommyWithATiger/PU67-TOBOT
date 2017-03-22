<template>
  <div class="page-content">
    <h1>{{subject.subjectCode}} - {{subject.title}}</h1>
    <h1>Connected topics</h1>
    <div v-if="relatedTopics.length">
      <div class="topic-info topic-info-header">
            <div class="topic-title">Title</div>
            <div class="topic-description">Description</div>
          </div>
          <div v-for="t in relatedTopics" class="topic-info">
            <div class="topic-title">{{ t.title }}</div>
            <div class="topic-description">{{ t.description }}</div>
          </div>
    </div>
    <div v-else>
      This subject is not connected to any topics.
    </div>
    <h1>Add topics</h1>
    <div v-if="topics.length">
      <div class="topic-info topic-info-header">
        <div class="topic-title">Title</div>
        <div class="topic-description">Description</div>
        <div class="topic-relate"></div>
      </div>
      <div v-for="t in topics" v-if="!isRelated(t)" class="topic-info">
        <div class="topic-title">{{ t.title }}</div>
        <div class="topic-description">{{ t.description }}</div>
        <button @click="relateTopic(t)" class="topic-relate">Connect</button>
      </div>
    </div>
    <div v-else>
      No topics available.
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
      relatedTopics: [],
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
  watch: {
    '$route' () {
      this.updateData()
    }
  },
  created () {
    this.updateData()
  },
  methods: {
    relateTopic (topic) {
      api.relateSubjectTopic(this, topic, this.subject, (data) => {
        if (data['already-related'] !== data['is-related']) {
          this.relatedTopics.push(topic)
        }
      })
    },
    isRelated (topic) {
      for (var index = 0; index < this.relatedTopics.length; index++) {
        if (this.relatedTopics[index].id === topic.id) {
          return true
        }
      }
      return false
    },
    updateData () {
      api.getTopics(this, (data) => {
        this.topics = data.topics
      }, () => {
        this.topics = []
      })

      api.getSubjectById(this, this.$route.params.id, (data) => {
        this.subject = data
      }, () => {
        window.location.href = '/subject'
      })

      api.getRelatedTopics(this, this.$route.params.id, (data) => {
        this.relatedTopics = data.related_topics
      })
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
	flex: 0 0 90px;
	cursor: pointer;
  align-self: center;
}

.topic-description {
  flex-grow: 3;
}

.topic-info:nth-child(even) {
  background-color: #ccc;
  background-color: var(--n-color-3);
  border-radius: 4px;
}

.topic-info {
  display: flex;
  flex-flow: row wrap;
  padding: 10px 12px;
}

.topic-info-header {
  font-weight: bold;
  font-size: 1.2em;
}
</style>
