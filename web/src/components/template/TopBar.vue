<template>
  <div class="header-content">
    <div class="header-flat">
      <router-link to="/"><img class="header-logo" v-bind:src="'/static/images/TOBOT_mascot.png'"></img></router-link>
    </div>
    <div class="header-flat header-fill-calc">
      <div v-if="authenticated" class="header-flat header-fill">
        <div v-for="link in links" v-if="checkUsertype(link.users)" class="header-navigation-button">
          <h2><router-link exact :to="link.path">{{ link.name }}</router-link></h2>
        </div>
        <SearchBar />
        <div class="header-user-info">
          <router-link :to="getUserUrl">{{ state.user.username }}</router-link>
          <LogoutBtn />
        </div>
      </div>
      <div v-else class="header-flat header-fill">
        <div class="header-user-info">
          <router-link to="/login">
            <button>Logg inn</button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LogoutBtn from 'components/auth/LogoutBtn'
import SearchBar from 'components/search/SearchBar'
import { auth } from 'auth'

export default {
  name: 'header',
  data () {
    return {
      links: [
        {
          name: 'Hjem',
          path: '/',
          users: ['Admin', 'Teacher', 'Student']
        },
        {
          name: 'Emner',
          path: '/subject',
          users: ['Admin', 'Teacher']
        },
        {
          name: 'Temaer',
          path: '/topic',
          users: ['Admin', 'Teacher']
        }
      ]
    }
  },

  // Recieving authentication from server on create.
  created () {
    this.$store.state.user.authenticated = auth.hasToken()
    this.$store.state.user.username = auth.getUsername()
    this.$store.state.user.usertype = auth.getUsertype()
  },
  computed: {

    // Making store user auth available through authenticated.
    authenticated () {
      return this.$store.state.user.authenticated
    },

    // Making store state available through state.
    state () {
      return this.$store.state
    },

    // Return user url
    getUserUrl () {
      return `/user/${this.$store.state.user.username}`
    }
  },
  methods: {
    checkUsertype (users) {
      return users.indexOf(this.$store.state.user.usertype) !== -1
    }
  },
  components: {
    LogoutBtn,
    SearchBar
  }
}
</script>

<style scoped>
.header-content {
  padding: 16px;
  background-color: #eee;
  flex: 0 0 64px;
}

.header-navigation-button {
  display: inline-block;
  width: 100px;
  height: 68px;
  padding: 4px;
  text-align: center;
}

.header-navigation-button > h2 > a:hover,
.header-navigation-button > h2 > a.router-link-active {
  border-bottom: 4px solid #87CEEB;
}

.header-navigation-button > h2 > a:visited {
  color: #333;
}

.header-navigation-button > h2 > a {
  color: #333;
  text-decoration: none;
}

.header-logo {
  display: inline-block;
  height: inherit;
  width: 180px;
  margin-right: 20px;
}

.header-flat {
  display: inline-block;
  vertical-align: top;
}

.header-user-info {
  display: inline-block;
  height: 68px;
  float: right;
}

.header-fill-calc {
  width: calc(100% - 250px);
}

.header-fill {
  width: 100%;
}

.header-user-info button {
  width: 100px;
  height: 30px;
  margin-top: 20px;
  background-color: #f7f7f7;
  border-radius: 2px;
  border: 1px solid #666;
  margin-left: 20px;
}

.header-user-info button:hover {
  border: 1px solid #333;
  background-color: #e9e9e9;
}
</style>
