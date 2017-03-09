<template>
  <div class="search-container">
    <Search
      class="search-box"
      v-on:topicResult="topicHandler"
      v-on:subjectResult="subjectHandler"
      v-on:search="termChange"
      placeholder="Søk..."
    />
    <div class="search-result" v-if="(subjects && subjects.length) || (topics && topics.length)">
      <router-link v-for="s in subjects" :to="'/subject/' + s.id">{{ s.title }}</router-link>
      <router-link v-for="s in topics" :to="'/topic/' + s.id">{{ s.title }}</router-link>
      <router-link :to="'/search/' + search">Flere resultater...</router-link>
    </div>
    <div class="search-result" v-else-if="search.length > 1">
      <p>Fant ingen resultater.</p>
    </div>
  </div>
</template>

<script>
import Search from 'components/search/Search'

export default {
  name: 'searchbar',
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
      this.search = term
    },
    showResult () {
      this.showBar = true
      this.hidingResult = false
    },
    hideResult () {
      this.hidingResult = true
      setTimeout(() => {
        if (this.hidingResult) this.showBar = false
      }, 100)
    }
  },
  components: {
    Search
  }
}
</script>

<style scoped>
.search-container {
  position: relative;
  width: 200px;
  max-width: 100%;
  box-sizing: border-box;
}

.search-box {
  width: 100%;
  box-sizing: border-box;
}

.search-result {
  position: absolute;
  width: 100%;
  background-color: #aaa;
  display: flex;
  flex-direction: column;
}

.search-result > a,
.search-result > p {
  padding: 4px 8px;
  box-sizing: border-box;
}
.search-result > a:hover {
  background-color: #888;
}
</style>
