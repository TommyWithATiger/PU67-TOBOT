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
        <div class="topic-rating-header"> Min kunnskap </div>
      </div>
      <div v-for="t in topics" class="topic-info">
        <div class="topic-title"> {{ t.title }} </div>
        <div class="topic-description"> {{ t.description }} </div>
        <div class="topic-rating">
          <span @click="rateTopic(t, 5)" v-bind:class="{selected: t[5]}">☆</span>
          <span @click="rateTopic(t, 4)" v-bind:class="{selected: t[4]}">☆</span>
          <span @click="rateTopic(t, 3)" v-bind:class="{selected: t[3]}">☆</span>
          <span @click="rateTopic(t, 2)" v-bind:class="{selected: t[2]}">☆</span>
          <span @click="rateTopic(t, 1)" v-bind:class="{selected: t[1]}">☆</span></div>
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
      ratings: [],
      rating_to_number: {
        None: 1,
        Poor: 2,
        Ok: 3,
        Good: 4,
        Superb: 5
      },
      number_to_rating: {
        1: 'None',
        2: 'Poor',
        3: 'Ok',
        4: 'Good',
        5: 'Superb'
      },
      getFeedback: 'Laster inn ...'
    }
  },
  created () {
    api.getTopics(this, (data) => {
      this.topics = data.topics
      for (var index = 0; index < this.topics.length; index++) {
        this.resetTopicRating(this.topics[index])
      }
      this.getFeedback = ''
    }, () => {
      this.topics = []
      this.getFeedback = 'Klarte ikke å hente temaer.'
    })
    api.getRatedTopics(this, (data) => {
      this.ratings = data.ratings
      for (var index = 0; index < this.ratings.length; index++) {
        for (var index2 = 0; index2 < this.topics.length; index2++) {
          if (this.topics[index2].id === this.ratings[index].topicID) {
            this.$set(this.topics[index2], [this.rating_to_number[this.ratings[index].rating]], true)
          }
        }
      }
    }, () => {
      this.ratings = []
    })
  },
  computed: {
  },
  methods: {
    resetTopicRating (topic) {
      this.$set(topic, '5', false)
      this.$set(topic, '4', false)
      this.$set(topic, '3', false)
      this.$set(topic, '2', false)
      this.$set(topic, '1', false)
    },
    addTopic () {
      this.addFeedback = ''
      api.addTopic(this, this.topic, () => {
        this.addFeedback = 'Lagt til i database.'
        location.reload()
      }, () => {
        this.addFeedback = 'Feilet med å legge til.'
      })
    },
    rateTopic (topic, rating) {
      api.rateTopic(this, topic, this.number_to_rating[rating], () => {
        this.resetTopicRating(topic)
        this.$set(topic, rating, true)
      }, () => {

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

.topic-rating {
  unicode-bidi: bidi-override;
  direction: rtl;
}
.topic-rating > span {
  display: inline-block;
  position: relative;
  width: 1.1em;
}

.topic-rating > .selected:before,
.topic-rating > .selected ~ span:before {
   content: "\2605";
   position: absolute;
   color: #dddd00;
}

.topic-rating > span:hover:before,
.topic-rating > span:hover ~ span:before {
   content: "\2605";
   position: absolute;
   color: #ffff00;
   cursor: pointer;
}
</style>
