<template>
  <div>
    <h1>Subjects you participate in:</h1>
    <div v-for="s in subjects">
      <h2><router-link :to="'/subject/' + s.id">{{ s.title }}</router-link></h2>
      <p v-if="s.relatedTopics && !s.relatedTopics.length" class="related-topics-empty">No topics.</p>
      <div v-for="t in s.relatedTopics" class="related-topics">
        <router-link :to="'/topic/' + t.id">{{ t.title }}</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'subjectparticipating',
  data () {
    return {
      subjects: {},
      ratingToText: {
        '0': 'Not rated',
        '1': 'Bad',
        '2': 'Below Average',
        '3': 'Average',
        '4': 'Good',
        '5': 'Superb'
      }
    }
  },
  created () {
    api.getSubjects(this, (data) => {
      for (let s of data.subjects) {
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
  }
}
</script>

<style scoped>
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
