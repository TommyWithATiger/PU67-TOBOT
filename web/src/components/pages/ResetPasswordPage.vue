<template>
  <div class="page-content">
    <div class="form-content">
      <h2>Reset password</h2>
      <div class="feedback">
        {{ feedback }}
      </div>
      <div class="form-row">
        <label for="password">New password: </label>
        <input @keydown.enter="submit" placeholder="New password" v-model="password" id="password" type="password">
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
      password: '',
      token: '',
      email: '',
      feedback: ''
    }
  },
  created () {
    this.token = this.$route.params.token
    this.email = this.$route.params.email.replace(new RegExp('\\^', 'g'), '.')
  },
  methods: {
    submit () {
      api.resetPassword(this, this.email, this.password, this.token, (data) => {
        this.$router.push('/login')
      }, (err) => {
        err
        if (this.password.length < 8) {
          this.feedback = 'Password must be of length 8'
        } else {
          this.feedback = 'Reset link expired, try requesting a new one'
        }
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

.form-row > input[type="password"] {
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
  color: #ff0000;
}
</style>
