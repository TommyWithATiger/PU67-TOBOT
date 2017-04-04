<template>
  <div class="page-content" v-if="title">
    <h1>{{ title }}</h1>
    <p>{{ description }}</p>

    <div class="exercises" v-if="exercises.length">
      <h2> Exercises: </h2>
      <div v-for="exercise in exercises">

      </div>
    </div>

    <div class="videos" v-if="videos.length">
      <div class="video_containers">
        <div class="video_container" v-for="video in videos">
          <h3> {{ video.title }} </h3>
          <youtube :player-width="400" :player-height="300" :video-id="video.link"></youtube>
        </div>
      </div>
    </div>
    <div class="references" v-if="references.length">
      <h2>References</h2>
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
      references: []
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
    link (reference) {
      return reference.link
    }
  }
}
</script>

<style scoped>

.video_container {
  display: inline-block;
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
  margin-right: 30px;
  vertical-align: text-top;
}

.reference_container > div:hover, .reference_container > h2:hover {
  cursor: pointer;
}

.references, .videos, .exercise {
  margin-bottom: 50px;
}

</style>
