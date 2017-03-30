<template>
  <div class="page-content">
    <h2 class="title">
      {{ title }}
    </h2>
    <div class="tags">
      <h3 class="tags_title">
        Tags: 
      </h3>
      <div class="topic_tag" v-for="tag in tags">
        <div class="tag_title">
          {{ tag.title }}
        </div>
      </div>
    </div>
    <div v-html="content" class="context_container">
    </div>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'topicpage',
  data () {
    return {
      title: '',
      content: '',
      tags: []
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
      api.getExercise(this, this.$route.params.id, (data) => {
        this.content = data.text
        this.tags = data.tags
        this.title = data.title
      }, () => {
        this.content = ''
        this.tags = []
        this.title = ''
      })
    }
  }
}

</script>



<style scoped> 

.context_container {
  position: relative;
}

.tags div, .tags h3 {
  display: inline-block;
}

</style>
