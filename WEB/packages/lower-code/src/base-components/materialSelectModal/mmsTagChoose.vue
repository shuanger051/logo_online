<template>
  <div class="mms-tag-choose" v-close>
    <div class="tag" @click='showDropDown'>
      {{displayTagName}}
      <i class="iconfont icon-unfold" :class="[{'icon-selected': isShow}]"></i>
    </div>
    <transition name="fade">
      <div class="dropDown" v-show="isShow" >
        <h-spin fix v-show="loading"></h-spin>
        <div class="tag-list">
          <div class="tag-list-head">
            <div class="list-head-left">
              <span style="fontWeight:700">待选</span><span>({{allTagNum}})</span>
            </div>
            <div class="list-head-right">
              <span style="fontWeight:700">已选</span><span>({{hasChooseTagList.length}})</span>
            </div>
          </div>
          <div class="tag-list-main">
            <div class="list-main-middle">
              <div class="main-middle-head">
                <div class="search-input">
                  <h-input placeholder="请输入搜索内容" v-model="tagSearchText" :maxlength="60" :byteNum="3" :lengthByByte="true" :filterRE="/[<>]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/ig" @on-enter="tagSearch(tagSearchText)"></h-input>
                  <h-icon name="search" color="#999" class="search-icon" @on-click="tagSearch(tagSearchText)"></h-icon>
                </div>
              </div>
              <div class="main-middle-bottom">
                <virtual-list style="overflow-y: auto;"
                              :style="hasDataFlag?'height: 0px;':'height:100%'"
                              :keeps="8"
                              :data-key="'id'"
                              :data-sources="allTagListCache"
                              :data-component="virtualPan"
                              ref="virtualLc"
                              :extra-props="{hasChooseTagList:hasChooseTagList,
                            chooseBtn:chooseBtn}"
                />
                <p v-show="hasDataFlag" class="no-data">未搜索到匹配项......</p>
              </div>
            </div>
            <div class="list-main-right">
              <div class="main-right-head" v-if="systemConfigInfo.custom_tag">
                <h-button type="ghost" @click="diyTagFlag=true">添加自定义标签</h-button>
                <div v-show="diyTagFlag" style="position: relative; z-index:9999">
                  <div class="diyTagDialogArrow"></div>
                  <div class="diyTagDialog">
                    <h-select v-model.trim="diyTagClass" placeholder="请选择标签分类">
                      <h-option
                        v-for="item in tagClassList"
                        :value="item.class_id"
                        :key="item.class_id"
                      >{{ item.class_name }}</h-option
                      >
                    </h-select>
                    <h-input v-model.trim="diyTag" :maxlength="60" :byteNum="3" :lengthByByte="true" :filterRE="/[<>]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/ig" placeholder="输入自定义标签" style="margin-top: 8px"></h-input>
                    <div style="display: flex; justify-content: flex-end; margin-top: 12px">
                      <h-button type="ghost" style="marginRight:8px;cursor:pointer" @click="closeDiyTag">取消</h-button>
                      <h-button type="primary" style="cursor:pointer" @click="addDiyTag">确定</h-button>
                    </div>
                  </div>
                </div>
                <!-- <div v-show="diyTagFlag">
                  <h-icon name="t-b-return" size="12" color="#999" style="marginRight:8px;cursor:pointer" @on-click="closeDiyTag"></h-icon>
                  <h-icon name="document-text" size="12" color="#999" style="cursor:pointer" @on-click="addDiyTag"></h-icon>
                </div> -->
              </div>
              <ul class="main-right-bottom">
                <li v-for="(item,index) in hasChooseTagList" :key="index" :title="item.tagName" class="has-choose-tag">{{item.tagName}}
                  <h-icon name="android-close" color="#555555" class="close" @on-click="deletTag(item,index)"></h-icon>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Api from './api/apis.js'
import virtualList from 'vue-virtual-scroll-list'
import virtualPan from './materialVirtual.vue'
export default {
  name: 'mmsTagChoose',
  components: {virtualList},
  props: {
    systemConfigInfo: {
      required: true,
      type: Object,
      default() {
        return {}
      }
    },
    sendChooseTagList: {
      type: Array
    },
    cmmGSV: {
      required: true,
      type: String,
      default() {
        return ''
      }
    }
  },
  data() {
    return {
      isShow: false,
      loading: false,
      allTagNum: '',
      allTagList: [],
      // 已选标签列表
      hasChooseTagList: this.sendChooseTagList,
      allTagListCache: [],
      tagSearchText: '',
      currentFirstGroup: [], // 折叠面板
      hasDataFlag: false,
      diyTagFlag: false, // 自定义标签
      diyTag: '', // 新增自定义标签
      diyTagClass: '', // 新增自定义标签所属的自定义类别
      virtualPan: virtualPan,
      tagClassList: [],
      exitTagList: []
    }
  },
  directives: {
    close: {
      inserted(el, binding, vnode) {
        window.onclick = function (e) {
          if (e.target.className.includes('h-icon-right') || e.target.className.includes('h-checkbtn-icon') || e.target.className.includes('h-icon-android-close')) {
            return false
          }
          if (!el.contains(e.target)) {
            vnode.context.isShow = false
          }
        }
      }
    }
  },
  computed: {
    material_admin_flag() {
      return this.$store.state.cmm.cmmCommon.licenceInfo.material_admin_flag
    },
    displayTagName() {
      let arr = []
      this.hasChooseTagList.forEach((item) => {
        arr.push(item.tagName)
      })
      this.$emit('update:sendChooseTagList', this.hasChooseTagList)
      return arr.join(', ')
    }
  },
  watch: {
    tagSearchText: {
      handler(val) {
        this.tagSearch(val)
      }
    }
  },
  mounted() {

  },
  methods: {
    chooseBtn(item) {
      let has = this.hasChooseTagList.find(i => i.id === item.id)
      if (has) {
        this.hasChooseTagList = this.hasChooseTagList.filter(subItem => item.id !== subItem.id)
      } else {
        if (this.hasChooseTagList.length >= 15) {
          this.$hMessage.info('最多添加15个标签')
          return
        }
        this.hasChooseTagList = [...this.hasChooseTagList, item]
      }
    },
    async showDropDown() {
      if (!this.isShow) {
        this.isShow = true
        await this.getTagList()
        await this.getCustomTagClass()
        this.initDropDown()
      } else {
        this.isShow = false
      }
    },
    initDropDown() {
      this.hasDataFlag = false
      this.diyTagFlag = false
      this.diyTag = ''
      this.diyTagClass = ''
      this.tagSearchText = ''
      this.tagSearch('')
    },
    async getTagList() {
      this.loading = true
      return Api.getTagList(this.cmmGSV + '/getTagAndClass').then(res => {
        this.loading = false
        // 标签数据拼接
        this.allTagList = []
        let secondClass = []
        this.exitTagList = res.data.tag_list
        res.data.class_list.forEach(item => {
          if (item.tree_level === 1) {
            let obj = {
              id: item.class_id,
              tagName: item.class_name,
              children: [],
              show: true
            }
            this.allTagList.push(obj)
          } else {
            let obj = {
              id: item.class_id,
              tagName: item.class_name,
              children: [],
              parent_code: item.parent_code,
              show: true
            }
            secondClass.push(obj)
          }
        })
        this.allTagNum = res.data.tag_list.length
        res.data.tag_list.forEach(item => {
          let obj = {
            id: item.tag_id,
            tagName: item.tag_name
          }
          let first = this.allTagList.find(val => val.id === item.class_id)
          if (first) {
            first.children.push(obj)
          } else {
            let second = secondClass.find(val => val.id === item.class_id)
            second && second.children.push(obj)
          }
        })
        secondClass.forEach(item => {
          let first = this.allTagList.find(val => val.id === item.parent_code)
          first && first.children.push(item)
        })
      })
    },
    tagSearch(val) {
      let text = val
      if (text) {
        this.allTagListCache = []
        this.allTagList.forEach(value => {
          // value一级分类
          value.children.forEach(val => {
            if (val.children) {
              // val二级分类
              val.children.forEach(v => {
                // v标签
                let flag = v.tagName.indexOf(text)
                if (flag !== -1) {
                  let first = this.allTagListCache.find(item => item.id === value.id)
                  if (first) {
                    let second = first.children.find(item => item.id === val.id)
                    if (second) {
                      second.children.push(v)
                    } else {
                      let obj = {
                        id: val.id,
                        tagName: val.tagName,
                        children: []
                      }
                      obj.children.push(v)
                      first.children.push(obj)
                    }
                  } else {
                    let obj = {
                      id: value.id,
                      tagName: value.tagName,
                      children: [],
                      show: true
                    }
                    let o = {
                      id: val.id,
                      tagName: val.tagName,
                      children: []
                    }
                    this.allTagListCache.push(obj)
                    obj.children.push(o)
                    o.children.push(v)
                  }
                }
              })
            } else {
              let flag = val.tagName.indexOf(text)
              if (flag !== -1) {
                let first = this.allTagListCache.find(item => item.id === value.id)
                if (first) {
                  first.children.push(val)
                } else {
                  let obj = {
                    id: value.id,
                    tagName: value.tagName,
                    children: [],
                    show: true
                  }
                  obj.children.push(val)
                  this.allTagListCache.push(obj)
                }
              }
            }
          })
        })
      } else {
        this.allTagListCache = this.allTagList
      }
      if (this.allTagListCache.length > 0) {
        this.hasDataFlag = false
      } else {
        this.hasDataFlag = true
      }
    },
    closeDiyTag() {
      this.diyTag = ''
      this.diyTagClass = ''
      this.diyTagFlag = false
    },
    deletTag(item, index) {
      this.hasChooseTagList = this.hasChooseTagList.filter(n => n != item)
    },
    addDiyTag() {
      if (!this.diyTagClass || this.diyTagClass == '') {
        this.$hMessage.warning('请选择自定义标签所属分类')
      } else {
        if (!this.diyTag || this.diyTag == '') {
          this.$hMessage.warning('自定义标签名称不能为空')
        } else {
          const tagInClass = this.getTagListByClassId(this.diyTagClass)
          // 先取到分类下的有没有同名的标签，有就是选择，没有就是新增
          if (tagInClass.length) {
            let tagIndex = tagInClass.findIndex(item => { return item.tagName == this.diyTag })
            if (tagIndex > -1) {
              // 找到了重名标签，得先判断当前选中状态，选中则直接关闭弹窗，没选中则选中
              let flag = this.hasChooseTagList.find(item => { return item.id == tagInClass[tagIndex].id })
              if (!flag) {
                this.chooseBtn(tagInClass[tagIndex])
              }
            } else {
              let obj = {
                tagName: this.diyTag,
                tag_id: '',
                class_id: this.diyTagClass
              }
              this.hasChooseTagList = [...this.hasChooseTagList, obj]
            }
          } else {
            let obj = {
              tagName: this.diyTag,
              tag_id: '',
              class_id: this.diyTagClass
            }
            this.hasChooseTagList = [...this.hasChooseTagList, obj]
          }
          this.closeDiyTag()
        }
      }
    },
    // 根据标签分类id获取标签分类下的所有标签
    getTagListByClassId(classId) {
      console.log(this.exitTagList)
      let arr = []
      this.exitTagList.forEach(item => {
        if (item.class_id == classId) {
          let obj = {
            id: item.tag_id,
            tagName: item.tag_name
          }
          arr.push(obj)
        }
      })
      return arr
    },
    getCustomTagClass() {
      return Api.getCustomTagClass(this.cmmGSV + '/getCustomTagClass').then(res => {
        let data = JSON.parse(JSON.stringify(res.data.rows))
        // data.forEach(item => {
        //   item.label = item.class_name
        //   item.value = item.class_id
        // })
        this.tagClassList = data
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.mms-tag-choose {
  .tag {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    position: relative;
    height: 28px;
    border-radius: 2px;
    border: 1px solid #d7dde4;
    cursor: pointer;
    padding: 0 24px 0 8px;
    &:hover {
      border-color: #3597f5;
    }
    i {
      right: 8px;
      position: absolute;
      font-size: 12px;
      color: #9ea7b4;
      transition: all 0.2s ease-in-out;
    }
    .icon-selected {
      transform: rotate(180deg);
    }
  }
  .dropDown {
    width: 100%;
    height: 316px;
    position: absolute;
    background: #fff;
    top: 30px;
    left: 0px;
    z-index: 1000;
    .tag-list {
      border: 1px solid #d9d9d9;
      height: 316px;
      .tag-list-head {
        height: 32px;
        border-bottom: 1px solid #d9d9d9;
        .list-head-left {
          float: left;
          width: 59%;
          height: 32px;
          line-height: 32px;
          text-indent: 8px;
        }
        .list-head-right {
          float: right;
          width: 41%;
          height: 32px;
          line-height: 32px;
          text-indent: 8px;
          border-left: 1px solid #d9d9d9;
        }
      }
      .tag-list-main {
        height: 283px;
        .list-main-left {
          float: left;
          width: 122px;
          height: 283px;
          overflow: auto;
          .list-main-left-menu {
            .menu-item {
              height: 28px;
              line-height: 28px;
              cursor: pointer;
              .menu-item-mao {
                position: relative;
                color: #333333;
                display: block;
                text-indent: 4px;
                width: 100%;
                height: 100%;
                .menu-item-mao-name {
                  display: inline-block;
                  width: 100px;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                  word-break: break-all;
                }
                .menu-icon {
                  position: absolute;
                  right: 2px;
                }
              }
            }
          }
        }
        .list-main-middle {
          float: left;
          width: 59%;
          height: 283px;
          .main-middle-head {
            border-bottom: 1px solid #d9d9d9;
            height: 44px;
            padding: 8px;
            .search-input {
              width: 100%;
              position: relative;
              .search-icon {
                position: absolute;
                right: 7px;
                top: 3px;
                cursor: pointer;
              }
            }
          }
          .main-middle-bottom {
            height: 238px;
            overflow: hidden;
            .tag-second-group {
              color: #333;
              margin-bottom: 8px;
            }
            .no-data {
              width: 100%;
              text-align: center;
              font-size: 20px;
              font-weight: 700;
              margin-top: 100px;
              color: #999;
            }
          }
        }
        .list-main-right {
          float: right;
          width: 41%;
          border-left: 1px solid #d9d9d9;
          height: 283px;
          .main-right-head {
            border-bottom: 1px solid #d9d9d9;
            height: 44px;
            padding: 7px 8px 8px;
            .diyTagDialogArrow {
              width:0;
              height:0;
              border-width:0 5px 5px;
              border-style:solid;
              border-color:transparent transparent #fff;
              margin:0 auto;
              position:relative;
              margin-left: 48px;
            }
            .diyTagDialog {
              background: #FFFFFF;
              box-shadow: 1px 0px 6px 0px rgba(51, 51, 51, 0.15);
              padding: 8px;
              margin-left: -65px;
            }
          }
          .main-right-bottom {
            height: 239px;
            overflow: auto;
            .has-choose-tag {
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              padding: 0 22px 0 8px;
              height: 30px;
              line-height: 30px;
              color: #555555;
              position: relative;
              .close {
                display: none;
                position: absolute;
                right: 12px;
                top: 2px;
                cursor: pointer;
              }
            }
            .has-choose-tag:hover {
              background: #FFF5F5;
              .close {
                display: inherit;
              }
            }
          }
        }
      }
    }
    .footer {
      position: absolute;
      bottom: 20px;
      right: 32px;
    }
  }
}
</style>
