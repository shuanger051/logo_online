import { reactive, ref } from 'vue'
import { Modal, message } from 'ant-design-vue'
import modalConfirm from 'ant-design-vue/es/modal/confirm'

/**
 * 表格列表
 * @param {*} request 
 */
export default function useTable(request) {

  // 表单查询
  const formData = reactive({})
  // 页码
  const page = reactive({
    current: 0,
    pageSize: 30,
    total: 0
  })
  // 数据列表
  const list = ref([])

  // 查询数据
  function queryData(params = {}) {
    return request(params).then((res) => {
      // 更新列表数据
      list.value = _.get(res, 'data.list')
      // 更新页码
      const { pageNum, pageSize = page.pageSize } = params
      page.total = _.get(res, 'data.total')
      page.current = pageNum
      page.pageSize = pageSize
    })
      .catch((err) => {
        message.error("查询失败，请稍后再试！")
        console.warn(err)
      })
  }

  // 查询
  function onSerach() {
    return queryData({
      pageNum: 1,
      // 查询字段
      ...formData,
    })
  }

  // 删除
  function createDelEvent(callback) {
    return (data) =>
      Modal.confirm({
        content: "删除后不可恢复，是否确认删除？",
        okText: "删除",
        okType: "danger",
        onOk: () => callback(data)
          .then(() => {
            message.success("删除成功")
          })
          .catch(() => {
            message.error("删除失败")
          })
      })
  }

  // event：弹窗事件
  function createModalEvent(compt, { title, okText = "保存", cancelText } = {}) {
    return (props) => {
      let el = null; // 子元素引用
      return modalConfirm({
        title,
        okText,
        cancelText,
        maskClosable: false,
        icon: () => null,
        onOk: () => el?.onOk && el.onOk(),
        onCancel: () => el?.onCancel && el.onCancel(),
        content: (h) => h(compt, { props, ref: (e) => el = e })
      })
    }
  }

  // 重置查询
  function onReset() {
    Object.keys(formData).forEach(key => formData[key] = null)
  }

  // 分页、排序、筛选
  function onChange(page) {
    return queryData({
      pageNum: page.current,
      pageSize: page.pageSize,
      ...formData
    })
  }


  return {
    // data
    list,
    page,
    formData,
    // method
    onSerach,
    onReset,
    onChange,
    createModalEvent,
    createDelEvent
  }

}