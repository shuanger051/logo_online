<template>
  <div class="action-panel-set right-panel-container">
    <h-form label-position="left" :rules="fromRuleCustom" :model="worksProxy" ref="pageFormCustom">
      <collapse-wrap v-if="showLoginSwitch" name="登录设置">
        <div>
          <h-form-item label="用户访问页面是否需要登录" style="margin-bottom: 8px">
            <el-switch
              v-model="worksProxy.need_login"
              active-color="#037df3"
              inactive-color="#c3cbd6"
              active-value="1"
              inactive-value="0">
            </el-switch>
            <!-- <h-switch size="small" :value="need_login" v-model="worksProxy.need_login" :true-value="1" :false-value="0"></h-switch> -->
          </h-form-item>
          <h-form-item label="校验方式" v-if="worksProxy.need_login == '1'">
            <h-simple-select v-model="worksProxy.works_login_user_type" :clearable="false">
              <h-select-block :data="sysParams['10019']"></h-select-block>
            </h-simple-select>
          </h-form-item>
        </div>
      </collapse-wrap>
      <collapse-wrap name="作品有效期设置">
        <div>
          <h-form-item label="开启有效期设置">
            <el-switch
              v-model="worksProxy.need_validate_date"
              active-color="#037df3"
              inactive-color="#c3cbd6"
              active-value="1"
              inactive-value="0">
            </el-switch>
            <!-- <h-switch size="small" v-model="worksProxy.need_validate_date" :true-value="1" :false-value="0"></h-switch> -->
          </h-form-item>
        </div>
        <template v-if="worksProxy.need_validate_date==1">
          <!-- <div style="margin-bottom: 6px">
            生效时间
          </div> -->
          <div>
            <h-form-item label="生效时间" prop="display_start_date">
              <DateTimePickerInt placement="bottom-end" :transfer="true" :start.sync="worksProxy.display_start_date" :end.sync="worksProxy.display_end_date" :date.sync="date_range_arr" placeholder="开始时间-结束时间">
              </DateTimePickerInt>
              <!-- <DatePickerInt placement="bottom-end" :transfer="true" :start.sync="worksProxy.display_start_date" :end.sync="worksProxy.display_end_date" :date.sync="date_range_arr" placeholder="开始时间-结束时间">
              </DatePickerInt> -->
            </h-form-item>
          </div>
        </template>
      </collapse-wrap>
      <collapse-wrap name="页面拦截提示" tips="仅在页面设置有效期时，拦截弹窗提示生效" v-if="worksProxy.need_validate_date==1">
        <!-- <div style="margin-bottom: 6px">
          提示标题
        </div> -->
        <div style="margin-bottom: 6px">
          <h-form-item label="提示标题" prop="hint_title">
            <h-input placeholder="请输入标题，20字以内" :maxlength="20" :filterRE="/[<>]/g" v-model="worksProxy.hint_title"></h-input>
          </h-form-item>
        </div>
        <!-- <div style="margin-bottom: 6px">
          提示正文
        </div> -->
        <div style="margin-bottom: 6px">
          <h-form-item label="提示正文" prop="hint_content">
            <h-input placeholder="请输入正文，150字以内" type="textarea" :filterRE="/[<>]/g" v-model="worksProxy.hint_content" :maxlength="150"></h-input>
          </h-form-item>
        </div>
      </collapse-wrap>
      <collapse-wrap name="分享设置" v-if="showShareSet" >
        <div style="margin-bottom: 6px">
          分享图片
        </div>
        <div>
          <img-select v-model="works.share_img_url" :size="5120" description="支持png、jpg格式，大小为100*100px，图片大小不超过5MB" :accept="['png', 'jpg', 'jpeg']" :isShare="true" name="分享图片"></img-select>
        </div>
        <div style="margin-bottom: 6px">
          分享标题
        </div>
        <div style="margin-bottom: 6px">
          <h-input placeholder="请输入分享标题，20字以内" :filterRE="/[<>]/g" v-model="worksProxy.share_title" :maxlength="20"></h-input>
        </div>
        <div style="margin-bottom: 6px">
          分享内容
        </div>
        <div style="margin-bottom: 6px">
          <h-input placeholder="请输入分享内容，150字以内" :filterRE="/[<>]/g" type="textarea" v-model="worksProxy.share_content" :maxlength="150"></h-input>
        </div>
         <div style="margin-bottom: 6px">
          分享链接
        </div>
        <div style="margin-bottom: 6px">
           <h-radio-group v-model="worksProxy.share_url_type" @on-change="setShowUrl">
            <h-radio label="page_url">
              <span>本作品链接</span>
            </h-radio>
            <h-radio label="custom_url">
              <span>自定义链接</span>
            </h-radio>
          </h-radio-group>
           <!-- <h-radio v-model="isShowUrl" @on-click="setShowUrl">自定义分享URL地址</h-radio> -->
        </div>
        <div style="margin-bottom: 6px" v-if="worksProxy.share_url_type === 'custom_url'">
          <h-input placeholder="请输入分享落地页链接" :filterRE="/[<>]/g" v-model="worksProxy.share_page_url" :maxlength="150"></h-input>
        </div>
      </collapse-wrap>
      <collapse-wrap name="作品标题">
        <h-input placeholder="请输入作品标题，20字以内" :filterRE="/[^\u4E00-\u9FA5A-Za-z0-9]+$/g" v-model="works_subtitle" @on-blur="onSubTitleChange" :maxlength="20"></h-input>
      </collapse-wrap>
    </h-form>
  </div>
</template>
<script>

import { workFormVlidate } from '@h5Designer/utils'

import { mapState } from 'vuex'
import { omit } from 'lodash'

import ImgSelect from '@Components/ImgSelect'
import CollapseWrap from '@Components/collapseWrap'
import DatePickerInt from '@Components/DatePickerInt'
import DateTimePickerInt from '@Components/DateTimePickerInt'
import store from '@Root/store/actState'

const validateData = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入有效期'))
  } else {
    callback()
  }
}
const validateTitle = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入提示标题'))
  } else {
    callback()
  }
}
const validateContent = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入提示正文'))
  } else {
    callback()
  }
}
export default {
  components: {
    DateTimePickerInt,
    DatePickerInt,
    ImgSelect,
    CollapseWrap
  },
  data() {
    return {
      showLoginSwitch: window.CMS_CONFIG.SHOW_LOGIN_SWITCH && window.CMS_CONFIG.SHOW_LOGIN_SWITCH == 'true',
      showShareSet: window.CMS_CONFIG.SHOW_SHARE && window.CMS_CONFIG.SHOW_SHARE == 'true',
      isShowUrl: false,
      works_subtitle: '',
      fromRuleCustom: {
        display_start_date: [{ required: true, validator: validateData, trigger: 'change' }],
        hint_title: [{ required: true, validator: validateTitle, trigger: 'blur' }],
        hint_content: [{ required: true, validator: validateContent, trigger: 'blur' }]
      }
    }
  },
  watch: {
    'worksProxy.need_validate_date': {
      handler(newVal) {
        if (parseInt(newVal) === 0) {
          this.$store.dispatch('cms/works/updateWorks', {
            display_start_date: null,
            display_end_date: null,
            hint_title: '',
            hint_content: ''
          })
          this.$store.dispatch('cms/works/clearPassValidate', {
            clearType: 'clearWorkVlidate'
          })
        } else {
          this.initVlidate()
        }
      },
      deep: true
    },
    'worksProxy.works_property': {
      handler(newVal) {
        // console.log(newVal)
        let works_property = newVal
        if (typeof newVal === 'string') {
          works_property = JSON.parse(newVal)
        }
        if (works_property.works_subtitle) {
          this.works_subtitle = works_property.works_subtitle
        }
      },
      deep: true
    }
  },
  computed: {
    ...mapState('cms/works', {
      works: state => state
    }),
    worksProxy() {
      return new Proxy(this.works, {
        get: (target, name) => {
          // if (this.works['share_url_type'] === ' ' || !this.works['share_url_type']) {
          //   this.works['share_url_type'] = 'page_url'
          //   this.works['share_page_url'] = ''
          //   // this.works['share_page_url'] = process.env.NODE_ENV === 'development' ? `${window.location.origin}/cms-h5.html?preview=true` : `${window.location.origin}/cms-h5/index.html?preview=true`
          // }
          return this.works[name] && this.works[name] !== ' ' ? this.works[name] : ''
        },
        set: (target, name, value) => {
          this.updateWorks(name, value)
          return true
        }
      })
    },
    date_range_arr: {
      get: function () {
        return [this.works.display_start_date, this.works.display_end_date]
      },
      set: function (newValue) {
        this.$store.dispatch('cms/works/updateWorks', {
          display_start_date: newValue[0],
          display_end_date: newValue[1]
        })
      }
    },
    sysParams() {
      return store.state.sysParams
    }
  },
  mounted() {
    // console.log(this.worksProxy)
    let works_property = this.worksProxy.works_property
    if (typeof works_property === 'string') {
      works_property = JSON.parse(works_property)
    }
    if (works_property.works_subtitle) {
      this.works_subtitle = works_property.works_subtitle
    }
    if (this.worksProxy.need_validate_date === '1') {
      this.initVlidate()
    }
  },
  methods: {
    onSubTitleChange() {
      this.updateWorks('works_property', {
        works_subtitle: this.works_subtitle
      })
    },
    updateWorks(key, value) {
      this.$store.dispatch('cms/works/updateWorks', {
        [key]: value
      })
    },
    initVlidate() {
      this.$store.dispatch('cms/works/updatePassValidate', {
        updateType: 'work',
        vlidateFunc: this.validateForm
      })
    },
    validateForm() {
      const vlidateList = [
        {
          key: 'work-display_start_date',
          lable: '作品有效期',
          value: this.worksProxy.display_start_date,
          validateFunc: validateData
        }, {
          key: 'work-hint_title',
          lable: '提示标题',
          value: this.worksProxy.hint_title,
          validateFunc: validateTitle
        }, {
          key: 'work-hint_content',
          lable: '提示正文',
          value: this.worksProxy.hint_content,
          validateFunc: validateContent
        }
      ]
      // 如果为当前属性栏，显示表单错误提示
      if (this.$refs.pageFormCustom) {
        this.$refs.pageFormCustom.validate()
      }
      // 调用无模版的form表单验证方法, 验证内容
      return workFormVlidate('work', this.$store, { vlidateList: vlidateList })
    },
    setShowUrl() {
      this.worksProxy.share_page_url = ''
      // if(this.worksProxy.share_url_type === 'custom_url'){
      //   this.worksProxy.share_page_url = ''
      // } else {
      // this.worksProxy.share_page_url = process.env.NODE_ENV === 'development' ? `${window.location.origin}/cms-h5.html?preview=true` : `${window.location.origin}/cms-h5/index.html?preview=true`
      // }
      // this.isShowUrl = !this.isShowUrl
      // if(!this.isShowUrl){
      //   this.worksProxy.share_page_url = ""
      // }
    }
  }
}
</script>
<style scoped lang="scss">

</style>
