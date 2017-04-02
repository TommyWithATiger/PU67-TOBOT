<template>
  <div>
    <h1>Subjects you participate in:</h1>
    <div v-for="s in subjects" class="subject-container">
      <h2>
        <router-link :to="'/subject/' + s.id">{{ s.title }}</router-link>
        <StarRating class="rating-container" :id="s.id" :value="CalculateValue(s.id)" />
      </h2>
      <p v-if="s.relatedTopics && !s.relatedTopics.length" class="related-topics-empty">No topics.</p>
      <div v-for="t in s.relatedTopics" class="related-topics">
        <h3><router-link :to="'/topic/' + t.id">{{ t.title }}</router-link></h3>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'
import StarRating from 'components/template/StarRating'

export default {
  name: 'subjectparticipating',
  data () {
    return {
      subjects: {}
    }
  },
  created () {
    api.getSubjects(this, (data) => {
      for (let s of data.subjects) {
        s.relatedTopics = []
        this.$set(this.subjects, s.id, s)
        api.getRelatedTopicsCount(this, s.id, (data) => {
          let rt = data.related_topics
          for (let i in data.related_topics) {
            let rating = rt[i].ratingCount.reduce((e, a) => e > a ? e : a, 0)
            rt[i].maxRating = rating
          }
          this.$set(this.subjects[s.id], 'relatedTopics', rt)
        })
      }
    })
  },
  methods: {
    CalculateValue (subjectId) {
      let sum = 0
      for (let t of this.subjects[subjectId].relatedTopics) {
        sum += t.value
      }
      return Math.round(sum / this.subjects[subjectId].relatedTopics.length)
    }
  },
  components: {
    StarRating
  }
}
</script>

<style scoped>
.rating-container {
  margin-right: 30px;
  display: inline-block;
}

.subject-container {
  margin: 80px;
}

.related-topics,
.related-topics-empty {
  padding-left: 32px;
}

.related-topics-empty {
  font-style: italic;
}

a {
  text-decoration: none;
}
</style>
