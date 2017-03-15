<template>
  <div>
    <h1>Statistikk over studenter:</h1>
    <div v-for="s in subjects">
      <h2><router-link :to="'/subject/' + s.id">{{ s.title }}</router-link></h2>
      <p v-if="s.relatedTopics && !s.relatedTopics.length" class="related-topics-empty">Ingen temaer.</p>
      <div v-for="t in s.relatedTopics" class="related-topics">
        <span>
          <svg width="50" height="20" viewBox="0 0 500 200">
            <g v-for="(r, i) in t.ratingCount" v-if="i !== 0">
              <rect
                :x="100 * (i - 1) + 5"
                y="110"
                width="90"
                height="90"
                fill="black"
              />
              <rect
                :x="100 * (i - 1) + 5"
                :y="100 * (1 - r / (t.maxRating || 1))"
                width="90"
                :height="100 * r / (t.maxRating || 1)"
                :fill="`rgb(${parseInt(255 / 4 * (5 - i))}, ${parseInt(255 / 4 * (i - 1))}, 0)`"
              />
              <text :x="100 * (i - 1) + 32" y="180" font-family="Arial" font-size="60" fill="white">{{ r }}</text>
            </g>
          </svg>
        </span> - 
        <router-link :to="'/topic/' + t.id">{{ t.title }}</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'studentstatistics',
  data () {
    return {
      subjects: {}
    }
  },
  created () {
    api.getSubjects(this, (data) => {
      for (let s of data.subjects) {
        this.$set(this.subjects, s.id, s)

        api.getRelatedTopicsCount(this, s.id, (data) => {
          let rt = data.related_topics
          for (let i in data.related_topics) {
            let rating = rt[i].ratingCount.reduce((e, a) => e > a ? e : a, 1)
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
