<template>
  <div class="rating">
    <span v-for="n in 5" @click="rate(6 - n)" v-bind:class="{ selected: isSelected(value, 6 - n) }">☆</span>
  </div>
</template>

<script>
export default {
  name: 'star-rating-interactive',
  props: ['id', 'value'],
  methods: {
    isSelected (rating, star) {
      return rating >= star
    },
    rate (i) {
      this.$emit('rate', this.id, i)
    }
  }
}

// this.$emit('ratingReturn', this.value)
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
}

.rating > .selected:before,
.rating > .selected ~ span:before {
   content: "\2605";
   position: absolute;
   color: #dddd00;
   color: var(--p-color-1);
}

.rating > span:hover:before,
.rating > span:hover ~ span:before {
   content: "\2605";
   position: absolute;
   color: #ffff00;
   color: var(--nn-color-2);
   cursor: pointer;
}
</style>

