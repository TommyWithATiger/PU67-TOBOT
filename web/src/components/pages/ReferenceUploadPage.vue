<template>
  <div class="page-content">
    <div class="reference_form">
      <div class="input-field">
        <label for="title"> Title: </label>
        <input type="text" v-model="title" @keydown.enter="submitReference" placeholder="Title" id="title"/>
      </div>
      
      <div class="input-field">
        <!-- Fix to textarea -->
        <label for="description"> Description: </label>
        <input type="text" v-model="description" @keydown.enter="submitReference" placeholder="Description" id="description"/>
      </div>

      <div class="input-field">
        <label for="link"> Link: </label>
        <input type="url" v-model="link" @keydown.enter="submitReference" placeholder="Link"/>
      </div>

      <div class="input-field">
        <label for="type"> Type: </label>
        <select v-model="type">
          <option value="Article">Article</option>
          <option value="Video">Video</option>
          <option value="Website">Website</option>
          <option value="Image">Image</option>
          <option value="Document">Document</option>
          <option value="Slide">Slide</option>
          <option value="Notes">Notes</option>
        </select>
      </div>  
    </div>
  </div>
</template>

<script>

export default {
  name: 'referenceupload',
  data () {
    return {
      link: '',
      title: '',
      description: '',
      type: 'Website'
    }
  },
  methods: {
    submitReference () {

    },
    createTag (id, title) {
      let tag = document.createElement('div')
      tag.classList.toggle('topic_tag', true)
      tag.textContent = title
      tag.setAttribute('tagID', id)
      let tagClose = document.createElement('div')
      tagClose.classList.toggle('topic_tag_close', true)
      tagClose.textContent = 'X'
      tagClose.onclick = function () {
        tag.parentNode.removeChild(tag)
      }
      tag.appendChild(tagClose)
      document.getElementById('topic_tags').appendChild(tag)
    },
    hasTag (id) {
      let child = document.getElementById('topic_tags').firstChild
      if (child === null) {
        return false
      }
      do {
        if (parseInt(child.getAttribute('tagID')) === id) {
          return true
        }
      } while ((child = child.nextSibling) !== null)
      return false
    }
  }
}

</script>

<style scoped>

.input-field label {
  width: 100px;
  display: inline-block;
}

</style>
