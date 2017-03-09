<template>
  <input v-model="search" v-on:input="searchHandler" />
</template>

<script>
import Rx from 'rxjs/Rx'

import { api } from 'api'

export default {
  name: 'searchbar',
  props: [ 'initialValue' ],
  data () {
    return {
      search: ''
    }
  },
  created () {
    this.search = this.initialValue || ''
  },
  methods: {
    searchHandler () {
      this.$emit('search', this.search)
      if (!this.search.length) {
        this.$emit('subjectResult', [])
        this.$emit('topicResult', [])
      }
    }
  },
  watch: {
    '$route' (to, from) {
      this.search = ''
    }
  },
  subscriptions () {
    return {
      subjectResult$: this.$watchAsObservable('search')
        .pluck('newValue')
        .filter(term => term.length > 1)
        .debounceTime(100)
        .distinctUntilChanged()
        .switchMap((term) => {
          return Rx.Observable.fromPromise(
            api.getSubjectsByTitle(this, term.trim())
          )
        })
        .map((res) => {
          this.$emit('subjectResult', res.subjects.slice(0, 3))
          return res.subjects.slice(0, 3)
        }),
      topicResult$: this.$watchAsObservable('search')
        .pluck('newValue')
        .filter(term => term.length > 1)
        .debounceTime(100)
        .distinctUntilChanged()
        .switchMap((term) => {
          return Rx.Observable.fromPromise(
            api.getTopicsByTitle(this, term.trim())
          )
        })
        .map((res) => {
          this.$emit('topicResult', res.topics.slice(0, 3))
          return res.topics.slice(0, 3)
        })
    }
  }
}
</script>

<style scoped>
</style>
