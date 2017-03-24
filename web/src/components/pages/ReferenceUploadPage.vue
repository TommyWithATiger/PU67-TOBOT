<template>
  <div class="page-content">
    <h2>Add reference</h2>
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
      <input class="submit_button" type="submit" v-on:click="submitReference"/>
      <div class="error"> 
        {{ error }}
      </div>
    </div>
    <div class="reference_form_tags">
      <div class="topic_list">
        <div>
          Tags: 
        </div>
        <div class="tags" id="topic_tags">

        </div>
      </div>

      <div class="add_tag_container" id="add_tag_container">
        <div class="add_tag_input">
          <input type="text" id="tag_input" v-on:keyup="searchTag" placeholder="Search for tag">
          <div class="tag_search_results" id="tag_search_result">

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from 'api'

export default {
  name: 'referenceupload',
  data () {
    return {
      tags: [],
      link: '',
      title: '',
      description: '',
      type: 'Website',
      error: ''
    }
  },
  created () {
    api.getTopics(this, (data) => {
      this.tags = data.topics
    }, () => {
      this.tags = []
    })
  },
  methods: {
    submitReference () {
      if (this.title === '') {
        this.error = 'Title must be set'
        return
      }
      if (this.description === '') {
        this.error = 'Description must be set'
        return
      }
      if (this.link === '') {
        this.error = 'Link must be set'
        return
      }
      let tags = document.getElementsByClassName('topic_tag')
      if (tags.length === 0) {
        this.error = 'The reference must have at least one tag'
        return
      }

      let tagIDs = []
      for (let tagIndex = 0; tagIndex < tags.length; tagIndex++) {
        tagIDs[tagIndex] = parseInt(tags[tagIndex].getAttribute('tagID'))
      }

      api.addReference(this, {
        title: this.title,
        description: this.description,
        link: this.link,
        type: this.type,
        tags: tagIDs
      }, (data) => {
        this.$router.push('reference/' + data.id)
      }, () => {
        this.error = 'Link is malformed or site cannot accessed'
      })
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
    },
    searchTag () {
      let search = document.getElementById('tag_input').value
      let resultBox = document.getElementById('tag_search_result')
      let child = resultBox.firstChild
      while ((child = resultBox.firstChild) != null) {
        resultBox.removeChild(child)
      }
      if (search === '') {
        return
      }
      for (let tagIndex = 0; tagIndex < this.tags.length; tagIndex++) {
        if (this.tags[tagIndex].title.toLowerCase().includes(search.toLowerCase()) && !this.hasTag(this.tags[tagIndex].id)) {
          let tag = document.createElement('div')
          let context = this
          tag.onclick = function () {
            if (!context.hasTag(context.tags[tagIndex].id)) {
              context.createTag(context.tags[tagIndex].id, context.tags[tagIndex].title)
            }
            tag.parentNode.removeChild(tag)
          }
          tag.classList.toggle('tag_search_result', true)
          tag.textContent = this.tags[tagIndex].title
          resultBox.appendChild(tag)
        }
      }
    }
  }
}

</script>

<style scoped>

.reference_form{
  display: inline-block;
  width: 400px;
  min-height: 200px;
  vertical-align: top;
}

.reference_form_tags {
  display: inline-block;
  width: 500px;
  min-height: 200px;
  vertical-align: top;
}

.submit_button {
  margin-top: 30px;
}

.tags {
  width: 450px;
}

.topic_list > div:first-child {
  vertical-align: top
}

.error {
  color: #ff0000;
  margin-top: 10px;
}

.input-field label {
  width: 100px;
  display: inline-block;
}

.topic_list > div {
  display: inline-block;
  margin-top: 10px;
  margin-bottom: 20px;
}

.add_tag_container {
  margin-left: -10px;
}

</style>
