<template>
  <div class="page-content">
    <div class="form-content">
      <div class="form-row">
        <label for="username">Username: </label>
        <input @focusout="checkRegistration" @keydown.enter="register" placeholder="Username" v-model="username" id="username" type="text">
        <div class="inputError" id="username-error"></div>
      </div>
      <div class="form-row">
        <label for="password">Password: </label>
        <input @focusout="checkRegistration" @keydown.enter="register" placeholder="Password" v-model="password" id="password" type="password">
        <div class="inputError" id="password-error"></div>
      </div>
      <div class="form-row">
        <label for="email">E-mail: </label>
        <input @focusout="checkRegistration" @keydown.enter="register" placeholder="E-mail" v-model="email" id="email" type="email">
        <div class="inputError" id="email-error"></div>
      </div>
      <div class="form-row userType">
        <input v-model="userType" type="radio" name="userType" value="Student"> Student
        <input v-model="userType" type="radio" name="userType" value="Teacher"> Teacher 
      </div>
      <div class="form-row">
        <button class="registerButton" @click="register">Registrer</button>
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
      userType: 'Student',
      canSubmit: false
    }
  },
  methods: {
    checkRegistration () {
      api.checkRegistrationData(this, this.username, this.email, this.password, (data) => {
        console.log(data)
        if (data.username_valid && data.password_valid && data.email_valid) {
          this.canSubmit = true
        }
        document.getElementById('username').classList.toggle('invalidInput', !data.username_valid)
        document.getElementById('password').classList.toggle('invalidInput', !data.password_valid)
        document.getElementById('email').classList.toggle('invalidInput', !data.email_valid)
        if (!data.username_valid) {
          document.getElementById('username-error').textContent = data.username_message
        } else {
          document.getElementById('username-error').textContent = ''
        }
        if (!data.password_valid) {
          document.getElementById('password-error').textContent = data.password_message
        } else {
          document.getElementById('password-error').textContent = ''
        }
        if (!data.email_valid) {
          document.getElementById('email-error').textContent = data.email_message
        } else {
          document.getElementById('email-error').textContent = ''
        }
      }, (err) => {
        console.log(err)
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

          }, (err) => {
            console.log(err)
            this.$router.push('/login')
          })
        }, (err) => {
          console.log(err)
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

.form-row > input[type="text"].invalidInput, .form-row > input[type="password"].invalidInput, .form-row > input[type="email"].invalidInput{
	border-color: red;
}

.form-row > input[type="text"], .form-row > input[type="password"], .form-row > input[type="email"]  {
	height: 25px;
	width: 285px;
	border: 1px solid gray;
	border-radius: 7px;
	flow: right;
	padding: 2px;
	font-size: 15px;
	text-align: center;
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
