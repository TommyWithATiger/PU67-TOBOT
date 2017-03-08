<template>
  <div class="search-container" v-on:blur="hideResult">
    <input class="search-box" v-model="search" v-on:focus="showResult = true" placeholder="Søk..." />
    <div class="search-result" v-if="showResult && search.length && result$">
      <router-link v-for="s in result$.subjects" :to="'/subject/' + s.id">{{ s.title }}</router-link>
    </div>
  </div>
</template>

<script>
import Rx from 'rxjs/Rx'

import { api } from 'api'

export default {
  name: 'header',
  data () {
    return {
      search: '',
      showResult: false
    }
  },
  watch: {
    '$route' (to, from) {
      this.$router.go({
        path: to.path
      })
    }
  },
  methods: {
    hideResult () {
      setTimeout(() => {
        this.showResult = false
      }, 1000)
    }
  },
  subscriptions () {
    return {
      result$: this.$watchAsObservable('search')
        .pluck('newValue')
        .filter(term => term.length > 1)
        .debounceTime(100)
        .distinctUntilChanged()
        .switchMap((term) => {
          return Rx.Observable.fromPromise(
            api.getSubjectsByTitle(this, term)
          )
        })
        .map((res) => {
          return {
            subjects: res.subjects
          }
        })
    }
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
  background-color: rgba(0, 0, 0, .2);
  display: flex;
  flex-direction: column;
}

.search-result > a {
  padding: 4px 8px;
  box-sizing: border-box;
}
.search-result > a:hover {
  background-color: rgba(0, 0, 0, .2);
}
</style>
