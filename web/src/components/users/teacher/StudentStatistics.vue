<template>
  <div>
    <h2>Statistics from students in your subjects:</h2>
    <div v-for="s in subjects">
      <h3><router-link :to="'/subject/' + s.id">{{ s.title }}</router-link></h3>
      <p v-if="s.relatedTopics && !s.relatedTopics.length" class="related-topics-empty">No topics.</p>
      <div v-for="t in s.relatedTopics" class="related-topics">
        <router-link :to="'/topic/' + t.id">{{ t.title }}</router-link>
        <span v-if="t.maxRating">
          <highcharts :options="t.chartOptions" ref="highcharts"></highcharts>
        </span>
        <span v-else> - Not rated</span>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'studentstatistics',
  data () {
    return {
      subjects: {},
      ratingToText: {
        '0': 'Not rated',
        '1': 'Bad',
        '2': 'Below Average',
        '3': 'Average',
        '4': 'Good',
        '5': 'Superb'
      }
    }
  },
  created () {
    api.getSubjectsEditor(this, (data) => {
      for (let s of data.subjects) {
        this.$set(this.subjects, s.id, s)

        api.getRelatedTopicsCount(this, s.id, (data) => {
          let rt = data.related_topics
          for (let i in data.related_topics) {
            let rating = rt[i].ratingCount.reduce((e, a) => e > a ? e : a, 0)
            rt[i].maxRating = rating
            rt[i].chartOptions = this.getOptions(rt[i].ratingCount)
          }
          this.$set(this.subjects[s.id], 'relatedTopics', rt)
        })
      }
    })
  },
  methods: {
    getOptions (ratings) {
      return {
        chart: {
          type: 'pie',
          width: 150,
          height: 65,
          spacingBottom: 0,
          spacingTop: 0,
          spacingLeft: 25,
          spacingRight: 25
        },
        title: {
          text: ''
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b><br />Count: <b>{point.y}</b>'
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: false
            },
            showInLegend: false
          }
        },
        series: [{
          name: 'Rate',
          colorByPoint: true,
          data: ratings.slice(1).map((e, i) => {
            return {
              name: this.ratingToText[i + 1],
              color: '#00FF00',
              y: e
            }
          })
        }]
      }
    }
  }
}
</script>

<style scoped>
.related-topics,
.related-topics-empty {
  padding-left: 32px;
  display: inline-block;
  text-align: center;
  width: 150px;
  font-weight: bold;
}

.related-topics-empty {
  font-style: italic;
}

a {
  text-decoration: none;
}
</style>

<style>
[data-highcharts-chart] {
  display: block;
  width: auto;
  position: relative;
}

.highcharts-container {
  display: inline-block;
  width: auto;
}

.highcharts-background {
  fill: transparent;
}

.highcharts-legend-item text {
  fill: #333;
  fill: var(--nn-color-2);
}

.highcharts-legend-item:hover text {
  fill: #222;
  fill: var(--nn-color-1);
}

.highcharts-pie-series .highcharts-point {
  stroke: transparent;
}

.highcharts-pie-series .highcharts-point-select {
  fill: inherit;
}

.highcharts-color-0 { fill: rgba(255, 0, 0, .8); }
.highcharts-color-1 { fill: rgba(255, 127, 0, .8); }
.highcharts-color-2 { fill: rgba(255, 255, 0, .8); }
.highcharts-color-3 { fill: rgba(127, 255, 0, .8); }
.highcharts-color-4 { fill: rgba(0, 255, 0, .8); }

.highcharts-color-0.highcharts-point-select { fill: rgba(255, 0, 0, 1); }
.highcharts-color-1.highcharts-point-select { fill: rgba(255, 127, 0, 1); }
.highcharts-color-2.highcharts-point-select { fill: rgba(255, 255, 0, 1); }
.highcharts-color-3.highcharts-point-select { fill: rgba(127, 255, 0, 1); }
.highcharts-color-4.highcharts-point-select { fill: rgba(0, 255, 0, 1); }

.highcharts-credits {
  display: none;
}
</style>
