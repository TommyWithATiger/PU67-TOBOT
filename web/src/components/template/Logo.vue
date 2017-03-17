<template>
  <svg width="360" height="144" viewBox="0 0 360 144" :class="color">
    <g class="logo-group" transform="translate(-30,685)">
      <rect class="logo-square" rx="9.715169" ry="9.715169" y="-673" x="110" height="120" width="200" />
      <g class="logo-element" transform="matrix(0,-1.25,1,0,-87,-559.75)">
        <rect y="157" x="38.599998" height="40" width="12.8" />
        <rect transform="rotate(90)" y="-65" x="147" height="40" width="16" />
      </g>
      <circle class="logo-element" cx="155" cy="-616" r="25" />
      <g class="logo-element" transform="rotate(90,523.29374,-191.29374)">
        <circle r="12.5" cy="134.5" cx="119.08749" />
        <circle r="12.5" cy="109.50001" cx="119.08749" />
        <rect y="97" x="100" height="50" width="20" />
      </g>
      <circle class="logo-element" cx="265" cy="-616" r="25" />
      <g class="logo-element" transform="matrix(0,1.25,-1,0,507,-672.25)">
        <rect class="logo-element" y="157" x="38.599998" height="40" width="12.8" />
        <rect class="logo-element" transform="rotate(90)" y="-65" x="147" height="40" width="16" />
      </g>
    </g>
  </svg>
</template>

<script>
export default {
  name: 'logo',
  computed: {
    color () {
      return this.$store.state.theme
    }
  }
}
</script>

<style scoped>
.logo-group { stroke: #09f; }
.dark .logo-group { stroke: #0df; }

.logo-element { fill: #09f; }
.dark .logo-element { fill: #0df; }

.logo-square {
  fill: none;
  stroke-width: 8;
}
</style>
