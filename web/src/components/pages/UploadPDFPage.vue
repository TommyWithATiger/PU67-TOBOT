<template>
	<div class="page-content">
		<input v-on:change="registerFileChange" type="file" name="file">
		<input @click="upload" type="submit">
		<div id="cont">
			{{ content }}
		</div>
	</div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'uploadpage',
  data () {
    return {
      file: '',
      content: ''
    }
  },
  methods: {
    upload () {
      let reader = new FileReader()
      let context = this
      reader.onload = function (event) {
        api.uploadPDF(context, event.target.result, (data) => {
          document.getElementById('cont').innerHTML = data.content
          context.content = data.content
        }, (err) => {
          err
        })
      }
      reader.readAsArrayBuffer(this.file)
    },
    registerFileChange (ele) {
      let files = ele.target.files || ele.dataTransfer.files
      this.file = files[0]
    }
  }
}
</script>

<style>
.p, .r {
  position:absolute;
}
@supports(-webkit-text-stroke: 1px black) {.p{text-shadow:none !important;}}
.container{width:595pt;position: relative;}
.exercise{position:absolute;width:595pt}
</style>
