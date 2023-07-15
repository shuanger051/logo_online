<template>
  <div>
    <div class="event-cancel-wrap">
      <div class="action-name">
        <span>跳转链接</span>
      </div>
      <div class="delete-event-button floatRight">
        <h-icon name="android-close icon-android-close" @on-click="deleteEvents" :size="16" />
      </div>
    </div>
    <div class="props-wrapper">
      <h-form :model="eventData.result.params" :rules="fromRuleCustom" ref="skipForm">
        <h-form-item 
          label=""
          prop="out_url"
          class="no-label">
          <h-input
            ref="inputRef"
            type="textarea"
            :maxlength="500"
            :rows="3"
            :canResize="false"
            placeholder="http://"
            :filterRE="/\s/g"
            v-model.trim="eventData.result.params.out_url"
            @on-change="onchange"
            @on-blur="onBlur"
          />
        </h-form-item>
      </h-form>
    </div>
  </div>
</template>

<script>
import emojiRegex from 'emoji-regex'

export default {
  name: 'SkipPanel',
  props: {
    eventData: {
      type: Object,
      default: () => {
      }
    },
    worksInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  data() {
    const validateOutUrl = (rule, value, callback) => {
      console.log(value)
      const emojiTest = emojiRegex()
      if (value === '') {
        callback()
      } else if (emojiTest.test(value)) {
        callback(new Error('请输入非表情内容'))
      } else {
        callback()
      }
    }
    return {
      fromRuleCustom: {
        out_url: [{ validator: validateOutUrl, trigger: 'change' }]
      }
    }
  },
  computed: {

  },
  methods: {
    deleteEvents() {
      this.$emit('deleteEvents')
    },
    onBlur(e) {
      this.$refs.skipForm.validate((valid) => {
        let url = ''
        if (valid) {
          url = e.target.value
        } else {
          url = ''
        }
        const selectedPage = this.$store.state.cms.pages.items.find(item => { return item.uuid == this.$store.state.cms.editState.selectedPage })
          const element = this.$store.state.cms.elements.items[this.$store.state.cms.editState.selectedPage].find(item => { return item.uuid == this.$store.state.cms.editState.selectedElement })
          const pageIndex = this.$store.state.cms.pages.items.findIndex(item => { return item.uuid == this.$store.state.cms.editState.selectedPage })
          this.$store.dispatch('cms/events/updateEvents', {
            uuid: this.eventData.uuid,
            result: {
              params: {
                pageIndex,
                worksInfo: { works_title: this.worksInfo.works_title, publish_date_time: this.worksInfo.publish_date_time, works_id: this.worksInfo.works_id },
                page: { uuid: selectedPage.uuid, name: selectedPage.name },
                element: { name: element.name, element_name: element.element_name },
                out_url: url
              }
            }
          })
      })
    }
  }
}

</script>
