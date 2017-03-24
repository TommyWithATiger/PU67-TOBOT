<template>
  <div class="page-content">
    <div class="form-content">
      <div class="form-row">
        <label for="username">Username: </label>
        <input
          @keydown.enter="register"
          placeholder="Username"
          v-model="username"
          id="username"
          autofocus
          autocorrect="false"
          :class="(this.usernameFeedback$ && this.usernameFeedback$.valid) ? '' : 'invalid-input'"
          type="text">
        <div class="inputError">{{ (this.usernameFeedback$ && this.usernameFeedback$.message) }}</div>
      </div>
      <div class="form-row">
        <label for="password">Password: </label>
        <input
          @keydown.enter="register"
          placeholder="Password"
          v-model="password"
          id="password"
          autocorrect="false"
          :class="(this.passwordFeedback$ && this.passwordFeedback$.valid) ? '' : 'invalid-input'"
          type="password">
        <div class="inputError">{{ (this.passwordFeedback$ && this.passwordFeedback$.message) }}</div>
      </div>
      <div class="form-row">
        <label for="email">E-mail: </label>
        <input
          @keydown.enter="register"
          placeholder="E-mail"
          v-model="email"
          id="email"
          autocorrect="false"
          :class="(this.emailFeedback$ && this.emailFeedback$.valid) ? '' : 'invalid-input'"
          type="email">
        <div class="inputError">{{ (this.emailFeedback$ && this.emailFeedback$.message) }}</div>
      </div>
      <div class="form-row userType">
        <input v-model="userType" type="radio" id="student" value="Student"><label for="student">Student</label>
        <input v-model="userType" type="radio" id="teacher" value="Teacher"><label for="teacher">Teacher</label>
      </div>
      <div class="form-row">
        <button class="registerButton" @click="register" :disabled="canSubmit ? null : 'true'">Registrer</button>
      </div>
    </div>
  </div>
</template>

<script>
import Rx from 'rxjs/Rx'

import { api } from 'api'
import { auth } from 'auth'

export default {
  name: 'relatetopicsubjectpage',
  data () {
    return {
      username: '',
      password: '',
      email: '',
      userType: 'Student'
    }
  },
  computed: {
    canSubmit () {
      return (this.usernameFeedback$ && this.usernameFeedback$.valid) &&
        (this.passwordFeedback$ && this.passwordFeedback$.valid) &&
        (this.emailFeedback$ && this.emailFeedback$.valid)
    }
  },
  methods: {
    register () {
      if (this.canSubmit) {
        api.registerUser(this, this.username, this.email, this.password, this.userType, (data) => {
          auth.login({
            username: this.username,
            password: this.password
          }, this, '/', () => {
          }, () => {
            this.$router.push('/login')
          })
        })
      }
    }
  },
  subscriptions () {
    return {
      usernameFeedback$: this.$watchAsObservable('username')
        .pluck('newValue')
        .filter(term => term.length > 2)
        .debounceTime(100)
        .distinctUntilChanged()
        .switchMap((term) => {
          return Rx.Observable.fromPromise(
            api.checkRegistrationData(this, term.trim(), this.email, this.password)
          )
        })
        .map((res) => {
          return {
            valid: res.username_valid,
            message: res.username_message
          }
        }),
      passwordFeedback$: this.$watchAsObservable('password')
        .pluck('newValue')
        .filter(term => term.length > 1)
        .debounceTime(100)
        .distinctUntilChanged()
        .switchMap((term) => {
          return Rx.Observable.fromPromise(
            api.checkRegistrationData(this, this.username, this.email, term.trim())
          )
        })
        .map((res) => {
          return {
            valid: res.password_valid,
            message: res.password_message
          }
        }),
      emailFeedback$: this.$watchAsObservable('email')
        .pluck('newValue')
        .filter(term => term.length > 1)
        .debounceTime(100)
        .distinctUntilChanged()
        .switchMap((term) => {
          return Rx.Observable.fromPromise(
            api.checkRegistrationData(this, this.username, term.trim(), this.password)
          )
        })
        .map((res) => {
          return {
            valid: res.email_valid,
            message: res.email_message
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

.form-row > input[type="text"].invalid-input,
.form-row > input[type="password"].invalid-input,
.form-row > input[type="email"].invalid-input {
  border-color: red;
}

.form-row > input[type="text"],
.form-row > input[type="password"],
.form-row > input[type="email"] {
  text-align: center;
  width: 100%;
}

.userType {
  text-align: center;
}

.inputError {
  padding-left: 10px;
  padding-top: 5px;
  color: #ff0000;
  font-size: 14px;
  text-align: center;
  width: 
}

.userType > input {
  margin-left: 40px;
  height: 18px;
  width: 18px;
  margin-top: 15px;
}
</style>
