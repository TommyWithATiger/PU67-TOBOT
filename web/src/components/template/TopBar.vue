<template>
  <div class="header background-color-n1">
    <div class="header-container">
      <router-link to="/"><Logo class="header-img" /></router-link>
      <router-link to="/"><TextLogo class="header-logo" /></router-link>
      <div class="header-links" v-if="authenticated">
        <router-link v-for="link in links"
          v-if="checkUsertype(link.users)"
          exact
          :to="link.path">
          {{ link.name }}
        </router-link>
      </div>
      <SearchBar v-if="authenticated && !isSearchRoute" />
      <div class="user-menu-container">
        <button class="user-menu-btn"
          v-if="authenticated"
          @click="userMenu = !userMenu">
          {{ state.user.username }} 
          <svg viewBox="0 0 10 10" :class="`user-menu-icon ${userMenu ? 'open' : ''}`">
            <rect x="-7" y="7" width="5" height="5" transform="rotate(-45)" v-if="userMenu" />
            <rect x="0" y="0" width="5" height="5" transform="rotate(-45)" v-else />
          </svg>
        </button>
        <div :class="`user-menu ${userMenu ? 'show' : ''}`" v-if="authenticated">
          <router-link :to="getUserUrl">Profile</router-link>
          <a @click="changeTheme" href="#">Change theme</a>
          <LogoutBtn />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Logo from 'components/template/Logo'
import TextLogo from 'components/template/TextLogo'
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
          name: 'Home',
          path: '/',
          users: ['Admin', 'Teacher', 'Student']
        },
        {
          name: 'Subjects',
          path: '/subject',
          users: ['Admin', 'Teacher']
        },
        {
          name: 'Topics',
          path: '/topic',
          users: ['Admin', 'Teacher']
        },
        {
          name: 'Upload',
          path: '/upload',
          users: ['Admin', 'Teacher', 'Student']
        },
        {
          name: 'References',
          path: '/reference',
          users: ['Admin', 'Teacher', 'Student']
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

      switch (parseInt(this.$store.state.hue)) {
        case 0xf0f0f0:
          theme = 'light'
          hue = 0x0099ff
          break
        case 0x0099ff:
          theme = 'light'
          hue = 0x22cc33
          break
        case 0x22cc33:
          theme = 'light'
          hue = 0x6f6f6f
          break
        case 0x6f6f6f:
          theme = 'dark'
          hue = 0xff8800
          break
        case 0xff8800:
          theme = 'dark'
          hue = 0xcc3322
          break
        case 0xcc3322:
          theme = 'dark'
          hue = 0xf0f0f0
          break
        default:
          theme = 'dark'
          hue = 0xff8800
      }

      this.$store.state.theme = theme
      this.$store.state.hue = hue

      try {
        localStorage.setItem('theme', theme)
        localStorage.setItem('hue', hue)
      } catch (err) {}

      ChangeTheme.change(theme, hue)
    }
  },
  components: {
    Logo,
    TextLogo,
    LogoutBtn,
    SearchBar
  }
}
</script>

<style scoped>
.header {
  box-shadow: 0 0 8px rgba(0, 0, 0, .4);
  box-shadow: var(--box-shadow-1);
  padding: 12px 24px;
  box-sizing: border-box;
}

.header-img {
  max-width: 160px;
  width: 100%;
  height: inherit;
}

.header-logo {
  max-width: 160px;
  width: 100%;
  height: inherit;
}

.header-container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
}

.header-links {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  padding: 0 16px;
}

.header-links > a {
  padding: 10px;
}

.header-links > a.router-link-active,
.user-menu > a.router-link-active {
  color: var(--nn-color-6);
}

.user-menu-btn {
  margin: 4px 16px;
}

.user-menu-container {
  position: relative;
}

.user-menu > * {
  margin: 4px 0;
}

.user-menu {
  margin: 4px 16px 0 0;
  width: 140px;
  display: none;
  position: absolute;
  top: 100%;
  border-radius: 4px;
  right: 0;
  padding: 8px;
  background-color: #ccc;
  background-color: var(--n-color-1);
  flex-direction: column;
  box-shadow: 0 0 8px rgba(0, 0, 0, .2);
  box-shadow: var(--box-shadow-2);
}

.user-menu.show {
  display: flex;
}

.user-menu-icon {
  fill: #000;
  width: 10px;
  margin-top: 5px;
  margin-bottom: -5px;
}

.user-menu-icon.open {
  margin-top: -2px;
  margin-bottom: 2px;
}
</style>
