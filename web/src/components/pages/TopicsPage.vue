<template>
  <div class="page-content">
    <h1>Temaer</h1>
    <h2>Legg til tema</h2>
    <div class="topic-add-fields">
      <label>Tittel: </label>
      <input @keydown.enter="addTopic" v-model="topic.title" type="text" />
      <br>
      <label>Beskrivelse: </label>
      <input @keydown.enter="addTopic" v-model="topic.description" type="text" />
      <p>
        <button @click="addTopic">Legg til</button>
        <span class="error">{{ addFeedback }}</span>
      </p>
    </div>
    <h2>Alle temaer</h2>
    <div v-if="topics.length">
      <div class="topic-info topic-info-header">
        <div class="topic-title"> Tittel </div>
        <div class="topic-description"> Beskrivelse </div>
        <div class="topic-rating-header"> Min kunnskap </div>
      </div>
      <div v-for="(t, id) in topics" v-if="id != 'length'" class="topic-info">
        <div class="topic-title"> {{ t.title }} </div>
        <div class="topic-description"> {{ t.description }} </div>
        <div class="topic-rating">
          <span v-for="n in 5" @click="rateTopic(id, 6 - n)" v-bind:class="{ selected: isSelected(t.rating, 6 - n) }">☆</span>
        </div>
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
      topics: {},
      ratingToNumber: {
        None: 1,
        Poor: 2,
        Ok: 3,
        Good: 4,
        Superb: 5
      },
      numberToRating: {
        '1': 'None',
        '2': 'Poor',
        '3': 'Ok',
        '4': 'Good',
        '5': 'Superb'
      },
      getFeedback: 'Laster inn ...'
    }
  },
  created () {
    api.getTopics(this, (data) => {
      this.topics = {
        length: 0
      }
      for (let t of data.topics) {
        this.topics[t.id] = t
        this.topics[t.id].rating = 0
        this.topics.length++
        this.$set(this.topics[t.id], 'rating', 0)
      }
      this.getFeedback = ''
    }, () => {
      this.topics = []
      this.getFeedback = 'Klarte ikke å hente temaer.'
    })
    api.getRatedTopics(this, (data) => {
      for (let t of data.ratings) {
        this.topics[t.topicID].rating = this.ratingToNumber[t.rating]
        this.$set(this.topics[t.topicID], 'rating', this.ratingToNumber[t.rating])
      }
      this.$forceUpdate()
    }, () => {
    })
  },
  methods: {
    isSelected (rating, star) {
      return rating >= star
    },
    addTopic () {
      this.addFeedback = ''
      api.addTopic(this, this.topic, (data) => {
        let t = data
        this.topics[t.id] = {
          title: t.title,
          description: t.description,
          id: t.id,
          rating: 0
        }
        this.topics.length++
        this.addFeedback = 'Lagt til i database.'
      }, () => {
        this.addFeedback = 'Feilet med å legge til.'
      })
    },
    rateTopic (id, rating) {
      api.rateTopic(this, id, this.numberToRating[rating], (data) => {
        this.$set(this.topics[id], 'rating', this.ratingToNumber[data.rating])
        this.$forceUpdate()
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

.topic-title, .topic-description {
  padding-right: 20px;
  flex-grow: 1;
  width: 120px;
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
