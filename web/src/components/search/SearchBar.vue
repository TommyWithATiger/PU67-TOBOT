<template>
  <div class="search-container">
    <Search
      class="search-box"
      @topicResult="topicHandler"
      @subjectResult="subjectHandler"
      @search="termChange"
      placeholder="Søk..."
    />
    <div class="search-result" v-if="showBar && ((subjects && subjects.length) || (topics && topics.length))">
      <router-link v-for="s in subjects" :to="'/subject/' + s.id">{{ s.title }}</router-link>
      <router-link v-for="s in topics" :to="'/topic/' + s.id">{{ s.title }}</router-link>
      <router-link :to="'/search/' + search">Flere resultater...</router-link>
    </div>
    <div class="search-result" v-else-if="search.length > 1 && showBar">
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
      this.hideResult()
    }
  },
  methods: {
    topicHandler (topics) {
      this.topics = topics.slice(0, 3)
    },
    subjectHandler (subjects) {
      this.subjects = subjects.slice(0, 3)
    },
    termChange (term) {
      this.showBar = !!term.length
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
  margin-top: 8px;
  background-color: #fff;
  background-color: var(--n-color-1);
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  box-shadow: var(--box-shadow-2);
}

.search-result > a,
.search-result > p {
  padding: 8px 12px;
  box-sizing: border-box;
}

.search-result > a:hover {
  background-color: #ddd;
  background-color: var(--n-color-2);
}
</style>
