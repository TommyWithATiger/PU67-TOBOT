<template>
  <div class="search-container">
    <input class="search-box" v-model="search" placeholder="Søk..." v-on:focus="showResult" v-on:blur="hideResult"/>
    <div class="search-result" v-if="showBar && search.length && result$">
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
