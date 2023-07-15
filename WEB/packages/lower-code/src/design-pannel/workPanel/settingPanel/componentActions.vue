<template>
  <div class="event-container right-panel-container">
    <collapse-wrap name="交互事件">
      <div>
        触发条件
      </div>
      <div class="props-wrapper">
        <h-select v-model="eventData.trigger" :disabled="curSelElement.name !== 'hs-cms-form-submit'">
          <h-option v-for="i in triggerList" :key="i.id" :value="i.id">{{i.name}}</h-option>
        </h-select>
      </div>
      <div>
        触发动作
      </div>
      <div class="action-result-container">
        <div class="action-select-wrap" :class="{active: seletedEventTypeArr.length === 0}"
          @click="handleToggleEvent('none')">
          <img :src="require('@Root/assets/images/action-nofeedback.svg')" style="margin-top:13px">
          <div>无反馈</div>
        </div>
        <div class="action-select-wrap" :class="{active: seletedEventTypeArr.indexOf(i.type) > -1}"
          v-for="i in actionList" :key="i.type" @click="handleToggleEvent(i.type)">
          <img :src="require('@Root/assets/images/action-' + i.type + '.svg')" style="margin-top:13px">
          <div>{{i.name}}</div>
        </div>
      </div>
      <div v-for="item in seletedEventTypeArr" :key="item">
        <download-panel v-if="item === 'download'" :eventData="selectedElementDownloadEvents"
          @deleteEvents="deleteEvents(item)" :worksInfo="worksInfo"></download-panel>
        <skip-panel v-if="item === 'skip'" :eventData="selectedElementSkipEvents" @deleteEvents="deleteEvents(item)"
          :worksInfo="worksInfo"></skip-panel>
        <skip-page-panel v-if="item === 'skipPage'" :eventData="selectedElementSkipPageEvents"
          @deleteEvents="deleteEvents(item)" :worksInfo="worksInfo"></skip-page-panel>
        <show-hide-panel v-if="item=== 'showHide'" :eventList="seletedElementShowHideEvents"
          @deleteEvents="deleteEvents(item)" :worksInfo="worksInfo" :trigger="eventData"></show-hide-panel>
        <call-number-panel v-if="item=== 'callNumber'" :eventData="selectedElementCallNumberEvents"
          @deleteEvents="deleteEvents(item)" :worksInfo="worksInfo"></call-number-panel>
        <wake-up-pop-panel v-if="item=== 'wakeUpPop'" :eventData="seletedElementWakeUpPopEvents"
          @deleteEvents="deleteEvents(item)" :worksInfo="worksInfo"></wake-up-pop-panel>
        <share-event-panel v-if="item=== 'shareEvent' && showShareSet" :eventData="seletedElementShareEvents"
          @deleteEvents="deleteEvents(item)" :worksInfo="worksInfo"></share-event-panel>
      </div>
    </collapse-wrap>
  </div>
</template>
<script>

import { find, filter } from 'lodash'
import { mapState, mapGetters } from 'vuex'
import CollapseWrap from '@Components/collapseWrap'
import DownloadPanel from '@h5Designer/core/actions/specail/downLoad/download-panel.vue'
import SkipPanel from '@h5Designer/core/actions/specail/skip/skip-panel.vue'
import SkipPagePanel from '@h5Designer/core/actions/specail/skipPage/skip-page-panel.vue'
import ShowHidePanel from '@h5Designer/core/actions/specail/showHide/showHide-panel.vue'
import CallNumberPanel from '@h5Designer/core/actions/specail/callNumber/callNumber-panel.vue'
import WakeUpPopPanel from '@h5Designer/core/actions/specail/wakeUpPop/wakeUpPop-panel.vue'
import ShareEventPanel from '@h5Designer/core/actions/specail/shareEvent/shareEvent-panel.vue'

export default {
  name: 'eActions',
  props: ['worksInfo'],
  components: {
    SkipPanel,
    ShowHidePanel,
    DownloadPanel,
    CollapseWrap,
    SkipPagePanel,
    CallNumberPanel,
    WakeUpPopPanel,
    ShareEventPanel
  },
  data() {
    return {
      showShareSet: window.CMS_CONFIG.SHOW_SHARE && window.CMS_CONFIG.SHOW_SHARE == 'true',
      seletedEventTypeArr: [],
      eventData: {
        trigger: 'click'
      },
      triggerList: [
        {
          id: 'click',
          name: '点击'
        },
         {
          id: 'afterClick',
          name: '表单提交成功后'
        }
      ],
      actionList: [
        // {
        //   type: 'none',
        //   name: '无反馈',
        //   icon: 'power icon-power'
        // },
        {
          type: 'skip',
          name: '跳转外部链接',
          icon: 'monitor icon-monitor'
        },
        {
          type: 'download',
          name: '跳转APP页面',
          icon: 't-b-download icon-t-b-download'
        },
        {
          type: 'showHide',
          name: '显示/隐藏',
          icon: 'view icon-view'
        },
        {
          type: 'skipPage',
          name: '跳转内部页面',
          icon: 'monitor icon-monitor'
        },
        {
          type: 'callNumber',
          name: '拨打电话',
          icon: 'monitor icon-monitor'
        },
        {
          type: 'wakeUpPop',
          name: '唤起弹窗',
          icon: 'monitor icon-monitor'
        },
        {
          type: 'shareEvent',
          name: '唤起分享',
          icon: 'monitor icon-monitor'
        }
      ],
      curSelElement:{}
    }
  },
  computed: {
    ...mapState('cms/pages', {
      pages: state => state.items
    }),
    ...mapState('cms/editState', [
      'selectedElement'
    ]),
    ...mapState('cms/editState', [
      'selectedPage'
    ]),
    ...mapState('cms/editState', [
      'selectedPage'
    ]),
    ...mapGetters('cms/events', [
      'selectedElementEvents'
    ]),
    ...mapGetters('cms/elements', [
      'getElementItems'
    ]),
    selectedElementSkipEvents() {
      return find(this.selectedElementEvents, {
        trigger: {
          value: this.selectedElementEvents.length>0?this.selectedElementEvents[0].trigger.value:'click',
          target: this.selectedElement
        },
        result: {
          value: 'skip'
        }
      })
    },
    selectedElementSkipPageEvents() {
      return find(this.selectedElementEvents, {
        trigger: {
          value: this.selectedElementEvents.length>0?this.selectedElementEvents[0].trigger.value:'click',
          target: this.selectedElement
        },
        result: {
          value: 'skipPage'
        }
      })
    },
    selectedElementDownloadEvents() {
      return find(this.selectedElementEvents, {
        trigger: {
          value: this.selectedElementEvents.length>0?this.selectedElementEvents[0].trigger.value:'click',
          target: this.selectedElement
        },
        result: {
          value: 'download'
        }
      })
    },
    selectedElementCallNumberEvents() {
      return find(this.selectedElementEvents, {
        trigger: {
          value: this.selectedElementEvents.length>0?this.selectedElementEvents[0].trigger.value:'click',
          target: this.selectedElement
        },
        result: {
          value: 'callNumber'
        }
      })
    },
    seletedElementShowHideEvents() {
      return filter(this.selectedElementEvents, {
        trigger: {
          value: this.selectedElementEvents.length>0?this.selectedElementEvents[0].trigger.value:'click',
          target: this.selectedElement
        },
        result: {
          value: 'showHide'
        }
      })
    },
    seletedElementWakeUpPopEvents() {
      return find(this.selectedElementEvents, {
        trigger: {
          value: this.selectedElementEvents.length>0?this.selectedElementEvents[0].trigger.value:'click',
          target: this.selectedElement
        },
        result: {
          value: 'wakeUpPop'
        }
      })
    },
    seletedElementShareEvents(){
      return find(this.selectedElementEvents, {
        trigger: {
          value: this.selectedElementEvents.length>0?this.selectedElementEvents[0].trigger.value:'click',
          target: this.selectedElement
        },
        result: {
          value: 'shareEvent'
        }
      })
    }
  },
  created() {
    this.setSeletedEventTypeArr()
    this.setCurElement()
    if (!this.showShareSet) {
      this.actionList.pop()
    }
  },
  watch: {
    selectedElement: {
      handler(newValue, oldValue) {
        this.setSeletedEventTypeArr()
        this.setCurElement()
      },
      deep: true
    },
    'eventData.trigger':{
      handler(val){
        if(this.selectedElementEvents.length > 0){
          this.$store.dispatch('cms/events/updateEvents', {
            uuid:this.selectedElementEvents[0].uuid,
            trigger: {
              value: val,
              target: this.selectedElementEvents[0].trigger.target
            },
          })
        }
      },
      deep: true
    }
  },
  componentWillReceiveProps(nextProps) {
  },
  methods: {
    setCurElement(){
      console.log('selectedElementEvents',this.selectedElementEvents)
      //获取当前选中的整个元素
      this.curSelElement = this.getElementItems[this.selectedPage].find(e => e.uuid == this.selectedElement)
      //
      if(this.curSelElement.name == 'hs-cms-form-submit'&& this.selectedElementEvents.length > 0){
        this.eventData.trigger = this.selectedElementEvents[0].trigger.value
      } else {
        this.eventData.trigger = "click"
      }
    },
    setSeletedEventTypeArr() {
      let selectedElementEvents = this.selectedElementEvents
      let arr = []
      for (let i = 0; i < selectedElementEvents.length; i++) {
        let value = selectedElementEvents[i].result.value
        if (arr.indexOf(value) === -1) {
          arr.push(value)
        }
      }
      this.seletedEventTypeArr = arr
    },
    handleToggleEvent(type) {
      if (type === 'none' || this.seletedEventTypeArr.includes(type)) {
        this.deleteAllEvent()
      } else {
        this.deleteAllEvent()
        if(type === 'showHide'){
          this.seletedEventTypeArr.push('showHide')
        } else {
          this.addEvent(this.eventData, type)
        }
      }
      // switch (type) {
      //   case 'none':
      //     this.deleteAllEvent()
      //     break
      //   case 'showHide':
      //     if (this.seletedEventTypeArr.includes(type)) {
      //       // this.deleteShowHideEvents()
      //       this.deleteAllEvent()
      //     } else {
      //       this.deleteAllEvent()
      //       this.seletedEventTypeArr.push('showHide')
      //     }
      //     break
      //   case 'download':
      //     if (this.seletedEventTypeArr.includes(type)) {
      //       this.deleteAllEvent()
      //     } else {
      //       this.deleteAllEvent()
      //       this.addEvent(this.eventData, type)
      //     }
      //     break
      //   case 'skip':
      //     if (this.seletedEventTypeArr.includes(type)) {
      //       this.deleteAllEvent()
      //     } else {
      //       this.deleteAllEvent()
      //       this.addEvent(this.eventData, type)
      //     }
      //     break
      //   case 'callNumber':
      //     if (this.seletedEventTypeArr.includes(type)) {
      //       this.deleteAllEvent()
      //     } else {
      //       this.deleteAllEvent()
      //       this.addEvent(this.eventData, type)
      //     }
      //     break
      //   case 'skipPage':
      //     if (this.seletedEventTypeArr.includes(type)) {
      //       // let uuidArr = []
      //       // for (let i = 0; i < this.selectedElementEvents.length; i++) {
      //       //   uuidArr.push(this.selectedElementEvents[i].uuid)
      //       // }
      //       // this.$store.dispatch('cms/events/deleteEvents', uuidArr)
      //       // this.seletedEventTypeArr = [] // 清空选中状态
      //       this.deleteAllEvent()
      //     } else {
      //       // this.$store.dispatch('cms/event/addEvents', type)
      //       // this.deleteShowHideEvents()
      //       // this.deleteEvents('download')
      //       // this.deleteEvents('callNumber')
      //       // this.deleteEvents('skip')
      //       // this.deleteOtherEvents(['showHide','download','callNumber','skip'])
      //       this.deleteAllEvent()
      //       this.addEvent(this.eventData, type)
      //       // this.$store.dispatch('cms/events/addEvents', {
      //       //   trigger: {
      //       //     value: this.eventData.trigger,
      //       //   },
      //       //   result: {
      //       //     value: type
      //       //   }
      //       // })
      //       // this.seletedEventTypeArr.push(type)
      //     }
      //     break
      // }
      // this.$store.dispatch('cms/events/toggleEvent', type)
    },
    //清除所有事件
    deleteAllEvent(){
      let uuidArr = []
      for (let i = 0; i < this.selectedElementEvents.length; i++) {
        uuidArr.push(this.selectedElementEvents[i].uuid)
      }
      this.$store.dispatch('cms/events/deleteEvents', uuidArr)
      this.seletedEventTypeArr = [] // 清空选中状态
    },
    //添加事件
    addEvent(eventData, type){
      this.$store.dispatch('cms/events/addEvents', {
        trigger: {
          value: eventData.trigger,
        },
        result: {
          value: type
        }
      })
      this.seletedEventTypeArr.push(type)
    },

    deleteEvents(type) {
      let uuidArr = []
      for (let i = 0; i < this.selectedElementEvents.length; i++) {
        if (this.selectedElementEvents[i].result.value === type) {
          uuidArr.push(this.selectedElementEvents[i].uuid)
        }
      }
      this.$store.dispatch('cms/events/deleteEvents', uuidArr)
      // 删除state
      this.seletedEventTypeArr.splice(this.seletedEventTypeArr.indexOf(type), 1)
    },
    // deleteShowHideEvents() {
    //   // 删除状态
    //   let uuidArr = []
    //   for (let i = 0; i < this.seletedElementShowHideEvents.length; i++) {
    //     uuidArr.push(this.seletedElementShowHideEvents[i].uuid)
    //   }
    //   this.$store.dispatch('cms/events/deleteEvents', uuidArr)
    //   // 删除state
    //   this.seletedEventTypeArr.splice(this.seletedEventTypeArr.indexOf('showHide'), 1)
    // }
  }

}
</script>
