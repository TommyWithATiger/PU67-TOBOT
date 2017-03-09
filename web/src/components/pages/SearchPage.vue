<template>
  <div class="page-content">
    <h1>Søk blant emner og temaer:</h1>
    <div class="search-container">
      <Search
        class="search-box"
        @topicResult="topicHandler"
        @subjectResult="subjectHandler"
        @search="termChange"
        :initialValue="search"
        placeholder="Søk..."
      />
      <div class="search-results">
        <h2>Emner</h2>
        <div class="search-list" v-if="subjects && subjects.length">
          <div class="search-item" v-for="s in subjects">
            <router-link :to="'/subject/' + s.id">{{ s.title }}</router-link>
            <span> - {{ s.description }}</span>
          </div>
        </div>
        <div v-else>Fant ingen emner.</div>
      </div>
      <div class="search-results">
        <h2>Temar</h2>
        <div class="search-list" v-if="topics && topics.length">
          <div class="search-item" v-for="t in topics">
            <router-link :to="'/topic/' + t.id">{{ t.title }}</router-link>
            <span> - {{ t.description }}</span>
          </div>
        </div>
        <div v-else>Fant ingen temaer.</div>
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
      topics: []
    }
  },
  created () {
    this.search = this.$route.params.term
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
      window.history.pushState(
        'search',
        'TOBOT',
        '/search/' + this.search
      )
    }
  },
  components: {
    Search
  }
}
</script>

<style scoped>
.search-container {
  display: flex;
  flex-direction: column;
  max-width: 420px;
  margin: auto;
}


</style>
