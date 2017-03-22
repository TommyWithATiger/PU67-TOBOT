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
      this.$router.go(window.history.back())
    }
    this.creationContext = store.exercise_creation_context
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
