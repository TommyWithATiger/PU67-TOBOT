<template>
  <div class="page-content" v-if="title">
    <h1>{{ title }}</h1>
    <p>{{ description }}</p>

    <div class="exercises" v-if="exercises.length">
      <h2> Exercises: </h2>
      <div class="exercise" v-for="exercise in exercises" v-if="exercise.title.replace(' ', '').length">
        <router-link :to="'/exercise/' + exercise.id">
          <div class="exercise">
            {{ exercise.title }}
          </div>
        </router-link>
      </div>
      <router-link :to="'/exercise/' + next_exercise" v-if="next_exercise !== -1">
        <button class="recommended_exercise">
          Recommended exercise
        </button>
      </router-link>
    </div>

    <div class="videos" v-if="videos.length">
      <h2>Videos:</h2>
      <div class="video_containers">
        <div class="video_container" v-for="video in videos">
          <h3> {{ video.title }} </h3>
          <youtube :player-width="400" :player-height="300" :video-id="video.link"></youtube>
        </div>
      </div>
    </div>
    <div class="references" v-if="references.length">
      <h2>References:</h2>
      <div class="reference_container" v-for="reference in references">
        <a v-bind:href=" link(reference) ">
          <h3> {{ reference.title }} </h3>
          <div> {{ reference.description }} </div>
        </a>
      </div>
    </div>
  </div>
  <div class="page-content" v-else-if="loading">
    <h1>Loading ...</h1>
  </div>
  <div class="page-content" v-else>
    <h1>Could not find topic.</h1>
  </div>
</template>

<script src="vue-youtube-embed.js" charset="utf-8"></script>
<script>
import { api } from 'api'

export default {
  name: 'topicpage',
  data () {
    return {
      loading: true,
      title: '',
      description: '',
      exercises: [],
      videos: [],
      references: [],
      next_exercise: -1
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
    updateData () {
      api.getTopicById(this, this.$route.params.id, (data) => {
        this.title = data.title
        this.description = data.description
        this.getExercises()
        this.getReferences()
      }, () => {
        this.title = ''
        this.description = ''
      })
    },
    getReferences () {
      this.videos = []
      this.references = []
      api.getReferencesByTopic(this, this.$route.params.id, (data) => {
        for (let referenceNumber in data.references) {
          let reference = data.references[referenceNumber]
          if (reference.type === 'Video') {
            this.videos.push(reference)
          } else {
            this.references.push(reference)
          }
        }
      }, () => {})
    },
    getExercises () {
      this.exercises = []
      api.getExercisesByTopic(this, this.$route.params.id, (data) => {
        this.exercises = data.exercises
        this.getNextExercise()
      }, () => {})
    },
    getNextExercise () {
      api.getNextExercise(this, parseInt(this.$route.params.id), (data) => {
        this.next_exercise = data.id
      }, () => {})
    },
    link (reference) {
      return reference.link
    }
  }
}
</script>

<style scoped>

.video_container {
  display: inline-block;
  padding: 20px;
  margin-top: 20px;
}

@media (min-width:830px) {
  .video_containers > .video_container:nth-child(2n) {
    float: right;
    margin-left: 30px;
  }
}

.reference_container {
  display: inline-block;
  width: 300px;
  min-height: 150px;
  padding: 10px;
  vertical-align: text-top;
}

.reference_container > div:hover, .reference_container > h2:hover {
  cursor: pointer;
}

.references, .videos, .exercises {
  margin-bottom: 50px;
}

.exercise {
  width: 228px;
  margin-bottom: 5px;
  padding: 2px;
  text-align: center;
  display: inline-block;
}

.recommended_exercise {
  margin-top: 10px;
}

.exercises > .exercise:nth-child(2n), .references > .reference_container:nth-child(2n), .video_containers > .video_container:nth-child(2n) {
  background-color: var(--n-color-3);
}

.exercises > .exercise:nth-child(2n + 1), .references > .reference_container:nth-child(2n + 1), .video_containers > .video_container:nth-child(2n + 1) {
  background-color: var(--n-color-4);
}

</style>
