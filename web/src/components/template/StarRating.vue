<template>
  <div class="rating">
    <span v-for="n in 5" v-bind:class="{ selected: isSelected(value, 6 - n) }">☆</span>
  </div>
</template>

<script>
export default {
  name: 'star-rating',
  props: ['id', 'value'],
  methods: {
    isSelected (rating, star) {
      return rating >= star
    }
  }
}

</script>

<style scoped>

.rating {
  unicode-bidi: bidi-override;
  direction: rtl;
}

.rating > span {
  display: inline-block;
  position: relative;
  width: 1.1em;
  cursor: default;
}

.rating > .selected:before,
.rating > .selected ~ span:before {
   content: "\2605";
   position: absolute;
   color: #dddd00;
   color: var(--p-color-1);
}

</style>

