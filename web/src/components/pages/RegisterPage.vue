<template>
  <div class="page-content">
    <div class="form-content">
      <div class="form-row">
        <label for="username">Username: </label>
        <input
          @focusout="checkRegistration"
          @keydown.enter="register"
          placeholder="Username"
          v-model="username"
          id="username"
          :class="usernameValid ? '' : 'invalid-input'"
          type="text">
        <div class="inputError">{{ usernameError }}</div>
      </div>
      <div class="form-row">
        <label for="password">Password: </label>
        <input
          @focusout="checkRegistration"
          @keydown.enter="register"
          placeholder="Password"
          v-model="password"
          id="password"
          :class="passwordValid ? '' : 'invalid-input'"
          type="password">
        <div class="inputError">{{ passwordError }}</div>
      </div>
      <div class="form-row">
        <label for="email">E-mail: </label>
        <input
          @focusout="checkRegistration"
          @keydown.enter="register"
          placeholder="E-mail"
          v-model="email"
          id="email"
          :class="emailValid ? '' : 'invalid-input'"
          type="email">
        <div class="inputError">{{ emailError }}</div>
      </div>
      <div class="form-row userType">
        <input v-model="userType" type="radio" id="student" value="Student"><label for="student">Student</label>
        <input v-model="userType" type="radio" id="teacher" value="Teacher"><label for="teacher">Teacher</label>
      </div>
      <div class="form-row">
        <button class="registerButton" @click="register" :disabled="canSubmit">Registrer</button>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'
import { auth } from 'auth'

export default {
  name: 'relatetopicsubjectpage',
  data () {
    return {
      username: '',
      password: '',
      email: '',
      usernameValid: true,
      passwordValid: true,
      emailValid: true,
      userType: 'Student',
      canSubmit: false,
      usernameError: '',
      passwordError: '',
      emailError: ''
    }
  },
  methods: {
    checkRegistration () {
      api.checkRegistrationData(this, this.username, this.email, this.password, (data) => {
        if (data.username_valid && data.password_valid && data.email_valid) {
          this.canSubmit = true
        }

        this.usernameValid = data.username_valid
        this.passwordValid = data.password_valid
        this.emailValid = data.email_valid

        this.usernameError = data.username_valid ? '' : data.username_message
        this.passwordError = data.password_valid ? '' : data.password_message
        this.emailError = data.email_valid ? '' : data.email_message
      })
    },
    register () {
      this.checkRegistration()
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
