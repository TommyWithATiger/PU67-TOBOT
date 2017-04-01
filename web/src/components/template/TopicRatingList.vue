<template>
  <div>
    <div v-for="(t, id) in topics" v-if="id != 'length'" class="topic-info">
      <div class="topic-title"> {{ t.title }} </div>
      <div class="topic-description"> {{ t.description }} </div>
      <StarRating :id="t.id" :value="t.rating" @rate="rateTopic" />
    </div>
    <div v-else><span v-if="!getFeedback.length">No topics.</span></div>
    <p class="error">{{ getFeedback }}</p>
  </div>
</template>

<script>
import { api } from 'api'
import StarRating from 'components/template/StarRating'

export default {
  name: 'topic-rating-list',
  data () {
    return {
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
      getFeedback: 'Loading ...'
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
      this.getFeedback = 'Could not fetch topics.'
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
    rateTopic (id, rating) {
      api.rateTopic(this, parseInt(id), this.numberToRating[rating], (data) => {
        this.$set(this.topics[id], 'rating', this.ratingToNumber[data.rating])
        this.$forceUpdate()
      })
    }
  },
  components: {
    StarRating
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
