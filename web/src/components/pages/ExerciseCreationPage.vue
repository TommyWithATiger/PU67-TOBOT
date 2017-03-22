<template>
	<div class="page-content">
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
  width: 595pt;
  border: 2px solid var(--p-color-1);
}

</style>
