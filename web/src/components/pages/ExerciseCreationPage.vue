<template>
	<div class="page-content" v-on:mousemove="onMove" v-on:mouseup="onUp">
		<div class="context_container" v-html="creationContext">
		</div>
	</div>
</template>

<script>
import { store } from 'store'

export default {
  name: 'exercisecreationpage',
  data () {
    return {
      creationContext: '',
      options: null,
      moving: {
        'exercise': null,
        'direction': null
      },
      selected: null
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
    this.options.innerHTML = '<div class="delete_button"> Delete </div>'
    this.options.childNodes[0].onmousedown = this.deleteSelected
  },
  mounted () {
    let exercises = document.getElementsByClassName('exercise')
    let context = this

    for (let index = 0; index < exercises.length; index++) {
      let exercise = exercises[index]

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

      exercise.onmousemove = function (event) {
        if (context.selected === null) {
          if (context.hoveringTopOfExercise(exercise, event)) {
            if (exercise.hasChildNodes() && context.canMoveExerciseUp(exercise)) {
              exercise.style.cursor = 'ns-resize'
            } else if (exercise.hasChildNodes()) {
              exercise.style.cursor = 's-resize'
            } else if (context.canMoveExerciseUp(exercise)) {
              exercise.style.cursor = 'n-resize'
            } else {
              exercise.style.cursor = 'pointer'
            }
          } else if (context.hoveringBottomOfExercise(exercise, event)) {
            if (exercise.hasChildNodes() && context.canMoveExerciseUp(exercise)) {
              exercise.style.cursor = 'ns-resize'
            } else if (exercise.hasChildNodes()) {
              exercise.style.cursor = 'n-resize'
            } else if (context.canMoveExerciseDown(exercise)) {
              exercise.style.cursor = 's-resize'
            } else {
              exercise.style.cursor = 'pointer'
            }
          } else {
            exercise.style.cursor = 'pointer'
          }
        } else {
          if (context.selected === exercise) {
            exercise.style.cursor = 'pointer'
          } else {
            exercise.style.cursor = 'default'
          }
        }
      }

      document.onselectstart = function (event) {
        return false
      }
    }
  },
  methods: {
    setupExerciseHandlers () {
      // Add handlers for dragging on the top/bottom
      // Add handlers for deleting, and selecting the exercise
    },
    deleteSelected (event) {
      let topSelected = parseFloat(this.selected.style.top)
      let firstChild = this.selected.firstChild
      while ((firstChild = this.selected.firstChild) !== this.options) {
        firstChild.style.top = topSelected + parseFloat(firstChild.style.top) + 'pt'
        this.selected.removeChild(firstChild)
        this.selected.parentNode.insertBefore(firstChild, this.selected)
        // statement
      }
      this.blurOtherExercises(this.selected, false)
      this.selected.parentNode.removeChild(this.selected)
      this.selected = null
      event.stopPropagation()
    },
    onMove (event) {
      if (this.moving.exercise !== null) {
        if (this.moving.direction === 'up') {
          if (this.canMoveExerciseUp(this.moving.exercise)) {
            this.moveExerciseTopUp(this.moving.exercise, this.translateHoverYPosition(event))
          }
          if (this.moving.exercise.hasChildNodes()) {
            this.moveExerciseTopDown(this.moving.exercise, this.translateHoverYPosition(event))
          }
        } else if (this.moving.direction === 'down') {
          if (this.moving.exercise.hasChildNodes()) {
            this.moveExerciseBottomUp(this.moving.exercise, this.translateHoverYPosition(event))
          }
          if (this.canMoveExerciseDown(this.moving.exercise)) {
            this.moveExerciseBottomDown(this.moving.exercise, this.translateHoverYPosition(event))
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
    toggleSelected (exercise) {
      console.log(this.selected)
      if (this.selected === null) {
        this.selected = exercise
        this.selected.appendChild(this.options)
        this.selected.classList.toggle('selected_exercise', true)
        this.blurOtherExercises(exercise, true)
      } else if (this.selected === exercise) {
        this.selected.removeChild(this.options)
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
      let exerciseTop = exercise.offsetTop + exercise.parentNode.offsetTop
      let y = this.translateHoverYPosition(event)
      return exerciseTop <= y && y < exerciseTop + 5
    },
    hoveringBottomOfExercise (exercise, event) {
      let exerciseBottom = exercise.offsetTop + exercise.parentNode.offsetTop + parseFloat(exercise.style.height) / 0.75
      let y = this.translateHoverYPosition(event)
      return exerciseBottom - 5 <= y && y <= exerciseBottom
    },
    canMoveExerciseUp (exercise) {
      return exercise.previousSibling !== null && !exercise.previousSibling.classList.contains('exercise')
    },
    canMoveExerciseDown (exercise) {
      return exercise.nextSibling !== null && !exercise.nextSibling.classList.contains('exercise')
    },
    moveExerciseTopUp (exercise, y) {
      let node = exercise
      while ((node = node.previousSibling) !== null) {
        if (node.offsetTop + node.parentNode.offsetTop < y || node.classList.contains('exercise')) {
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
      let reduceDiff = y - exercise.offsetTop - exercise.parentNode.offsetTop
      while (node != null) {
        if ((parseFloat(node.style.top) + this.getHeight(node)) / 0.75 >= reduceDiff) {
          break
        }
        let nextNode = node.nextSibling
        exercise.removeChild(node)
        exercise.parentNode.insertBefore(node, exercise)
        node.style.top = parseFloat(exercise.style.top) + parseFloat(node.style.top) + 'pt'
        node = nextNode
      }

      if (exercise.hasChildNodes()) {
        let changedTop = parseFloat(exercise.firstChild.style.top)
        exercise.style.top = parseFloat(exercise.style.top) + parseFloat(exercise.firstChild.style.top) + 'pt'
        this.moveExerciseChildern(exercise, changedTop)
        exercise.style.height = parseFloat(exercise.lastChild.style.top) + this.getHeight(exercise.lastChild) + 'pt'
      } else {
        exercise.parentNode.removeChild(exercise)
      }
    },
    moveExerciseBottomDown (exercise, y) {
      let node = exercise
      while ((node = node.nextSibling) !== null) {
        if (node.offsetTop + node.parentNode.offsetTop + parseFloat(this.getHeight(node)) / 0.75 > y || node.classList.contains('exercise')) {
          break
        }
        if (parseFloat(exercise.style.top) + parseFloat(exercise.style.height) < parseFloat(node.style.top) + this.getHeight(node)) {
          exercise.style.height = parseFloat(node.style.top) + this.getHeight(node) - parseFloat(exercise.style.top) + 'pt'
        }
        node.style.top = parseFloat(node.style.top) - parseFloat(exercise.style.top) + 'pt'
        node.parentNode.removeChild(node)
        exercise.appendChild(node)
      }
    },
    moveExerciseBottomUp (exercise, y) {
      let node = exercise.lastChild
      let reduceDiff = y - exercise.offsetTop - exercise.parentNode.offsetTop
      while (node != null) {
        if ((parseFloat(node.style.top)) / 0.75 <= reduceDiff) {
          break
        }
        let nextNode = node.previousSibling
        exercise.removeChild(node)
        exercise.parentNode.insertBefore(node, exercise.nextSibling)
        node.style.top = parseFloat(exercise.style.top) + parseFloat(node.style.top) + 'pt'
        node = nextNode
      }

      if (exercise.hasChildNodes()) {
        exercise.style.height = parseFloat(exercise.lastChild.style.top) + this.getHeight(exercise.lastChild) + 'pt'
      } else {
        exercise.parentNode.removeChild(exercise)
      }
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
    }
  }
}
</script>

<style>

.context_container {
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

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

.options .delete_button {
  height: 20px;
  width: 50px;
  font-size: 15px;
  background-color: var(--p-color-1);
  border-bottom: 2px solid var(--p-color-1);
  text-align: center;
}

.options .delete_button:hover {
  cursor: pointer;
  background-color: var(--p-color-3);
}

.context_container .selected_exercise {
  z-index: 2;
}

.blurred {
  -webkit-filter: blur(2px);
  -moz-filter: blur(2px);
  -o-filter: blur(2px);
  -ms-filter: blur(2px);
  filter: blur(2px);
}

.options {
  left: calc(595pt + 4px);
  width: 50px;
  margin-top: -2px;
  border: 2px solid var(--p-color-1);
  position: absolute;
  background-color: var(--n-color-2);
}

</style>
