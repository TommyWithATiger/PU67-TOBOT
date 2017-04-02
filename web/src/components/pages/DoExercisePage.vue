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
      <h3 class="solutionTitle"> 
        Solution:
      </h3>
      <div class="solution">
        This exercise has no registered solution
      </div>
    </div>
    <div v-if="done" class="ratingBar">
      <div class="ratingBox">
        <div class="ratingTitle">How did you do?</div>
        <div class="rating">
          <div title='Bad' class="inline">
            <svg v-bind:class="[ result=='BAD' ? 'clicked' : '']" @click="setResult('BAD')" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="red" viewBox="0 0 24 24"><path d="M19.406 14.442c1.426-.06 2.594-.858 2.594-2.506 0-1-.986-6.373-1.486-8.25-.714-2.689-2.471-3.686-5.009-3.686-2.283 0-4.079.617-5.336 1.158-2.585 1.113-4.665 1.842-8.169 1.842v9.928c3.086.401 6.43.956 8.4 7.744.483 1.66.972 3.328 2.833 3.328 3.448 0 3.005-5.531 2.196-8.814 1.107-.466 2.767-.692 3.977-.744zm-.207-1.992c-2.749.154-5.06 1.013-6.12 1.556.431 1.747.921 3.462.921 5.533 0 2.505-.781 3.666-1.679.574-1.993-6.859-5.057-8.364-8.321-9.113v-6c2.521-.072 4.72-1.041 6.959-2.005 1.731-.745 4.849-1.495 6.416-.614 1.295.836 1.114 1.734.292 1.661l-.771-.032c-.815-.094-.92 1.068-.109 1.141 0 0 1.321.062 1.745.115.976.123 1.028 1.607-.04 1.551-.457-.024-1.143-.041-1.143-.041-.797-.031-.875 1.078-.141 1.172 0 0 .714.005 1.761.099s1.078 1.609-.004 1.563c-.868-.037-1.069-.027-1.069-.027-.75.005-.874 1.028-.141 1.115l1.394.167c1.075.13 1.105 1.526.05 1.585z"/></svg>
          </div>
          <div title='Good' class="inline">
            <svg v-bind:class="[ result=='GOOD' ? 'clicked' : '']" @click="setResult('GOOD')" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="green" viewBox="0 0 24 24"><path d="M15.43 8.814c.808-3.283 1.252-8.814-2.197-8.814-1.861 0-2.35 1.668-2.833 3.329-1.971 6.788-5.314 7.342-8.4 7.743v9.928c3.503 0 5.584.729 8.169 1.842 1.257.541 3.053 1.158 5.336 1.158 2.538 0 4.295-.997 5.009-3.686.5-1.877 1.486-7.25 1.486-8.25 0-1.649-1.168-2.446-2.594-2.507-1.21-.051-2.87-.277-3.976-.743zm3.718 4.321l-1.394.167s-.609 1.109.141 1.115c0 0 .201.01 1.069-.027 1.082-.046 1.051 1.469.004 1.563l-1.761.099c-.734.094-.656 1.203.141 1.172 0 0 .686-.017 1.143-.041 1.068-.056 1.016 1.429.04 1.551-.424.053-1.745.115-1.745.115-.811.072-.706 1.235.109 1.141l.771-.031c.822-.074 1.003.825-.292 1.661-1.567.881-4.685.131-6.416-.614-2.238-.965-4.437-1.934-6.958-2.006v-6c3.263-.749 6.329-2.254 8.321-9.113.898-3.092 1.679-1.931 1.679.574 0 2.071-.49 3.786-.921 5.533 1.061.543 3.371 1.402 6.12 1.556 1.055.059 1.025 1.455-.051 1.585z"/></svg>
          </div>
        </div>
      </div>
      <div class="ratingBox ratingBoxRight">
        <div class="ratingTitle">How difficult is the exercise?</div>
        <div class="rating">
          <div title='Hard' class="inline">
            <svg v-bind:class="[ difficulty=='Hard' ? 'clicked' : '']" @click="setDifficulty('Hard')" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="red" viewBox="0 0 24 24"><path d="M12 2c5.514 0 10 4.486 10 10s-4.486 10-10 10-10-4.486-10-10 4.486-10 10-10zm0-2c-6.627 0-12 5.373-12 12s5.373 12 12 12 12-5.373 12-12-5.373-12-12-12zm.001 14c-2.332 0-4.145 1.636-5.093 2.797l.471.58c1.286-.819 2.732-1.308 4.622-1.308s3.336.489 4.622 1.308l.471-.58c-.948-1.161-2.761-2.797-5.093-2.797zm-3.501-6c-.828 0-1.5.671-1.5 1.5s.672 1.5 1.5 1.5 1.5-.671 1.5-1.5-.672-1.5-1.5-1.5zm7 0c-.828 0-1.5.671-1.5 1.5s.672 1.5 1.5 1.5 1.5-.671 1.5-1.5-.672-1.5-1.5-1.5z"/></svg>
          </div>
          <div title='Medium' class="inline">
            <svg v-bind:class="[ difficulty=='Medium' ? 'clicked' : '']" @click="setDifficulty('Medium')" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="orange" viewBox="0 0 24 24"><path d="M12 2c5.514 0 10 4.486 10 10s-4.486 10-10 10-10-4.486-10-10 4.486-10 10-10zm0-2c-6.627 0-12 5.373-12 12s5.373 12 12 12 12-5.373 12-12-5.373-12-12-12zm4 17h-8v-2h8v2zm-7.5-9c-.828 0-1.5.671-1.5 1.5s.672 1.5 1.5 1.5 1.5-.671 1.5-1.5-.672-1.5-1.5-1.5zm7 0c-.828 0-1.5.671-1.5 1.5s.672 1.5 1.5 1.5 1.5-.671 1.5-1.5-.672-1.5-1.5-1.5z"/></svg>
          </div>
          <div title='Easy' class="inline">
            <svg v-bind:class="[ difficulty=='Easy' ? 'clicked' : '']" @click="setDifficulty('Easy')" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="green" viewBox="0 0 24 24"><path d="M12 2c5.514 0 10 4.486 10 10s-4.486 10-10 10-10-4.486-10-10 4.486-10 10-10zm0-2c-6.627 0-12 5.373-12 12s5.373 12 12 12 12-5.373 12-12-5.373-12-12-12zm5.507 13.941c-1.512 1.195-3.174 1.931-5.506 1.931-2.334 0-3.996-.736-5.508-1.931l-.493.493c1.127 1.72 3.2 3.566 6.001 3.566 2.8 0 4.872-1.846 5.999-3.566l-.493-.493zm-9.007-5.941c-.828 0-1.5.671-1.5 1.5s.672 1.5 1.5 1.5 1.5-.671 1.5-1.5-.672-1.5-1.5-1.5zm7 0c-.828 0-1.5.671-1.5 1.5s.672 1.5 1.5 1.5 1.5-.671 1.5-1.5-.672-1.5-1.5-1.5z"/></svg>
          </div>
        </div>
      </div>
      <button v-bind:title="[canContinue ? '' : 'Cannot continue without rating the exercise']" v-bind:class="[canContinue ? '' : 'disabled']" @click="next">Continue</button>
    </div>
  </div>
</template>

<script>
import { api } from 'api'
import StarRating from 'components/template/StarRating'

export default {
  name: 'doexercisepage',
  data () {
    return {
      title: '',
      solution: '',
      content: '',
      tags: [],
      id: parseInt(this.$route.params.id),
      done: false,
      difficulty: '',
      result: ''
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
    setDifficulty (difficulty) {
      this.difficulty = difficulty
    },
    setResult (result) {
      this.result = result
    },
    exerciseDone () {
      this.done = !this.done
    },
    next () {
      if (this.canContinue) {
        api.registerExerciseAttempt(this, this.id, this.difficulty, this.result === 'GOOD', () => {
          this.$router.go(window.history.back())
        }, () => {})
      }
    }
  },
  components: {
    StarRating
  },
  computed: {
    showSolution: function () {
      return this.done && this.solution.length !== 0
    },
    canContinue: function () {
      return this.result !== '' && this.difficulty !== ''
    }
  }
}

</script>



<style scoped> 

.inline {
  display: inline-block;
}

svg {
  opacity: 0.5;
}

svg:hover {
  opacity: 1;
  cursor: pointer;
}

.clicked {
  opacity: 1;
}

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

.ratingBar {
  margin-top: 60px;
  padding-left: 20px;
}

.ratingBar button, .ratingBar .ratingBox {
  vertical-align: text-top;
  display: inline-block;
  margin-left: 50px;
  position: relative;
}

.ratingBar button {
  margin-top: 10px;
  float: right;
}

.disabled {
  background-color: var(--n-color-4);
}

.disabled:hover {
  cursor: default;
}

.ratingBar .ratingBox:nth-child(1) {
  margin-left: 0;
}

.ratingTitle {
  font-weight: bold;
}

.ratingBox .rating {
  position: absolute;
  left: 50%;
  margin-right: -50%;
  transform: translate(-50%, 0);
}

.solution {
  margin-left: 20px;
}

.solutionTitle {
  margin-top: 60px;
}

</style>
