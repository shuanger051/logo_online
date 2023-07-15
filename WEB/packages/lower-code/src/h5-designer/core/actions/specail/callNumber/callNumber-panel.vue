<template>
  <div>
    <div class="event-cancel-wrap">
      <div class="action-name">
        <span>拨打电话</span>
      </div>
      <div class="delete-event-button floatRight">
        <h-icon name="android-close icon-android-close" @on-click="deleteEvents" :size="16" />
      </div>
    </div>
    <div class="props-wrapper">
      <h-form :model="form" ref="form" :errorFocus="true" label-position="top" class="cardForm">
        <h-form-item label="" prop='call_number' :rules="{
          validator: this.validatePhone,
          trigger: 'blur'
        }">
          <h-input placeholder="请输入电话号码" :maxlength="15" :filterRE="/[<>]/g" v-model="form.call_number" />
        </h-form-item>
      </h-form>
    </div>
  </div>
</template>

<script>

export default {
  name: 'CallNumberPanel',
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
    return {
      form: this.eventData.result.params
    }
  },
  computed: {

  },
  methods: {
    deleteEvents() {
      this.$emit('deleteEvents')
    },
    validatePhone(rule, value, callback) {
      if (!value) {
        return callback(new Error('请输入电话号码'))
      } else {
        this.$store.dispatch('cms/events/updateEvents', {
          uuid: this.eventData.uuid,
          result: {
            params: {
              call_number: form.call_number
            }
          }
        })
      }
    }
  }
}

</script>
