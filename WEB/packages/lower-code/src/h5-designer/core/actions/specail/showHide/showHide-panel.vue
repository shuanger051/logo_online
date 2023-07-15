<template>
  <div>
    <div class="event-cancel-wrap">
      <div class="action-name">
        <span>显示/隐藏</span>
      </div>
      <div class="delete-event-button floatRight">
        <h-icon name="android-close icon-android-close" @on-click="deleteEvents" :size="16">
        </h-icon>
      </div>
    </div>
    <div>
      <h-select multiple v-model="targetList" placeholder="选择目标元素" @on-change="onchange" @on-item-remove="removeItem">
        <h-option v-for="i in selectedPageElements" :key="i.uuid" :value="i.uuid" :label="i.element_name || i.name">
          {{i.element_name || i.name}} </h-option>
      </h-select>
    </div>
    <div v-for="(i, index) in eventList" :key="i.uuid" class="props-wrapper showHideWrap">
      <div class="init-status-wrap">
        <h-icon v-if="findElement(i.result.target).extra && findElement(i.result.target).extra.initStatus === 0"
          name="eye-disabled icon-eye-disabled" :size="14" @on-click="toggleInitStatus(i.result.target)">
        </h-icon>
        <h-icon v-else name="view icon-view" :size="14" @on-click="toggleInitStatus(i.result.target)">
        </h-icon>
      </div>
      <div class="name-wrap">
        <h-tooltip :content="findElement(i.result.target).element_name || findElement(i.result.target).name"
          placement="top">
          <span>{{findElement(i.result.target).element_name || findElement(i.result.target).name}}</span>
        </h-tooltip>
      </div>
      <div class="result-wrap">
        <h-select @on-change="changeShowHideStatus(i.uuid, $event)" size="small" :value="i.result.params.showStatus">
          <h-option value="1">显示</h-option>
          <h-option value="2">隐藏</h-option>
          <h-option value="3">切换</h-option>
        </h-select>
      </div>
      <div class="delay-time-wrap">
        <h-input-number size="small" :min="0" :max="10" :value="i.params.delay"
          @on-change="changeShowHideDealy(i.uuid, $event)">
          <span slot="append">s</span>
        </h-input-number>
      </div>
      <div class="delete-wrap">
        <h-icon name="android-close icon-android-close" @on-click="deleteShowHideEvents(i.uuid, index)" :size="14">
        </h-icon>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { find, difference } from 'lodash'

export default {
  name: 'ShowHidePanel',
  props: {
    eventList: {
      type: Array,
      default: () => []
    },
    trigger:{
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      targetList: []
    }
  },
  watch: {
    eventList: {
      handler(newValue, oldValue) {
        this.setTargetList()
      },
      deep: true
    }
  },
  computed: {
    ...mapGetters('cms/elements', ['selectedPageElements']),
    eventListTarget() {
      let arr = []
      for (let i = 0; i < this.eventList.length; i++) {
        arr.push(this.eventList[i].result.target)
      }
      return arr
    }
  },
  created() {
    this.setTargetList()
  },
  methods: {
    setTargetList() {
      this.targetList = this.eventList.map(e => {
        return e.result.target
      })

      // for (let i = 0; i < this.eventList.length; i++) {
      //   this.targetList.push(this.eventList[i].result.target)
      // }
    },
    findElement(uuid) {
      return find(this.selectedPageElements, { uuid })
    },
    // 切换组件初始显示隐藏状态
    toggleInitStatus(uuid) {
      let element = this.findElement(uuid)
      let extra = element.extra
      let newInitStatus = 0 // 1显示 0隐藏
      if ((extra && extra.initStatus === 1)) {
        newInitStatus = 0
      } else if (extra && extra.initStatus === 0) {
        newInitStatus = 1
      }
      this.$store.dispatch('cms/elements/updateElementExtra', {
        uuid,
        extra: {
          initStatus: newInitStatus
        }
      })
    },
    onchange(value) {
      // value判断是否在eventlist中，若没有则新增
      let addArr = difference(value, this.eventListTarget)
      for (let i = 0; i < addArr.length; i++) {
        this.$store.dispatch('cms/events/addEvents', {
          trigger: {
            value: this.trigger.trigger,
          },
          result: {
            value: 'showHide',
            target: addArr[i]
          }
        })
      }
    },
    findEvent(targetUuid) {
      return find(this.eventList, {
        result: {
          target: targetUuid
        }
      })
    },
    removeItem(item, index) {
      let eventUuid = this.findEvent(item.value).uuid
      // 根据result.target获取event的uuid
      this.deleteShowHideEvents(eventUuid, -1)
    },
    // 改变组件的显示隐藏切换状态
    changeShowHideStatus(eventUUID, $event) {
      this.updateEvent({
        uuid: eventUUID,
        result: {
          params: {
            showStatus: $event
          }
        }
      })
    },
    // 改变动作执行的延迟时间
    changeShowHideDealy(eventUUID, $event) {
      this.updateEvent({
        uuid: eventUUID,
        params: {
          delay: $event
        }
      })
    },
    updateEvent(event) {
      this.$store.dispatch('cms/events/updateEvents', {
        ...event
      })
    },
    deleteEvents() {
      this.$emit('deleteEvents')
    },
    deleteShowHideEvents(uuid, targetIndex) {
      this.$store.dispatch('cms/events/deleteEvents', uuid)
      if (targetIndex !== -1) {
        this.targetList.splice(targetIndex, 1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.showHideWrap {
  white-space: nowrap;
  display: flex;
  justify-content: space-between;
  .init-status-wrap {
    flex-grow: 0;
    width: 16px;
    cursor: pointer;
  }

  .name-wrap {
    flex-grow: 0;
    width: 60px;
  }

  .result-wrap {
  }

  .delay-time-wrap {
  }
  .delete-wrap {
    flex-grow: 0;
    width: 16px;
    cursor: pointer;
  }
}
</style>
