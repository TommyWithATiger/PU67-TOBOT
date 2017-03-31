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
    <div class="contentBox">
      <h3 class="contentTitle"> 
        Exercise:
      </h3>
      <div v-html="content" id="context_container" class="context_container"></div>
    </div>
    <div class="buttonBar" v-if="!done">
      <button @click="exerciseDone">Done</button>
    </div>
    <div class="solutionBox" v-if="showSolution">
      <h3 class="solution_title"> 
        Solution:
      </h3>
      <div class="solution" id="solution" v-html="solution"></div>
    </div>
    <div class="solution" v-else-if="done">
      <h3 class="solution_title"> 
        Solution:
      </h3>
      <div class="solution">
        This exercise has no solution
      </div>
    </div>
    <div v-if="done" class="ratingBar">
      <div class="ratingBox">
        <div class="ratingTitle">Rate the exercise</div>
        <StarRating :id="this.id" :value="0" @rate="rateExercise" class="exercise_rate"/>
      </div>
      <div class="ratingBox ratingBoxRight">
        <div class="ratingTitle">Rate your performance</div>
        <StarRating :id="this.id" :value="0" @rate="rateResult" class="result_rate"/>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'
import StarRating from 'components/template/StarRating'

export default {
  name: 'topicpage',
  data () {
    return {
      title: '',
      solution: '',
      content: '',
      tags: [],
      id: this.$route.params.id,
      done: false
    }
  },
  created () {
    this.updateData()
  },
  mounted () {
    document.getElementById('context_container').addEventListener('DOMSubtreeModified', this.setContentHeight)
    if (document.getElementById('solution') !== null) {
      document.getElementById('solution').addEventListener('DOMSubtreeModified', this.setContentHeight)
    }
  },
  methods: {
    updateData () {
      api.getExercise(this, this.$route.params.id, (data) => {
        this.content = data.text
        this.tags = data.tags
        this.title = data.title
        if (data.solution !== null && data.solution !== undefined) {
          this.solution = data.solution
        }
      }, () => {
        this.content = ''
        this.tags = []
        this.title = ''
        this.solution = ''
      })
    },
    setContentHeight (event) {
      let container = event.target
      if (container.lastChild !== null) {
        container.style.height = parseFloat(container.lastChild.style.top) + this.getHeight(container.lastChild) + 'pt'
      }
    },
    getHeight (node) {
      return node.style.height !== '' ? parseFloat(node.style.height) : (node.style.lineHeight !== '' ? parseFloat(node.style.lineHeight) : 0)
    },
    rateExercise (id, rating) {

    },
    rateResult (id, rating) {

    },
    exerciseDone () {
      this.done = !this.done
    }
  },
  components: {
    StarRating
  },
  computed: {
    showSolution: function () {
      return this.done && this.solution.length !== 0
    }
  }
}

</script>



<style scoped> 

.buttonBar button {
  float: right;
}

.context_container {
  position: relative;
  margin-bottom: 20px;
}

.tags div, .tags h3 {
  display: inline-block;
}

.ratingBox {
  vertical-align: text-top;
  display: inline-block;
  margin-right: 50px;
  float: right;
  position: relative;
}

.ratingBar .ratingBox:nth-child(1) {
  margin-right: 10px;
}

.ratingTitle {
  font-weight: bold;
}

.ratingBox .exercise_rate, .ratingBox .result_rate {
  position: absolute;
  left: 50%;
  margin-right: -50%;
  transform: translate(-50%, 0);
}

.solution {
  margin-left: 20px;
}

</style>
