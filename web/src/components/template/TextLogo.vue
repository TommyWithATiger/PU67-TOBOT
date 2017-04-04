<template>
  <svg xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns="http://www.w3.org/2000/svg" height="50.292mm" width="240mm" version="1.1" xmlns:cc="http://creativecommons.org/ns#" xmlns:dc="http://purl.org/dc/elements/1.1/" viewBox="0 0 239.99998 50.292324">
 <g class="logo-group" transform="translate(-104.84 790.23)">
  <g class="logo-group" fill="#917c6f" transform="translate(93.895 -898.09)">
   <g class="logo-group" fill="#333">
    <g class="logo-group" transform="matrix(1.25 0 0 1 -20.305 -39.144)">
     <rect class="logo-element" height="40" width="12.8" y="157" x="38.6" stroke-width=".27456"/>
     <rect class="logo-element" transform="rotate(90)" height="40" width="16" y="-65" x="147" stroke-width=".30697"/>
    </g>
    <circle class="logo-element" cx="85.945" cy="132.86" r="25" stroke-width=".25997"/>
    <g class="logo-group" transform="translate(15.776 11.148)">
     <circle class="logo-element" stroke-width=".25997" cy="134.5" cx="119.09" r="12.5"/>
     <circle class="logo-element" stroke-width=".25997" cy="109.5" cx="119.09" r="12.5"/>
     <rect class="logo-element" height="50" width="20" y="97" x="100" stroke-width=".19282"/>
    </g>
    <circle class="logo-element" cx="175.95" cy="132.86" r="25" stroke-width=".25997"/>
    <g class="logo-group" transform="matrix(1.25 0 0 1 169.7 -39.144)">
     <rect class="logo-element" height="40" width="12.8" y="157" x="38.6" stroke-width=".27456"/>
     <rect class="logo-element" transform="rotate(90)" height="40" width="16" y="-65" x="147" stroke-width=".30697"/>
    </g>
   </g>
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
.logo-group {
  stroke: #09f;
  stroke: var(--p-color-3);
}

.logo-element {
  fill: #09f;
  fill: var(--p-color-3);
}

.logo-square {
  fill: none;
  stroke-width: 8;
}
</style>
