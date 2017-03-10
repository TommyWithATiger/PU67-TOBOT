<template>
  <div class="page-content">
    <div class="form-content">
      <h2>Request password reset</h2>
      <div class="feedback" id="feedback">
        {{ feedback }}
      </div>
      <div class="form-row">
        <label for="email">E-mail: </label>
        <input @keydown.enter="submit" placeholder="E-mail" v-model="email" id="email" type="email">
      </div>
      <div class="form-row">
        <button class="submit" @click="submit">Submit</button>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'relatetopicsubjectpage',
  data () {
    return {
      email: '',
      feedback: ''
    }
  },
  methods: {
    submit () {
      api.requestPasswordReset(this, this.email, (data) => {
        this.feedback = 'An e-mail has been sent to your address'
        document.getElementById('feedback').classList.toggle('error', false)
      }, (err) => {
        err
        document.getElementById('feedback').classList.toggle('error', true)
        this.feedback = 'Could not send a reset request to that e-mail, make sure that it is a valid e-mail'
      })
    }
  }
}
</script>

<style scoped>
.form-content {
  position: absolute;
  font-size: 20px;
  width: 400px;
  left: 50%;
  top: 50%;
  margin-left: -200px;
  margin-top: -70px;
}

.form-row {
  padding-bottom: 10px;
}

.form-row > label {
  width: 100px;
  display: inline-block;
  vertical-align: middle;
  font-weight: bold;
}

.form-row > input[type="email"] {
  height: 25px;
  width: 285px;
  border: 1px solid gray;
  border-radius: 7px;
  flow: right;
  padding: 2px;
  font-size: 15px;
  text-align: center;
}

.submit {
  width: 150px;
  height: 35px;
  background: #FFF;
  border: 1px solid gray;
  border-radius: 5px;
  margin-left: 105px;
}

.feedback {
  padding-bottom: 5px;
  font-size: 18px;
  color: #007700;
}

.error {
  color: #ff0000;
}
</style>
