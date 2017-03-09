<template>
  <div class="page-content">
    <h1>Søk blant emner og temaer:</h1>
    <Search
      class="search-box"
      v-on:topicResult="topicHandler"
      v-on:subjectResult="subjectHandler"
      v-on:search="termChange"
      placeholder="Søk..."
    />
    <div class="search-results">
      <h2>Emner</h2>
      <div class="search-list">
        <router-link to="some">Link</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import Search from 'components/search/Search'

export default {
  name: 'searchpage',
  data () {
    return {
      search: '',
      subjects: [],
      topics: [],
      showBar: false,
      hidingResult: false
    }
  },
  watch: {
    '$route' (to, from) {
      this.hideResult()
      let sFrom = from.path.split('/')
      let sTo = to.path.split('/')
      if (sFrom[1] === sTo[1] && sTo.length > 2 && sFrom.length > 2) {
        this.$router.go({
          path: to.path
        })
      }
    }
  },
  methods: {
    topicHandler (topics) {
      this.topics = topics
    },
    subjectHandler (subjects) {
      this.subjects = subjects
    },
    termChange (term) {
      this.showBar = !!term.length
      this.search = term
    }
  },
  components: {
    Search
  }
}
</script>

<style scoped>
</style>
