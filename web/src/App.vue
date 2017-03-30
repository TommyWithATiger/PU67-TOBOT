<template>
  <div id="app" :class="`app background-color-n2 color-nn1 ${theme}`">
    <TopBar></TopBar>
    <router-view></router-view>
  </div>
</template>

<script>
import TopBar from 'components/template/TopBar'
import { ChangeTheme } from 'common/ChangeTheme'

export default {
  name: 'app',
  created () {
    ChangeTheme.change()
  },
  computed: {
    theme () {
      return this.$store.state.theme
    }
  },
  components: {
    TopBar
  }
}
</script>

<style>
/*
Understanding our color system:

There are 3 types of colors:
 - p-colors = Primary colors
 - n-colors = Neutral colors
 - nn-colors = Negative Neutral colors

The numbers behind each defines how strong it is where 1 is the strongest.
*/
:root {
  --p-color-1: #00ffff;
  --p-color-2: #00efff;
  --p-color-3: #00dfff;
  --p-color-4: #00cfff;
  --p-color-5: #00bfff;
  --p-color-6: #00afff;
  --p-color-7: #009fff;
  --p-color-8: #008fff;
  --p-color-9: #007fff;

  --n-color-1: #000000;
  --n-color-2: #111111;
  --n-color-3: #222222;
  --n-color-4: #333333;
  --n-color-5: #444444;
  --n-color-6: #555555;
  --n-color-7: #666666;
  --n-color-8: #777777;

  --nn-color-1: #ffffff;
  --nn-color-2: #eeeeee;
  --nn-color-3: #dddddd;
  --nn-color-4: #cccccc;
  --nn-color-5: #bbbbbb;
  --nn-color-6: #aaaaaa;
  --nn-color-7: #999999;
  --nn-color-8: #888888;

  --box-shadow-1: 0 0 8px rgba(0, 0, 0, .4);
  --box-shadow-2: 0 0 8px rgba(0, 0, 0, .2);
  --box-shadow-3: 0 0 8px rgba(0, 0, 0, .1);
}

.background-color-p1 { background-color: #00ffff; background-color: var(--p-color-1); }
.background-color-p2 { background-color: #00efff; background-color: var(--p-color-2); }
.background-color-p3 { background-color: #00dfff; background-color: var(--p-color-3); }
.background-color-p4 { background-color: #00cfff; background-color: var(--p-color-4); }
.background-color-p5 { background-color: #00bfff; background-color: var(--p-color-5); }
.background-color-p6 { background-color: #00afff; background-color: var(--p-color-6); }
.background-color-p7 { background-color: #009fff; background-color: var(--p-color-7); }
.background-color-p8 { background-color: #008fff; background-color: var(--p-color-8); }
.background-color-p9 { background-color: #007fff; background-color: var(--p-color-9); }
.background-color-n1 { background-color: #ffffff; background-color: var(--n-color-1); }
.background-color-n2 { background-color: #eeeeee; background-color: var(--n-color-2); }
.background-color-n3 { background-color: #dddddd; background-color: var(--n-color-3); }
.background-color-n4 { background-color: #cccccc; background-color: var(--n-color-4); }
.background-color-n5 { background-color: #bbbbbb; background-color: var(--n-color-5); }
.background-color-n6 { background-color: #aaaaaa; background-color: var(--n-color-6); }
.background-color-n7 { background-color: #999999; background-color: var(--n-color-7); }
.background-color-n8 { background-color: #888888; background-color: var(--n-color-8); }
.background-color-nn1 { background-color: #000000; background-color: var(--nn-color-1); }
.background-color-nn2 { background-color: #111111; background-color: var(--nn-color-2); }
.background-color-nn3 { background-color: #222222; background-color: var(--nn-color-3); }
.background-color-nn4 { background-color: #333333; background-color: var(--nn-color-4); }
.background-color-nn5 { background-color: #444444; background-color: var(--nn-color-5); }
.background-color-nn6 { background-color: #555555; background-color: var(--nn-color-6); }
.background-color-nn7 { background-color: #666666; background-color: var(--nn-color-7); }
.background-color-nn8 { background-color: #777777; background-color: var(--nn-color-8); }

.color-p1 { color: #00ffff; color: var(--p-color-1); }
.color-p2 { color: #00efff; color: var(--p-color-2); }
.color-p3 { color: #00dfff; color: var(--p-color-3); }
.color-p4 { color: #00cfff; color: var(--p-color-4); }
.color-p5 { color: #00bfff; color: var(--p-color-5); }
.color-p6 { color: #00afff; color: var(--p-color-6); }
.color-p7 { color: #009fff; color: var(--p-color-7); }
.color-p8 { color: #008fff; color: var(--p-color-8); }
.color-p9 { color: #007fff; color: var(--p-color-9); }
.color-n1 { color: #ffffff; color: var(--n-color-1); }
.color-n2 { color: #eeeeee; color: var(--n-color-2); }
.color-n3 { color: #dddddd; color: var(--n-color-3); }
.color-n4 { color: #cccccc; color: var(--n-color-4); }
.color-n5 { color: #bbbbbb; color: var(--n-color-5); }
.color-n6 { color: #aaaaaa; color: var(--n-color-6); }
.color-n7 { color: #999999; color: var(--n-color-7); }
.color-n8 { color: #888888; color: var(--n-color-8); }
.color-nn1 { color: #000000; color: var(--nn-color-1); }
.color-nn2 { color: #111111; color: var(--nn-color-2); }
.color-nn3 { color: #222222; color: var(--nn-color-3); }
.color-nn4 { color: #333333; color: var(--nn-color-4); }
.color-nn5 { color: #444444; color: var(--nn-color-5); }
.color-nn6 { color: #555555; color: var(--nn-color-6); }
.color-nn7 { color: #666666; color: var(--nn-color-7); }
.color-nn8 { color: #777777; color: var(--nn-color-8); }

@import url('https://fonts.googleapis.com/css?family=Roboto');

.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: 'Roboto', sans-serif;
}

.page-content {
  padding: 16px;
  box-sizing: border-box;
  flex: 1;
}

.error {
  color: #a00;
}

.content-container,
.header-container,
.page-content {
  max-width: 960px;
  width: 100%;
  margin: auto;
}

.header-container {
  max-width: 1200px;
}

button,
input[type=submit],
input[type=reset] {
  background-color: #0df;
  background-color: var(--p-color-1);
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 3px;
  outline: none;
  box-shadow: 0 0 8px -2px #000;
  transition-property: background-color box-shadow;
  transition-duration: .2s;
}

button:hover,
input[type=submit]:hover,
input[type=reset]:hover {
  background-color: #0bd;
  background-color: var(--p-color-3);
  transition-duration: .05s;
}

button:active,
input[type=submit]:active,
input[type=reset]:active {
  box-shadow: none;
  transition-duration: .05s;
}

button[disabled],
input[type=submit][disabled],
input[type=reset][disabled] {
  background-color: #0bd;
  background-color: var(--n-color-4);
  color: #aaa;
  color: var(--nn-color-1);
}

input[type=text], input, textarea, select {
  background-color: #999;
  background-color: var(--n-color-3);
  color: #999;
  color: var(--nn-color-1);
  outline: none;
  border: none;
  border-radius: 4px;
  border: 8px solid #999;
  border-color: var(--n-color-3);
  box-sizing: border-box;
  margin: 4px 8px;
  transition: background-color .2s;
}

input[type=text]:focus, input:focus {
  background-color: #999;
  background-color: var(--n-color-4);
  transition-duration: .05s;
}

.link, .colored-link, a {
  -webkit-apperance: none;
  color: #00f;
  color: var(--nn-color-2);
  text-decoration: none;
}

.colored-link {
  color: var(--p-color-2);
}
</style>
