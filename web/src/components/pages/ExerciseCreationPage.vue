<template>
	<div class="page-content" v-on:mousemove="onMove" v-on:mouseup="onUp">
    <div class="explanation">
      Here is the parsed output of the uploaded pdf. Add, remove and alter the boxes to surround exercises in the text. Remember to tag the exercises with the topics the exercise tests.
    </div>
		<div class="context_container" v-html="creationContext">
		</div>
    <div class="global_exercise_options" v-if="selected === null">
      <div class="add_exercise" v-on:click="toggleAddExercise">
        +
      </div>
    </div>
    <div class="add_tag_full hidden" id="add_tag_background">
      
    </div>
    <div class="add_tag_container hidden" id="add_tag_container">
        <div class="close_tag_input" v-on:click="closeTags">
         X
        </div>
        <div class="add_tag_input">
          <input type="text" id="tag_input" v-on:keyup="searchTag" placeholder="Search for tag">
          <div class="tag_search_results" id="tag_search_result">

          </div>
        </div>
        <div class="tags" id="topic_tags">

        </div>
      </div>
      <div>
        <div class="submitInfo"> {{ submitInfo }} </div>
        <button v-on:click="submitExercises">Submit exercises</button>
      </div>
	</div>
</template>

<script>
import { store } from 'store'
import { api } from 'api'

export default {
  name: 'exercisecreationpage',
  data () {
    return {
      creationContext: '',
      options: null,
      titlebox: null,
      moving: {
        'exercise': null,
        'direction': null
      },
      selected: null,
      tags: [],
      submitInfo: '',
      creation: false
    }
  },
  created () {
    if (store.exercise_creation_context === null) {
      this.$router.push(window.history.back())
    }
    this.creationContext = store.exercise_creation_context
    store.exercise_creation_context = null

    this.options = document.createElement('div')
    this.options.classList.toggle('options', true)
    this.options.innerHTML = '<div class="options_button"> Delete </div><div class="options_button"> Tags </div><div class="options_button">Solution+</div>'
    this.options.childNodes[0].onmousedown = this.deleteSelected
    this.options.childNodes[1].onmousedown = this.addTag
    this.options.childNodes[2].onmousedown = this.toggleAddSolution

    this.titlebox = document.createElement('input')
    this.titlebox.setAttribute('placeholder', 'No title set')
    this.titlebox.classList.toggle('titlebox', true)
    this.titlebox.innerHTML = ''
    this.titlebox.onmousedown = function (event) {
      event.stopPropagation()
    }

    api.getTopics(this, (data) => {
      this.tags = data.topics
    }, () => {
      this.tags = []
    })
  },
  mounted () {
    let exercises = document.getElementsByClassName('exercise')

    for (let index = 0; index < exercises.length; index++) {
      this.addExerciseEventHandlers(exercises[index])
    }

    document.onselectstart = function (event) {
      return false
    }

    let words = document.getElementsByClassName('p')
    for (let index = 0; index < words.length; index++) {
      let word = words[index]
      word.onclick = this.createExercise
    }

    let exerciseContainer = document.getElementsByClassName('container')[0]
    exerciseContainer.style.height = parseFloat(exerciseContainer.lastChild.style.top) + this.getHeight(exerciseContainer.lastChild) + 'pt'
  },
  methods: {
    deleteSelected (event) {
      let topSelected = parseFloat(this.selected.style.top)
      let firstChild = this.selected.firstChild
      while ((firstChild = this.selected.firstChild) !== this.options) {
        if (firstChild.classList.contains('solution')) {
          this.deleteSolution(firstChild)
          continue
        }
        firstChild.style.top = topSelected + parseFloat(firstChild.style.top) + 'pt'
        this.selected.removeChild(firstChild)
        this.selected.parentNode.insertBefore(firstChild, this.selected)
      }
      this.blurOtherExercises(this.selected, false)
      this.selected.parentNode.removeChild(this.selected)
      this.selected = null
      event.stopPropagation()
    },
    onMove (event) {
      let exercise = this.moving.exercise
      let direction = this.moving.direction
      if (exercise !== null) {
        if (direction === 'up') {
          if (this.canMoveExerciseUp(exercise) && this.translateHoverYPosition(event) < this.getOffsetTop(exercise)) {
            this.moveExerciseTopUp(exercise, this.translateHoverYPosition(event))
          } else if (exercise.hasChildNodes()) {
            this.moveExerciseTopDown(exercise, this.translateHoverYPosition(event))
          }
        } else if (direction === 'down') {
          if (exercise.hasChildNodes() && this.translateHoverYPosition(event) < this.getOffsetTop(exercise) + parseFloat(exercise.style.height) / 0.75) {
            this.moveExerciseBottomUp(this.moving.exercise, this.translateHoverYPosition(event))
          } else if (this.canMoveExerciseDown(exercise)) {
            this.moveExerciseBottomDown(exercise, this.translateHoverYPosition(event))
          }
        }
      } else {

      }
      event.stopPropagation()
    },
    onUp (event) {
      if (this.moving.exercise !== null) {
        this.moving.exercise = null
        this.moving.direction = null
      }
    },
    onTouchMove (event) {
      this.onMove(event.touches[0])
    },
    onTouchEnd (event) {
      this.onUp(event.touches[0])
    },
    setTitleBoxContent (exercise) {
      if (exercise.getAttribute('title') !== null) {
        this.titlebox.value = exercise.getAttribute('title')
      } else {
        this.titlebox.value = ''
      }
    },
    setTitleExercise (exercise) {
      exercise.removeAttribute('title')
      if (this.titlebox.value !== '') {
        exercise.setAttribute('title', this.titlebox.value)
      }
    },
    toggleSelected (exercise) {
      if (this.creation) {
        return
      }
      if (this.selected === null) {
        this.selected = exercise
        this.selected.appendChild(this.options)
        this.selected.appendChild(this.titlebox)
        this.setTitleBoxContent(exercise)
        this.selected.classList.toggle('selected_exercise', true)
        this.blurOtherExercises(exercise, true)
      } else if (this.selected === exercise) {
        this.setTitleExercise(exercise)
        this.selected.removeChild(this.options)
        this.selected.removeChild(this.titlebox)
        this.blurOtherExercises(exercise, false)
        this.selected.classList.toggle('selected_exercise', false)
        this.selected = null
      }
    },
    blurOtherExercises (exercise, blur) {
      let container = exercise.parentNode
      let child = container.firstChild
      do {
        if (child !== exercise) {
          child.classList.toggle('blurred', blur)
        }
      } while ((child = child.nextSibling) != null)
    },
    hoveringTopOfExercise (exercise, event) {
      let exerciseTop = this.getOffsetTop(exercise)
      let y = this.translateHoverYPosition(event)
      return exerciseTop <= y && y < exerciseTop + 5
    },
    hoveringBottomOfExercise (exercise, event) {
      let exerciseBottom = this.getOffsetTop(exercise) + parseFloat(exercise.style.height) / 0.75
      let y = this.translateHoverYPosition(event)
      return exerciseBottom - 5 <= y && y <= exerciseBottom + 1
    },
    canMoveExerciseUp (exercise) {
      return exercise.previousSibling !== null && !exercise.previousSibling.classList.contains('exercise')
    },
    canMoveExerciseDown (exercise) {
      return exercise.nextSibling !== null && !exercise.nextSibling.classList.contains('exercise')
    },
    moveExerciseTopUp (exercise, y) {
      let node = exercise
      while ((node = exercise.previousSibling) !== null) {
        if (this.getOffsetTop(node) < y || node.classList.contains('exercise')) {
          break
        }
        if (exercise.offsetTop > node.offsetTop) {
          let difference = parseFloat(node.style.top) - parseFloat(exercise.style.top)
          exercise.style.top = node.style.top
          exercise.style.height = parseFloat(exercise.style.height) - difference + 'pt'
          this.moveExerciseChildern(exercise, difference)
        }
        node.parentNode.removeChild(node)
        exercise.insertBefore(node, exercise.firstChild)
        node.style.top = parseFloat(node.style.top) - parseFloat(exercise.style.top) + 'pt'
      }
    },
    moveExerciseTopDown (exercise, y) {
      let node = exercise.firstChild
      let reduceDiff = y - this.getOffsetTop(exercise)
      while (node != null) {
        if ((parseFloat(node.style.top) + this.getHeight(node)) / 0.75 >= reduceDiff && (!(node.classList.contains('r') && parseFloat(node.style.top) >= reduceDiff))) {
          break
        }
        if (node.classList.contains('solution')) {
          this.deleteSolution(node)
          this.moveExerciseTopDown(exercise, y)
          return
        }
        let nextNode = node.nextSibling
        exercise.removeChild(node)
        exercise.parentNode.insertBefore(node, exercise)
        node.style.top = parseFloat(exercise.style.top) + parseFloat(node.style.top) + 'pt'
        node = nextNode
      }

      if (exercise.hasChildNodes()) {
        let startChild = exercise.firstChild
        while (startChild !== null && startChild.classList.contains('r')) {
          startChild = startChild.nextSibling
        }
        let changedTop = parseFloat(startChild.style.top)
        exercise.style.top = parseFloat(exercise.style.top) + parseFloat(startChild.style.top) + 'pt'
        this.moveExerciseChildern(exercise, changedTop)
        exercise.style.height = parseFloat(exercise.lastChild.style.top) + this.getHeight(exercise.lastChild) + 'pt'
      } else {
        exercise.parentNode.removeChild(exercise)
      }
    },
    moveExerciseBottomDown (exercise, y) {
      let node = exercise
      while ((node = exercise.nextSibling) !== null) {
        if (this.getOffsetTop(exercise) + parseFloat(this.getHeight(node)) / 0.75 > y || node.classList.contains('exercise') || node.classList.contains('options')) {
          break
        }
        if (parseFloat(exercise.style.top) + parseFloat(exercise.style.height) < parseFloat(node.style.top) + this.getHeight(node) && !node.classList.contains('r')) {
          exercise.style.height = parseFloat(node.style.top) + this.getHeight(node) - parseFloat(exercise.style.top) + 'pt'
        }
        node.style.top = parseFloat(node.style.top) - parseFloat(exercise.style.top) + 'pt'
        node.parentNode.removeChild(node)
        exercise.appendChild(node)
      }
    },
    moveExerciseBottomUp (exercise, y) {
      let node = exercise.lastChild
      let reduceDiff = y - this.getOffsetTop(exercise)
      while (node !== null) {
        if ((parseFloat(node.style.top)) / 0.75 <= reduceDiff) {
          break
        }
        if (node.classList.contains('solution')) {
          this.deleteSolution(node)
          this.moveExerciseBottomUp(exercise, y)
          return
        }
        let nextNode = node.previousSibling
        exercise.removeChild(node)
        exercise.parentNode.insertBefore(node, exercise.nextSibling)
        node.style.top = parseFloat(exercise.style.top) + parseFloat(node.style.top) + 'pt'
        node = nextNode
      }

      if (exercise.hasChildNodes()) {
        let endChild = exercise.lastChild
        while (endChild !== null && endChild.classList.contains('r')) {
          endChild = endChild.previousSibling
        }
        exercise.style.height = parseFloat(endChild.style.top) + this.getHeight(endChild) + 'pt'
      } else {
        exercise.parentNode.removeChild(exercise)
      }
    },
    getOffsetTop (node) {
      let offsetTop = node.offsetTop + node.parentNode.offsetTop
      while (!node.parentNode.classList.contains('container')) {
        node = node.parentNode
        offsetTop += node.parentNode.offsetTop
      }
      return offsetTop
    },
    translateHoverYPosition (event) {
      return event.y + window.scrollY
    },
    moveExerciseChildern (exercise, difference) {
      let child = exercise.firstChild
      do {
        child.style.top = parseFloat(child.style.top) - difference + 'pt'
      } while ((child = child.nextSibling) != null)
    },
    getHeight (node) {
      return node.style.height !== '' ? parseFloat(node.style.height) : (node.style.lineHeight !== '' ? parseFloat(node.style.lineHeight) : 0)
    },
    addTag (event) {
      document.getElementById('add_tag_container').classList.toggle('hidden', false)
      document.getElementById('add_tag_background').classList.toggle('hidden', false)
      let tags = document.getElementsByClassName('selected_exercise')[0].getAttribute('tags')
      if (tags !== null) {
        let tagsSplit = tags.split(';')
        for (let tagIndex = 0; tagIndex < tagsSplit.length; tagIndex++) {
          let tagSplit = tagsSplit[tagIndex].split(':')
          this.createTag(tagSplit[0], tagSplit[1])
        }
      }
      event.stopPropagation()
    },
    createTag (id, title) {
      let tag = document.createElement('div')
      tag.classList.toggle('topic_tag', true)
      tag.textContent = title
      tag.setAttribute('tagID', id)
      let tagClose = document.createElement('div')
      tagClose.classList.toggle('topic_tag_close', true)
      tagClose.textContent = 'X'
      tagClose.onclick = function () {
        tag.parentNode.removeChild(tag)
      }
      tag.appendChild(tagClose)
      document.getElementById('topic_tags').appendChild(tag)
    },
    hasTag (id) {
      let child = document.getElementById('topic_tags').firstChild
      if (child === null) {
        return false
      }
      do {
        if (parseInt(child.getAttribute('tagID')) === id) {
          return true
        }
      } while ((child = child.nextSibling) !== null)
      return false
    },
    closeTags (event) {
      document.getElementById('add_tag_container').classList.toggle('hidden', true)
      document.getElementById('add_tag_background').classList.toggle('hidden', true)
      document.getElementById('tag_input').value = ''

      let tagContainer = document.getElementById('topic_tags')
      let selectedExercise = document.getElementsByClassName('selected_exercise')[0]
      let tagsString = ''
      while (tagContainer.hasChildNodes()) {
        let child = tagContainer.firstChild
        child.removeChild(child.lastChild)
        tagsString += child.getAttribute('tagID') + ':' + child.textContent + ';'
        tagContainer.removeChild(child)
      }
      selectedExercise.setAttribute('tags', tagsString.substring(0, tagsString.length - 1))
      event.stopPropagation()
    },
    searchTag () {
      let search = document.getElementById('tag_input').value
      let resultBox = document.getElementById('tag_search_result')
      let child = resultBox.firstChild
      while ((child = resultBox.firstChild) != null) {
        resultBox.removeChild(child)
      }
      if (search === '') {
        return
      }
      for (let tagIndex = 0; tagIndex < this.tags.length; tagIndex++) {
        if (this.tags[tagIndex].title.toLowerCase().includes(search.toLowerCase()) && !this.hasTag(this.tags[tagIndex].id)) {
          let tag = document.createElement('div')
          let context = this
          tag.onclick = function () {
            if (!context.hasTag(context.tags[tagIndex].id)) {
              context.createTag(context.tags[tagIndex].id, context.tags[tagIndex].title)
            }
            tag.parentNode.removeChild(tag)
          }
          tag.classList.toggle('tag_search_result', true)
          tag.textContent = this.tags[tagIndex].title
          resultBox.appendChild(tag)
        }
      }
    },
    submitExercises () {
      let canSubmit = true
      let exercises = document.getElementsByClassName('exercise')
      for (let exerciseIndex = 0; exerciseIndex < exercises.length; exerciseIndex++) {
        let exercise = exercises[exerciseIndex]
        if (exercise.parentNode.classList.contains('exercise')) {
          continue
        }
        let hasTags = exercise.getAttribute('tags') !== null && exercise.getAttribute('tags') !== ''
        let hasTitle = exercise.getAttribute('title') !== null && exercise.getAttribute('title') !== ''
        canSubmit = canSubmit && hasTags && hasTitle
        exercise.classList.toggle('missing_tags', !hasTags || !hasTitle)
      }
      if (canSubmit) {
        this.submitInfo = 'Submitting exercises, you will be redirected when all exercises have been submitted'
        for (let exerciseIndex = 0; exerciseIndex < exercises.length; exerciseIndex++) {
          let exercise = exercises[exerciseIndex]
          let solution = null
          let afterSolution = null
          if (this.hasSolution(exercise)) {
            solution = this.getSolution(exercise)
            afterSolution = solution.nextSibling
            exercise.removeChild(solution)
          }
          let title = exercise.getAttribute('title')
          let tagsSplit = exercise.getAttribute('tags').split(';')
          let tags = []
          for (let index = 0; index < tagsSplit.length; index++) {
            tags[index] = parseInt(tagsSplit[index].split(':')[0])
          }
          if (solution !== null) {
            api.createExerciseWithSolution(this, exercise.innerHTML, solution.innerHTML, title, tags, () => {}, () => {})
            if (afterSolution === null) {
              exercise.appendChild(solution)
            } else {
              exercise.insertBefore(solution, afterSolution)
            }
          } else {
            api.createExercise(this, exercise.innerHTML, title, tags, () => {}, () => {})
          }
        }
        this.$router.push('/')
      } else {
        this.submitInfo = 'Make sure all exercises have at least one tag and a title before submitting. All exercises missing tags or title have been marked red'
      }
    },
    toggleAddExercise () {
      this.creation = !this.creation
      document.body.style.cursor = this.creation ? 'crosshair' : 'default'
    },
    createExercise (event) {
      if (this.creation) {
        let targetNode = event.target
        if (targetNode.parentNode.classList.contains('exercise')) {
          if (this.selected !== null && this.selected === targetNode.parentNode && !this.hasSolution(this.selected)) {
            let solution = document.createElement('div')
            solution.classList.toggle('solution', true)
            solution.style.top = targetNode.style.top
            solution.style.height = this.getHeight(targetNode) + 'pt'
            targetNode.parentNode.insertBefore(solution, targetNode)
            targetNode.parentNode.removeChild(targetNode)
            targetNode.style.top = 0
            solution.appendChild(targetNode)
            this.addSolutionEventHandlers(solution)
            this.moveExerciseTopUp(solution, this.getOffsetTop(solution) - 5)
            this.moveExerciseBottomDown(solution, this.getOffsetTop(solution) + parseFloat(solution.style.height) / 0.75 + 5)
            this.toggleAddSolution(event)
            event.stopPropagation()
          }
          return
        }
        let exercise = document.createElement('div')
        exercise.classList.toggle('exercise', true)
        exercise.style.top = targetNode.style.top
        exercise.style.height = this.getHeight(targetNode) + 'pt'
        targetNode.parentNode.insertBefore(exercise, targetNode)
        targetNode.parentNode.removeChild(targetNode)
        targetNode.style.top = 0
        exercise.appendChild(targetNode)
        this.addExerciseEventHandlers(exercise)
        this.moveExerciseTopUp(exercise, this.getOffsetTop(exercise) - 5)
        this.moveExerciseBottomDown(exercise, this.getOffsetTop(exercise) + parseFloat(exercise.style.height) / 0.75 + 5)
        this.toggleAddExercise()
      }
    },
    getSolution (exercise) {
      let child = exercise.firstChild
      while (child !== null) {
        if (child.classList.contains('solution')) {
          return child
        }
        child = child.nextSibling
      }
      return null
    },
    hasSolution (exercise) {
      return this.getSolution(exercise) !== null
    },
    toggleAddSolution (event) {
      event.stopPropagation()
      if (this.selected !== null) {
        this.creation = !this.creation
        if (this.hasSolution(this.selected)) {
          this.creation = false
        }
      }
    },
    deleteSolution (solution) {
      let top = parseFloat(solution.style.top)
      let node = solution.firstChild
      while ((node = solution.firstChild) !== null) {
        node.style.top = top + parseFloat(node.style.top) + 'pt'
        solution.removeChild(node)
        solution.parentNode.insertBefore(node, solution)
      }
      solution.parentNode.removeChild(solution)
    },
    addCursorHandlers (container, inverse) {
      let context = this
      container.onmousemove = function (event) {
        if ((context.selected === null && !inverse) || (context.selected !== null && inverse)) {
          if (container.classList.contains('solution')) {
          }
          container.style.cursor = 'pointer'
          if (context.hoveringTopOfExercise(container, event) && (container.hasChildNodes() || context.canMoveExerciseUp(container))) {
            container.style.cursor = (context.canMoveExerciseUp(container) ? 'n' : '') + (container.hasChildNodes() ? 's' : '') + '-resize'
          } else if (context.hoveringBottomOfExercise(container, event) && (container.hasChildNodes() || context.canMoveExerciseDown(container))) {
            container.style.cursor = (container.hasChildNodes() ? 'n' : '') + (context.canMoveExerciseDown(container) ? 's' : '') + '-resize'
          }
        } else {
          if (context.selected === container && context.creation) {
            container.style.cursor = 'crosshair'
          } else {
            container.style.cursor = 'default'
          }
        }
      }
    },
    addSolutionEventHandlers (solution) {
      let context = this
      solution.onmousedown = function (event) {
        if (context.selected === solution.parentNode) {
          if (context.hoveringBottomOfExercise(solution, event)) {
            context.moving.exercise = solution
            context.moving.direction = 'down'
            event.stopPropagation()
          } else if (context.hoveringTopOfExercise(solution, event)) {
            context.moving.exercise = solution
            context.moving.direction = 'up'
            event.stopPropagation()
          }
        }
      }
      this.addCursorHandlers(solution, true)
    },
    addExerciseEventHandlers (exercise) {
      let context = this
      exercise.onmousedown = function (event) {
        if (context.selected === null) {
          if (context.hoveringBottomOfExercise(exercise, event)) {
            context.moving.exercise = exercise
            context.moving.direction = 'down'
          } else if (context.hoveringTopOfExercise(exercise, event)) {
            context.moving.exercise = exercise
            context.moving.direction = 'up'
          }
        }
        if (!(context.hoveringTopOfExercise(exercise, event) || context.hoveringBottomOfExercise(exercise, event))) {
          context.toggleSelected(exercise)
        }
        event.stopPropagation()
      }
      this.addCursorHandlers(exercise, false)
    }
  }
}
</script>

<style>

.context_container .p, .context_container .r {
  position: absolute;
}

@supports(-webkit-text-stroke: 1px black) {
  .context_container .p {
    text-shadow: none !important;
  }
}

.context_container .container {
  width: 595pt;
  position: relative;
}

.context_container .exercise{
  position: absolute;
  width: calc(595pt + 4px);
  margin-left: -2px;
  border: 2px solid var(--p-color-1);
}

.options .options_button {
  height: 20px;
  font-size: 15px;
  background-color: var(--p-color-1);
  border-bottom: 2px solid var(--p-color-1);
  text-align: center;
}

.options .options_button:hover {
  cursor: pointer;
  background-color: var(--p-color-3);
}

.context_container .selected_exercise {
  z-index: 2;
}

.context_container .selected_exercise:hover {
  cursor: auto;
}

.context_container .missing_tags {
  border-color: #ff0000;
}

.exercise .solution {
  width: calc(100% - 4px);
  border: 2px solid var(--n-color-5);
  margin-top: -2px;
  position: absolute;
}

.blurred {
  -webkit-filter: blur(2px);
  -moz-filter: blur(2px);
  -o-filter: blur(2px);
  -ms-filter: blur(2px);
  filter: blur(2px);
}

.hidden {
  display: none
}

.options {
  left: calc(595pt + 4px);
  width: 70px;
  margin-top: -2px;
  text-align: center;
  border: 2px solid var(--p-color-1);
  position: absolute;
  background-color: var(--n-color-2);
}

.add_tag_full {
  z-index: 10;
  position: fixed;
  width: 100%;
  height: 100%;
  opacity: 0.75;
  left: 0;
  top: 0;
  background-color: var(--n-color-2);
}

.topic_tag {
  padding: 3px 3px 2px 5px;
  margin-left: 10px;
  margin-bottom: 5px;
  border-radius: 3px;
  background-color: var(--p-color-1);
  display: inline-block;
}

.topic_tag_close  {
  display: inline-block;
  font-size: 10px;
  right: 4px;
  padding-left: 2px;
  vertical-align: top;
  color: var(--nn-color-1);
}

.topic_tag_close:hover {
  cursor: pointer;
}

.tag_search_result {
  margin: 8px;
  width: 268px;
}

.tag_search_result:hover {
  cursor: pointer;
}

.submitInfo {
  margin-top: 20px;
  margin-bottom: 5px;
}

.global_exercise_options {
  width: 30px;
  height: 30px;
  text-align: center;
  position: fixed;
  right: 0;
  top: 200px;
  background-color: var(--p-color-1);
}

.global_exercise_options:hover {
  cursor: pointer;
  background-color: var(--p-color-3);
}

.add_exercise {
  font-size: 25px;
}

.titlebox {
  position: absolute;
  top: -35px;
  height: 35px;
  left: -2px;
  border: 2px solid var(--p-color-1);
  border-radius: 0;
  padding-left: 5px;
  margin: 0;
}

</style>

<style scoped>
.context_container {
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

.add_tag_container input[type=text] {
  width: 260px;
}
.close_tag_input {
  position: absolute;
  border-radius: 3px;
  left: 320px;
  top: 8px;
  font-size: 15px;
  width: 12px;
  height: 12px;
}

.add_tag_input {
  width: 320px;
  margin-left: -20px;
  padding-left: 20px;
  border-bottom: 2px solid var(--n-color-4);
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.close_tag_input:hover {
  cursor: pointer;
}

.add_tag_container {
  z-index: 11;
  position: fixed;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background-color: var(--n-color-2);
  border: 2px solid var(--n-color-4);
  border-radius: 15px;
  padding: 20px;
  width: 300px;
  opacity: 1;
  min-height: 100px;
}

.tags {
  width: 300px;
  min-height: 50px;
}

</style>
