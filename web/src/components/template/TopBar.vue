<template>
  <div class="header background-color-n1">
    <div class="header-container">
      <router-link to="/"><Logo class="header-img" /></router-link>
      <div class="header-links" v-if="authenticated">
        <router-link v-for="link in links"
          v-if="checkUsertype(link.users)"
          exact
          :to="link.path">
          {{ link.name }}
        </router-link>
      </div>
      <SearchBar v-if="authenticated && !isSearchRoute" />
      <button class="header-user-menu-btn"
        v-if="authenticated"
        @click="userMenu = !userMenu">
        Menu
      </button>
      <div :class="`header-user-menu ${userMenu ? 'show' : ''}`" v-if="authenticated">
        <router-link :to="getUserUrl">{{ state.user.username }}</router-link>
        <span @click="changeTheme">Change theme</span>
        <LogoutBtn />
      </div>
    </div>
  </div>
</template>

<script>
import Logo from 'components/template/Logo'
import LogoutBtn from 'components/auth/LogoutBtn'
import SearchBar from 'components/search/SearchBar'
import { auth } from 'auth'
import { ChangeTheme } from 'common/ChangeTheme'

export default {
  name: 'header',
  data () {
    return {
      userMenu: false,
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

    // Checking if the route is on search page. Else hide searchbar.
    isSearchRoute () {
      return /^\/search/.test(this.$route.path)
    },

    // Return user url
    getUserUrl () {
      return `/user/${this.$store.state.user.username}`
    }
  },
  methods: {
    checkUsertype (users) {
      return users.indexOf(this.$store.state.user.usertype) !== -1
    },
    changeTheme () {
      let theme = this.$store.state.theme
      let hue = this.$store.state.hue

      switch (this.$store.state.theme) {
        case 'dark':
          theme = 'light'
          hue = 0x00ffff
          break
        case 'light':
          theme = 'dark'
          hue = 0x00ff00
          break
      }

      this.$store.state.theme = theme
      this.$store.state.hue = hue

      ChangeTheme.change(theme, hue)

      try {
        localStorage.setItem('theme', theme)
        localStorage.setItem('hue', hue)
      } catch (err) {}
    }
  },
  components: {
    Logo,
    LogoutBtn,
    SearchBar
  }
}
</script>

<style scoped>
.header-img {
  max-width: 200px;
  width: 100%;
}

.header-container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.header-links {
  flex: 1;
  display: flex;
  justify-content: flex-end;
}

.header-links > a {
  padding: 10px;
}

.header-user-menu {
  display: flex;
  flex-direction: column;
}

.header-user-menu {
  display: none;
}

.header-user-menu.show {
  display: block;
}
</style>
