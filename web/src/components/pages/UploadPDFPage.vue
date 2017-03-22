<template>
	<div class="page-content">
    <div id="feedback" class="hidden feedback">
      Could not read the uploaded file. Is it a PDF?
    </div>
		<input v-on:change="registerFileChange" type="file" name="file">
		<input @click="upload" type="submit">
    <div id="loader" class="hidden">
      <div class="spinner">
      </div>
      <div>
        Extracting exercises from the PDF, this will take a couple of seconds. Sit tight!
      </div>
		</div>
	</div>
</template>

<script>
import { api } from 'api'
import { store } from 'store'

export default {
  name: 'uploadpage',
  data () {
    return {
      file: ''
    }
  },
  methods: {
    upload () {
      document.getElementById('loader').classList.toggle('hidden', false)
      document.getElementById('feedback').classList.toggle('hidden', true)
      let reader = new FileReader()
      let context = this
      reader.onload = function (event) {
        api.uploadPDF(context, event.target.result, (data) => {
          store.exercise_creation_context = data.content
          context.$router.push('/exercise/create')
        }, (err) => {
          document.getElementById('loader').classList.toggle('hidden', true)
          document.getElementById('feedback').classList.toggle('hidden', false)
          console.log(err)
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

<style scoped>
.hidden {
  display: none;
}

.feedback {
  color: #f00;
}

.spinner {
    border: 8px solid var(--n-color-3);
    border-top: 8px solid var(--p-color-1);
    border-radius: 50%;
    width: 50px;
    height: 50px;
    animation: spin 2s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

</style>
