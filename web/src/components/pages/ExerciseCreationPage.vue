<template>
	<div class="page-content">
		<div>
			{{ creationContext }}
		</div>
	</div>
</template>

<script>
import { store } from 'store'

export default {
  name: 'exercisecreationpage',
  data () {
    return {
      creationContext: ''
    }
  },
  created () {
    if (store.exercise_creation_context === null) {
      this.$router.push(window.history.back())
    }
    this.creationContext = store.exercise_creation_context
    store.exercise_creation_context = null
  },
  methods: {

  }
}
</script>

<style scoped>

.p, .r {
  position:absolute;
}

@supports(-webkit-text-stroke: 1px black) {
  .p {
    text-shadow:none !important;
  }
}

.container {
  width:595pt;
  position: relative;
}

.exercise{
  position:absolute;
  width:595pt
}

</style>
