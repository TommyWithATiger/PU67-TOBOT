<template>
  <div>
    <h2>Subjects you participate in:</h2>
    <div v-for="s in subjects" class="subject-container">
      <h3>
        <router-link :to="'/subject/' + s.id">{{ s.title }}</router-link>
        <StarRating class="rating-container" :id="s.id" :value="CalculateValue(s.id)" />
      </h3>
      <p v-if="s.relatedTopics && !s.relatedTopics.length" class="related-topics-empty">No topics.</p>
      <div v-for="t in s.relatedTopics" class="related-topics">
        <router-link :to="'/topic/' + t.id"><div class="related-topic-container">{{ t.title }}</div></router-link>
        <router-link :to="'/exercise/' + t.exercise" v-if="t.exercise">
          <div class="exercise-container">
            Do exercise
          </div>
        </router-link>
        <div class="exercise-container" v-else>
          No exercise
        </div>
      </div>
    </div>
  </div>
</template>
data
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
    api.getParticipatingSubjects(this, (data) => {
      for (let s of data.subjects) {
        s.relatedTopics = []
        this.$set(this.subjects, s.id, s)
        api.getRelatedTopicsCount(this, s.id, (data) => {
          let rt = data.related_topics
          for (let i in data.related_topics) {
            let rating = rt[i].ratingCount.reduce((e, a) => e > a ? e : a, 0)
            rt[i].maxRating = rating
            api.getNextExercise(this, rt[i].id, (data) => {
              if (data.exercises.length) {
                this.$set(this.subjects[s.id].relatedTopics[i], 'exercise', data.exercises[0].id)
              }
            }, () => {})
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
        sum += isNaN(t.value) ? 1 : t.value
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
  margin-bottom: 35px;
}

.subject-container > .related-topics:nth-child(2n) .related-topic-container, .subject-container > .related-topics:nth-child(2n + 1) .exercise-container {
  background-color: var(--n-color-4);
}

.subject-container > .related-topics:nth-child(2n + 1) .related-topic-container, .subject-container > .related-topics:nth-child(2n) .exercise-container {
  background-color: var(--n-color-3);
}

.related-topic-container,
.related-topics-empty,
.exercise-container {
  padding: 4px;
  margin: 0;
  text-align: center;
  width: 228px;
  font-weight: bold;
  display: inline-block;
}

.related-topic-container {
  margin-right: -4px;
}

.related-topics-empty {
  font-style: italic;
}

a {
  text-decoration: none;
}

</style>
