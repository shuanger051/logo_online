<template>
  <div class="CMS-OMS-WEB material-select">
    <slot>
      <div v-if="!multiple" class="single-select">
        <div class="select-area" @click="showModal">
          <h-icon name="android-add" size="20"></h-icon>
          <div v-if="Object.keys(selectedRow[0]).length !== 0" class="preview">
            <img :src="getImg(selectedRow[0])" alt="" @error="loadErrorImg">
            <a><h-icon name="delete" size="20" class="delete-btn" @on-click.stop="deleteItem"></h-icon></a>
          </div>
        </div>
      </div>
      <div v-else class="multiple-select">
        <template v-if="selectedRow.length !== 0" >
          <div class="multiple-preview" v-for="(item, index) in selectedRow" :key="item.material_ext_id">
            <img :src="getImg(item)" alt="" @error="loadErrorImg">
            <a><h-icon name="delete" size="20" class="delete-btn" @on-click.stop="deleteItem(index)"></h-icon></a>
          </div>
        </template>
        <div class="multiple-select-area" @click="showModal">
          <h-icon name="android-add" size="20"></h-icon>
        </div>
      </div>
    </slot>
    <div v-if="isShow">
      <h-msg-box class="set-modal-style material-select-modal" v-model="isShow" title="素材库" :mask-closable="false" width="1100" height="580" footerHide @on-close="cancel">
        <h-row style="height:100%">
        <h-col span="5" class="body-left">
          <div class="search-input">
            <h-input icon="search" placeholder="输入关键字进行过滤" v-model.trim="filterText" :maxlength="60" :byteNum="3" :lengthByByte="true" :filterRE="/[<>]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/ig"></h-input>
          </div>
          <div class="tree-box">
            <h-tree :data="treeData" isAlwaysSelect :show-checkbox="false" :filter-node-method="filterNode" ref="tree" @on-select-change="treeClick"></h-tree>
          </div>
        </h-col>
        <h-col span="19" class="body-right">
          <h-tabs v-model="tabIndex" @on-click="tabClick" v-if="isShare" class="tab-box">
            <h-tab-pane label="首页" name="home"></h-tab-pane>
            <h-tab-pane label="我的素材库" name="owner"></h-tab-pane>
          </h-tabs>
          <div class="body-right-box">
<!--            <div class="custom-title">-->
<!--              <span class="custom-text" v-if="currentMenu.length">{{ currentMenu[0].title }}</span>-->
<!--            </div>-->
            <div class="mms-search-area">
              <h-form :model="formData" ref="formData" :label-width="100" cols="3" >
                <h-form-item label="素材名称/ID" prop="keyword">
                  <h-input v-model.trim="formData.keyword" :maxlength="255" :byteNum="3" :lengthByByte="true" :filterRE="/[<>]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/ig" placeholder="请输入素材名称/ID"></h-input>
                </h-form-item>
                <h-form-item label="标签名称" prop="tag_id_value">
                  <h-cascader :data="tagData" v-model="formData.tag_id_value" multiple></h-cascader>
                </h-form-item>
                <h-form-item class="no-label">
                  <h-button type="primary" @click="handleSearch(1)">查询</h-button>
                  <h-button @click="handleReset">重置</h-button>
                </h-form-item>
              </h-form>
            </div>
            <div>
              <h-button style="margin-bottom: 9px" type="primary" @click="openUploadModal" v-if="systemConfigInfo.material_popup_upload === '1'"  :disabled="currentMenu[0].id === '-1'">上传素材</h-button>
            </div>
            <div class="table-box">
              <h-table v-if="!multiple" stripe :columns="columns" :data="materialListData" @on-current-change="handleSelectRow" :height="calculateHeight" highlight-row showTitle rowSelectOnly ref="selectTable"></h-table>
              <h-table v-else stripe :columns="columns" :data="materialListData" :height="calculateHeight" showTitle rowSelect dataCheckedProp
                       @on-select="selectChange"
                       @on-select-cancel="selectChangeCancel"
                       @on-select-all="selectChangeAll"
                       ref="selectTable"></h-table>
              <div style="text-align: right; margin-top: 8px">
                <h-page :total="total" :current="current" size="small" show-total placement="top" show-elevator show-sizer @on-change="pageChange" @on-page-size-change="pageSizeChange"></h-page>
              </div>
            </div>
            <div style="text-align: right; margin-top: 16px">
              <h-button type="ghost" @click="cancel">取消</h-button>
              <h-button style="margin-left: 8px" type="primary" @click="save">确定</h-button>
            </div>
          </div>
        </h-col>
      </h-row>
      </h-msg-box>
    </div>
    <!--上传弹窗-->
    <div v-if='showUploadModal'>
      <h-msg-box title="上传素材" width='676' v-model="showUploadModal" footerHide @on-close="closeUploadModal" :mask-closable="false" class="set-modal-style upload-modal">
        <h-form :model="uploadForm" ref="uploadForm" :label-width="116" cols="1">
          <h-form-item label="选择素材" required>
            <commonUpload v-model="uploadList" :systemConfigInfo="systemConfigInfo" :omsGSV="omsGSV" :cmmGSV="cmmGSV"></commonUpload>
          </h-form-item>
          <h-form-item label="素材名称" prop="materiel_name" required>
            <h-input v-model.trim="uploadForm.materiel_name" :filterRE="/[<>]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/ig" placeholder="请输入素材名称"></h-input>
          </h-form-item>
          <h-form-item label="有效期" prop="valid_time" :required="isShare">
            <h-date-picker
              @on-change="validTimeChange"
              v-model="valid_time"
              format="yyyy/MM/dd"
              type="daterange"
              placeholder="选择日期和时间"
              :options="options"
            ></h-date-picker>
          </h-form-item>
          <h-form-item label="是否共享" prop="share_flag" :required="isShare" v-if="isShare">
            <h-radio-group v-model="uploadForm.share_flag">
              <h-radio label="1">
                <span>是</span>
              </h-radio>
              <h-radio label="0">
                <span>否</span>
              </h-radio>
            </h-radio-group>
          </h-form-item>
          <h-form-item label="选择标签" >
            <MmsTagChoose :cmmGSV="cmmGSV" :systemConfigInfo="systemConfigInfo" :sendChooseTagList.sync='chooseTagList'></MmsTagChoose>
          </h-form-item>
          <h-form-item label="素材简介" prop="material_intro">
            <h-input v-model.trim="uploadForm.material_intro" placeholder="请输入素材简介" type="textarea" :rows="2" :canResize="false" :maxlength="200" :byteNum="3" :lengthByByte="false" :filterRE="/[<>]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/ig"></h-input>
          </h-form-item>
        </h-form>
        <div class="footer-btn">
          <h-button type="ghost" @click="closeUploadModal">取消</h-button>
          <h-button type="primary" @click="saveUploadModal" :loading="uploadLoading" :disabled="uploadLoading">确定</h-button>
        </div>
      </h-msg-box>
    </div>
  </div>
</template>

<script>
import Api from './api/apis.js'
import Picture from './images/picture.svg'
import Excel from './images/excel.svg'
import PPT from './images/ppt.svg'
import Word from './images/word.svg'
import Video from './images/video.svg'
import Music from './images/music.svg'
import PDF from './images/pdf.svg'
import PSD from './images/psd.svg'
import H5 from './images/h5.svg'
import Other from './images/other.svg'
import MmsTagChoose from './mmsTagChoose'
import commonUpload from './commonUpload'
export default {
  name: 'MaterialSelectModal',
  components: { MmsTagChoose, commonUpload },
  props: {
    value: {
      required: true,
      type: [Object, Array]
    },
    // 接口前缀
    omsGSV: {
      required: false,
      type: String,
      default() {
        return ''
      }
    },
    cmmGSV: {
      required: false,
      type: String,
      default() {
        return ''
      }
    },
    // check校验函数,需返回true/false
    check: {
      required: false,
      default() {
        return null
      }
    },
    // 弹窗展示的资源类型
    material_format: {
      //  0: {material_format_name: "视频", material_format_id: "1"}
      //  1: {material_format_name: "音频", material_format_id: "2"}
      //  2: {material_format_name: "文档", material_format_id: "3"}
      //  3: {material_format_name: "图片", material_format_id: "4"}
      //  4: {material_format_name: "psd", material_format_id: "5"}
      required: false,
      type: String,
      default() {
        return '4'
      }
    },
    // 是否开启多选
    multiple: {
      required: false,
      type: Boolean,
      default() {
        return false
      }
    },
    // 多选限制选择数量
    multipleSize: {
      required: false,
      type: Number,
      default() {
        return 10
      }
    }
  },
  data() {
    let _this = this
    return {
      systemConfigInfo: {
        'material_download_record': '1',
        'tag_level': '2',
        'material_dir_create_auth': '0',
        'del_catalog_check_material': '1',
        'file_upload_url': 'http://10.20.146.245:62559/file/receiveFile',
        'catalog_level': '2',
        'material_share': '1',
        'material_popup_upload': '0',
        'need_process_audit_id': '1',
        'material_format': '1-视频,2-音频,3-文档,4-图片,5-psd,',
        'need_role_codes': '1',
        'material_admin_role_code': 'material_admin',
        'catalog_alias': '分类',
        'material_alias': '素材',
        'material_audit_warn': '0',
        'support_machine_audit': '0',
        'file_upload_model': '1',
        'folder_material_record': '1'
      },
      isShow: false,
      // 文件类型列表
      type_arr: [
        {
          id: '1',
          url: Picture,
          type: ['png', 'jpg', 'jpeg', 'PNG', 'JPG', 'JPEG', 'bmp', 'BMP', 'gif', 'GIF'],
          name: '图片'
        },
        {
          id: '2',
          url: Excel,
          type: ['xls', 'xlsx', 'XLS', 'XLSX'],
          name: '文档'
        },
        {
          id: '3',
          url: PPT,
          type: ['ppt', 'PPT', 'pptx', 'PPTX'],
          name: '文档'
        },
        {
          id: '4',
          url: Word,
          type: ['doc', 'docx', 'DOC', 'DOCX', 'txt', 'TXT'],
          name: '文档'
        },
        {
          id: '5',
          url: Video,
          type: ['avi', 'm4v', 'wmv', 'mp4', 'mov', 'mpeg', 'rm', 'rmvb', 'WMV', 'AVI', 'M4V', 'MP4', 'MOV', 'MPEG', 'RM', 'RMVB'],
          name: '视频'
        },
        {
          id: '6',
          url: Music,
          type: ['mp3', 'MP3', 'wma', 'WMA', 'aac', 'AAC'],
          name: '音频'
        },
        {
          id: '7',
          url: PDF,
          type: ['pdf', 'PDF'],
          name: '文档'
        },
        {
          id: '9',
          url: PSD,
          type: ['psd', 'PSD'],
          name: 'PSD'
        },
        {
          id: '10',
          url: H5,
          type: [],
          name: 'H5'
        },
        {
          id: '99',
          url: Other,
          type: []
        }
      ],
      isShare: true,
      tabIndex: '',
      tagData: [],
      currentMenu: [],
      filterText: '',
      tData: [],
      treeData: [],
      formData: {
        keyword: '',
        tag_id_value: []
      },
      currentRow: {}, // 对象格式
      currentMultipleRows: [], // 数组格式
      selectedRow: this.multiple ? this.value : Array.isArray(this.value) ? this.value : [this.value], // 选中物料
      materialListData: [],
      columns: [
        {
          title: '缩略图',
          key: 'thumbnail_path',
          width: 70,
          // ellipsis: true,
          render(h, params) {
            let src = _this.getImg(params.row)
            return h('div', {
              style: {
                'height': '64px',
                'width': '40px',
                'line-height': '64px'
              }
            },
            [h('img', {
              style: {
                'width': '40px',
                'height': '40px'
              },
              attrs: {
                src
              }
            })])
          }
        },
        {
          title: '素材名称',
          key: 'materiel_name',
          // ellipsis: true,
          render(h, params) {
            return <span>
              {params.row.materiel_name}
              <span v-show={_this.isShare && params.row.share_flag === '1'} style={{width: '36px',
                height: '16px',
                display: 'inline-block',
                color: '#fff',
                marginLeft: '8px',
                textAlign: 'center',
                background: 'linear-gradient(270deg, #316BF2 0%, #70A3FC 100%)',
                borderRadius: '6px 2px 6px 2px'}}>共享</span>
              <span v-show={params.row.expired_flag === '0'} style={{width: '46px',
                height: '16px',
                display: 'inline-block',
                color: '#fff',
                marginLeft: '8px',
                textAlign: 'center',
                background: 'linear-gradient(270deg, #9F9F9F 0%, #C5C5C5 100%)',
                borderRadius: '6px 2px 6px 2px'}}>已过期</span>
            </span>
          }
        },
        {
          title: '素材ID',
          key: 'material_ext_id',
          ellipsis: true,
          width: 110
        },
        {
          title: '上传人',
          key: 'creator_no',
          ellipsis: true,
          width: 120
        },
        {
          title: '上传时间',
          key: 'create_date_time',
          width: 160,
          // ellipsis: true,
          render(h, params) {
            let time = params.row.create_date_time
            let text = ('' + time).replace(
              /^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/,
              '$1/$2/$3 $4:$5:$6'
            )
            if (text) {
              return h(
                'span',
                {
                  attrs: {
                    title: text
                  }
                },
                text
              )
            } else {
              return h('span', '--')
            }
          }
        },
        {
          title: '标签名称',
          key: 'tag_rows',
          width: 160,
          // ellipsis: true,
          render(h, params) {
            let tag_name_list = []
            params.row.tag_rows.forEach(i => {
              tag_name_list.push(i.tag_name)
            })
            let result = tag_name_list.join('; ')
            return <h-tooltip placement="top" content={result}>
              <div style={{overflow: 'hidden',
                textOverflow: 'ellipsis',
                width: '128px',
                whiteSpace: 'nowrap'}}>{result}</div>
            </h-tooltip>
          }
        }
      ],
      current: 1,
      total: 0,
      pageInfo: {
        pageNo: 1,
        pageSize: 10
      },
      // 上传
      uploadLoading: false,
      showUploadModal: false, // 显示弹框
      uploadList: [],
      options: {
        shortcuts: [
          {
            text: '一周',
            value() {
              const end = new Date()
              const start = new Date()
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 7)
              return [start, end]
            }
          },
          {
            text: '一个月',
            value() {
              const end = new Date()
              const start = new Date()
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 30)
              return [start, end]
            }
          },
          {
            text: '一季度',
            value() {
              const end = new Date()
              const start = new Date()
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 90)
              return [start, end]
            }
          },
          {
            text: '一年',
            value() {
              const end = new Date()
              const start = new Date()
              end.setTime(start.getTime() + 3600 * 1000 * 24 * 365)
              return [start, end]
            }
          },
          {
            text: '长期',
            value() {
              // const end = new Date()
              // const start = new Date()
              // end.setTime(start.getTime() + 3600 * 1000 * 24 * 365 * 100)
              return [new Date(), new Date(`${new Date().getFullYear() + 100}/${new Date().getMonth() + 1}/${new Date().getDate()}`)]
            }
          }
        ]
      },
      valid_time: [new Date(), new Date(`${new Date().getFullYear() + 100}/${new Date().getMonth() + 1}/${new Date().getDate()}`)],
      // 上传表单
      uploadForm: {
        share_flag: '1',
        materiel_name: '',
        material_intro: '',
        valid_time: []
      },
      // 选择标签
      chooseTagList: []
    }
  },
  computed: {
    calculateHeight() {
      // tab高度和上传按钮高度为37px
      if (this.isShare && this.systemConfigInfo.material_popup_upload === '1') {
        // 有tab，有上传按钮
        return 422 - 37 - 37
      }
      if (!this.isShare && this.systemConfigInfo.material_popup_upload !== '1') {
        // 无tab,无上传按钮
        return 422
      }
      if (!this.isShare && this.systemConfigInfo.material_popup_upload === '1') {
        // 无tab,有上传按钮
        return 422 - 37
      }
      if (this.isShare && this.systemConfigInfo.material_popup_upload !== '1') {
        // 有tab,无上传按钮
        return 422 - 37
      }
    }
  },
  watch: {
    filterText: {
      handler(val) {
        if (this.$refs.tree) {
          this.$refs.tree.filter(val)
        }
      },
      immediate: true
    },
    uploadList: {
      handler(val) {
        if (val.length) {
          let splitIndex = val[0].file_name.lastIndexOf('.')
          this.uploadForm.materiel_name = val[0].file_name.substring(0, splitIndex)
        }
      }
    }
  },
  created() {
    Api.getSystemConfigInfo(this.cmmGSV + '/getSystemConfigInfo').then(res => {
      if (res && res.data && !res.data.error_info) {
        this.systemConfigInfo = JSON.parse(res.data.system_config_info)
        let arr = this.systemConfigInfo.material_types.split(';')
        arr.forEach((item, index) => {
          let id = Number(item.split(':')[0])
          let sub1 = this.type_arr.find(j => j.id == id)
          sub1.type = item.split(':')[1].split(',')
        })
        this.systemConfigInfo.type_arr = this.type_arr
        this.isShare = this.systemConfigInfo.material_share === '1'
        this.tabIndex = this.isShare ? 'home' : ''
      } else {
        this.$hMessage.error('错误信息：' + (res.data.error_info || res.data.ret_info))
      }
    })
    if (this.multiple) {
      this.columns.unshift({
        type: 'selection',
        width: 40,
        align: 'center'
      })
    }
  },
  mounted() {
  },
  methods: {
    async init() {
      await this.getMaterialCatalogList()
      this.defaultSelect()
      this.getTagList()
    },
    transformTree(list, options = {}) {
      const {
        keyField = 'id',
        childField = 'children',
        parentField = '_parentId'
      } = options
      const tree = []
      const record = {}
      for (let i = 0, len = list.length; i < len; i++) {
        const item = list[i]
        const id = item[keyField]
        if (!id) {
          continue
        }
        if (record[id]) {
          item[childField] = record[id]
        } else {
          item[childField] = record[id] = []
        }
        if (item[parentField]) {
          const parentId = item[parentField]
          if (!record[parentId]) {
            record[parentId] = []
          }
          record[parentId].push(item)
        } else {
          tree.push(item)
        }
      }
      return tree
    },
    // 获取菜单列表
    getMaterialCatalogList() {
      return Api.getMaterialCatalogList(this.cmmGSV + '/getMaterialCatalogList').then(res => {
        let result = res.data.catalog_rows
        // 补上全部素材目录
        let allCatalog = {
          catalog_id: '-1',
          catalog_name: '全部素材'
        }
        result.unshift(allCatalog)
        result.forEach((item) => {
          item.id = item.catalog_id
          item.expand = true
          if (item.parent_id) {
            item._parentId = item.parent_id
          }
          item.title = item.catalog_name
        })
        this.tData = result
        let options = {
          keyField: 'id',
          childField: 'children',
          parentField: '_parentId'
        }
        this.treeData = this.transformTree(this.tData, options)
      })
    },
    // 默认选中第一个目录
    defaultSelect() {
      if (this.treeData.length) {
        this.$set(this.treeData[0], 'selected', true)
        this.treeClick(null, this.treeData[0])
      }
    },
    // 树点击
    treeClick(arr, obj) {
      this.currentMenu = []
      this.currentMenu.push(obj)
      this.handleSearch(1)
    },
    // tab点击
    tabClick(name) {
      this.handleSearch(1)
    },
    handleSearch(pageNo) {
      if (pageNo) this.pageInfo.pageNo = pageNo
      this.getMaterialStoreUploadList()
    },
    handleReset() {
      this.$refs.formData.resetFields()
      this.formData.tag_id_value = []
      this.handleSearch(1)
    },
    getMaterialStoreUploadList() {
      let tag_id_value = []
      this.formData.tag_id_value.forEach(item => {
        tag_id_value.push(item[item.length - 1])
      })
      let params = {
        catalog_id: this.currentMenu[0].id,
        page_no: this.pageInfo.pageNo,
        page_size: this.pageInfo.pageSize,
        keyword: this.formData.keyword,
        tag_id_value: tag_id_value,
        material_format: this.material_format,
        tabIdentity: this.isShare ? this.tabIndex : 'owner'
      }
      Api.getMaterialStoreUploadList(params, this.cmmGSV + '/getMaterialStoreUploadList').then(res => {
        this.materialListData = res.data.list
        this.materialListData.forEach(item => {
          item._isHighlight = false
          item._isChecked = false
        })
        this.total = res.data.total_count
        this.current = res.data.page_no
        // 选中效果
        this.$nextTick(() => {
          if (this.multiple) {
            // 如果是多选
            if (this.currentMultipleRows.length !== 0) {
              this.currentMultipleRows.forEach(rowItem => {
                let index = this.materialListData.findIndex(item => {
                  return rowItem.material_ext_id === item.material_ext_id
                })
                if (index > -1) {
                  this.$refs.selectTable.handleCheckData(index, true)
                }
              })
            }
          } else {
            // 如果是单选
            if (Object.keys(this.currentRow).length !== 0) {
              let index = this.materialListData.findIndex(item => {
                return item.material_ext_id === this.currentRow.material_ext_id
              })
              if (index > -1) {
                this.$refs.selectTable.handleCheckData(index, true)
              }
            }
          }
        })
      })
    },
    // 获取标签选择下拉框
    getTagList() {
      Api.getTagList(this.cmmGSV + '/getTagAndClass').then(res => {
        let tagList = res.data.tag_list
        let classList = res.data.class_list
        tagList.forEach(item => {
          item.value = item.tag_id
          item.label = item.tag_name
        })
        let classListLv1 = []
        let classListLv2 = []
        classList.forEach(item => {
          item.value = item.class_id
          item.label = item.class_name
          item.children = []
          tagList.forEach(i => {
            if (i.class_id === item.class_id) {
              item.children.push(i)
            }
          })
          if (item.tree_level === 1) {
            classListLv1.push(item)
          }
          if (item.tree_level === 2) {
            classListLv2.push(item)
          }
        })
        classListLv2.forEach(item => {
          let obj = classListLv1.find(i => {
            return i.class_id === item.parent_code
          })
          if (obj) {
            obj.children.push(item)
          }
        })
        this.tagData = classListLv1
      })
    },
    filterNode(val, data, node) {
      if (!val) return true
      return node.title.indexOf(val) !== -1
    },
    // 获取缩略图
    getImg(item) {
      if (item.thumbnail_path) {
        return item.thumbnail_path
      } else if (item.materiel_type === '1') {
        let index = item.materiel_path.lastIndexOf('.')
        let ext = item.materiel_path.slice(index + 1)
        if (['svg', 'svga'].includes(ext.toLowerCase())) {
          let filter = this.type_arr.find(value => value.id == item.materiel_type)
          if (filter) {
            return filter.url
          } else {
            let Other = this.type_arr.find(value => value.id == '99')
            return Other.url
          }
        } else {
          return item.materiel_path
        }
      } else {
        let filter = this.type_arr.find(value => value.id == item.materiel_type)
        if (filter) {
          return filter.url
        } else {
          let Other = this.type_arr.find(value => value.id == '99')
          return Other.url
        }
      }
    },
    handleSelectRow(currentRow) {
      this.currentRow = currentRow
    },
    // 点击分页
    pageChange(i) {
      this.handleSearch(i)
    },
    // 页码改变
    pageSizeChange(i) {
      this.pageInfo.pageSize = i
      if (this.pageInfo.pageNo === 1) {
        this.handleSearch()
      }
    },
    save() {
      if (this.multiple) {
        if (this.currentMultipleRows.length) {
          if (this.currentMultipleRows.length > this.multipleSize) {
            return this.$hMessage.warning(`选择的数量超过上限值(${this.multipleSize}个)`)
          }
          if (this.check) {
            if (this.check(this.currentMultipleRows)) {
              this.selectedRow = JSON.parse(JSON.stringify(this.currentMultipleRows))
              this.$emit('input', this.currentMultipleRows)
              this.cancel()
            } else {
              return false
            }
          } else {
            this.selectedRow = JSON.parse(JSON.stringify(this.currentMultipleRows))
            this.$emit('input', this.currentMultipleRows)
            this.cancel()
          }
        } else {
          this.$hMessage.warning('请先选择一项')
        }
      } else {
        if (Object.keys(this.currentRow).length !== 0) {
          if (this.check) {
            if (this.check(this.currentRow)) {
              this.selectedRow = [this.currentRow]
              this.$emit('input', this.currentRow)
              this.cancel()
            } else {
              return false
            }
          } else {
            this.selectedRow = [this.currentRow]
            this.$emit('input', this.currentRow)
            this.cancel()
          }
        } else {
          this.$hMessage.warning('请先选择一项')
        }
      }
    },
    async showModal() {
      await this.init()
      let valueCopy = JSON.parse(JSON.stringify(this.value))
      this.currentRow = Array.isArray(valueCopy) ? valueCopy[0] : valueCopy
      this.currentMultipleRows = valueCopy
      this.isShow = true
    },
    loadErrorImg() {
      if (event.type === 'error') {
        event.target.src = require('./images/image-error.png')
      }
    },
    deleteItem(index) {
      if (this.multiple) {
        this.currentMultipleRows.splice(index, 1)
        this.selectedRow = JSON.parse(JSON.stringify(this.currentMultipleRows))
        this.$emit('input', this.currentMultipleRows)
      } else {
        this.currentRow = {}
        this.selectedRow = [this.currentRow]
        this.$emit('input', this.currentRow)
      }
    },
    cancel() {
      this.isShow = false
    },
    // 多选
    selectChange(selection, row) {
      let has = this.currentMultipleRows.some(item => item.material_ext_id === row.material_ext_id)
      if (!has) {
        this.currentMultipleRows.push(row)
      }
    },
    selectChangeCancel(selection, row) {
      let index = this.currentMultipleRows.findIndex(item => item.material_ext_id === row.material_ext_id)
      this.currentMultipleRows.splice(index, 1)
    },
    selectChangeAll(selection) {
      if (selection.length) {
        selection.forEach(item => {
          this.selectChange('', item)
        })
      } else {
        this.materialListData.forEach(item => {
          this.selectChangeCancel('', item)
        })
      }
    },
    // 上传弹窗
    openUploadModal() {
      this.validTimeChange()
      this.showUploadModal = true
    },
    checkType(type) {
      let result = this.type_arr.find(item => {
        return item.type.includes(type)
      })
      if (result) {
        return result.id
      } else {
        return '99'
      }
    },
    async saveUploadModal() {
      this.uploadLoading = true
      if (this.uploadList.length === 0) {
        this.$hMessage.warning('请先上传素材')
        this.uploadLoading = false
        return
      }
      // 素材名称长度校验
      if (this.uploadForm.materiel_name.length > 20) {
        this.$hMessage.warning(`素材名称过长`)
        this.uploadLoading = false
        return
      }
      // check name
      let args = {
        catalogId: this.currentMenu[0].id,
        materielName: this.uploadForm.materiel_name + '.' + this.uploadList[0].file_extension
      }
      let res = await Api.existsMaterielByCatalogIdAndName(args, this.cmmGSV + '/existsMaterielByCatalogIdAndName')
      if (res.data.exist_flag === '1') {
        this.$hMessage.error(`该分类下已有名称为${this.uploadForm.materiel_name}的素材`)
        this.uploadLoading = false
        return
      }
      let materielaudit = []
      this.uploadList.forEach(item => {
        let obj = {
          materiel_name: item.file_name,
          thumbnail_path: item.thumbnail_path,
          materiel_path: item.file_path,
          materiel_size: item.file_size,
          materiel_type: item.file_type,
          image_width: item.width,
          image_height: item.height
        }
        materielaudit.push(obj)
      })
      let chooseTagListCopy = []
      this.chooseTagList.forEach((item) => {
        let obj = {}
        obj.tag_name = item.tagName
        obj.tag_id = item.id
        chooseTagListCopy.push(obj)
      })
      let params = {
        default_params: JSON.stringify(materielaudit),
        share_flag: this.uploadForm.share_flag,
        materiel_name: this.uploadForm.materiel_name,
        material_intro: this.uploadForm.material_intro,
        shareBeginValidTime: this.uploadForm.valid_time[0],
        shareEndValidTime: this.uploadForm.valid_time[1],
        catalog_id: this.currentMenu[0].id,
        tag_ids: JSON.stringify(chooseTagListCopy)
      }
      this.$refs.uploadForm.validate(async (valid) => {
        if (!valid) {
          this.uploadLoading = false
          return false
        }
        Api.postMmsMaterielAudit(params, this.cmmGSV + '/postMmsMaterielAudit').then(res => {
          this.uploadLoading = false
          if (res.data && res.data.ret_code === 200) {
            this.$hMessage.success('保存成功')
            this.resetUploadModal()
            this.handleSearch()
          } else {
            this.$hMessage.error(res.data.error_info || res.data.ret_info)
          }
        }).catch(() => {
          this.uploadLoading = false
        })
      })
    },
    closeUploadModal() {
      this.resetUploadModal()
    },
    resetUploadModal() {
      this.showUploadModal = false
      this.uploadList = []
      this.uploadForm.share_flag = '1'
      this.uploadForm.materiel_name = ''
      this.uploadForm.material_intro = ''
      this.uploadForm.valid_time = []
      this.valid_time = [new Date(), new Date(`${new Date().getFullYear() + 100}/${new Date().getMonth() + 1}/${new Date().getDate()}`)]
      this.chooseTagList = []
    },
    validTimeChange() {
      this.uploadForm.valid_time = this.valid_time.map((item) => {
        return this.covertDateTimeToInteger(item)
      })
    },
    covertDateTimeToInteger(date) {
      if (!date || !(date instanceof Date)) {
        return ''
      }
      let y = date.getFullYear()
      let m = (date.getMonth() + 1).toString().padStart(2, '0')
      let d = date.getDate().toString().padStart(2, '0')
      let h = date.getHours().toString().padStart(2, '0')
      let minute = date.getMinutes().toString().padStart(2, '0')
      let s = date.getSeconds().toString().padStart(2, '0')
      return parseInt('' + y + m + d + h + minute + s)
    }
  }
}
</script>

<style lang="scss" scoped>
.material-select {
  .single-select {
    .select-area {
      position: relative;
      width: 160px;
      height: 92px;
      background: #F7F7F7;
      border: 1px dashed #F7F7F7;
      display: flex;
      cursor: pointer;
      justify-content: center;
      align-items: center;
      &:hover {
        border-color: #298DFF;
      }
      .preview {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        >img {
          width: 100%;
          height: 100%;
        }
        .delete-btn {
          position: absolute;
          right: 4px;
          top: 2px;
        }
      }
    }
  }
  .multiple-select {
    .multiple-select-area {
      position: relative;
      width: 160px;
      height: 92px;
      background: #F7F7F7;
      border: 1px dashed #F7F7F7;
      display: inline-flex;
      cursor: pointer;
      justify-content: center;
      align-items: center;
      &:hover {
        border-color: #298DFF;
      }
    }
    .multiple-preview {
      width: 160px;
      height: 92px;
      display: inline-block;
      margin-right: 10px;
      border: 1px dashed #F7F7F7;
      position: relative;
      &:hover {
        border-color: #298DFF;
      }
      >img {
        width: 100%;
        height: 100%;
      }
      .delete-btn {
        position: absolute;
        right: 4px;
        top: 2px;
      }
    }
  }
}
.material-select-modal {
  /deep/ .h-modal-body {
    padding: 0;
  }
  .body-left {
    border-right: 1px solid #e6e6e6;
    height: 100%;
    padding: 8px;
    .search-input {
      margin-bottom: 8px;
    }
    .tree-box {
      height: calc(100% - 36px);
      overflow: auto;
      /deep/ .h-tree-empty {
        text-align: center;
        margin-top: 100px;
      }
    }
  }
  .body-right {
    height: 100%;
    padding-bottom: 16px;
    .tab-box /deep/ .h-tabs-bar {
      margin-bottom: 0;
    }
    .body-right-box {
      padding: 0 16px;
      .custom-title {
        margin-top: 16px;
        position: relative;
        display: flex;
        align-items: center;
        .custom-text {
          padding: 0 6px;
          background: #fff;
          border-left: 4px solid #037DF3;;
        }
      }
      .mms-search-area {
        margin-top: 16px;
        .h-form {
          .h-form-item {
            padding-right: 8px;
            .h-form-item-content{
              line-height: 26px;
              .h-cascader-multiple {
                height: 28px;
              }
            }
          }
        }
        .no-label {
          .h-form-item-requiredIcon{
            display: none!important;
          }
          .h-form-item-content {
            margin-left: 0 !important;
            padding-left: 0 !important;
            // font-size: 0;
            > button {
              float: left;
              margin-right: 8px;
              margin-left: 0px;
            }
          }
        }
      }
    }
  }
}
.upload-modal {
  .footer-btn {
    text-align: right;
    margin-top: 12px;
  }
}
</style>
