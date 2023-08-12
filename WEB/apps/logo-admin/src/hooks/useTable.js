import { reactive, ref } from "vue";
import { Modal, message } from "ant-design-vue";
import modalConfirm from "ant-design-vue/es/modal/confirm";

/**
 * 表格列表
 * @param {*} request
 */
export default function useTable(request) {
  // 页码
  const page = reactive({
    current: 0,
    pageSize: 30,
    total: 0,
  });
  // 加载状态
  const loading = ref(false);
  // 数据列表
  const list = ref([]);
  // 查询条件
  let condition = {};

  // 查询数据
  function queryData(params = {}) {
    const {
      pageNum = page.current,
      pageSize = page.pageSize,
      ...data
    } = params;
    // 判断查询条件
    if (!_.isEmpty(data)) condition = data;
    // 请求状态
    loading.value = true;
    return request(
      Object.assign(
        // 参数传入
        {
          pageNum,
          pageSize,
        },
        condition
      )
    )
      .then((res) => {
        // 更新列表数据
        list.value = _.get(res, "data.list");
        // 更新页码
        const { pageNum, pageSize = page.pageSize } = params;
        page.total = _.get(res, "data.total");
        page.current = pageNum;
        page.pageSize = pageSize;
      })
      .finally(() => (loading.value = false))
      .catch((err) => {
        message.error("查询失败，请稍后再试！");
        console.warn(err);
      });
  }

  // 查询
  function onSerach(data) {
    return queryData({
      pageNum: 1,
      ...data,
    });
  }

  // 分页、排序、筛选
  function onChange(page) {
    return queryData({
      pageNum: page.current,
      pageSize: page.pageSize,
    });
  }

  // 删除
  function createDelEvent(callback) {
    return (data) =>
      Modal.confirm({
        content: "删除后不可恢复，是否确认删除？",
        okText: "删除",
        okType: "danger",
        onOk: () =>
          callback(data)
            .then(() => message.success("删除成功"))
            .catch(() => message.error("删除失败")),
      });
  }

  // event：弹窗事件
  function createModalEvent(compt, { props: defProps, ...modalConf } = {}) {
    return (props) => {
      let el = null; // 子元素引用
      return modalConfirm(
        Object.assign(
          modalConf,
          // 默认参数（不可修改）
          {
            type: "form",
            maskClosable: false,
            icon: () => null,
          },
          // 事件
          {
            onOk: () => el?.onOk && el.onOk(),
            onCancel: () => el?.onCancel && el.onCancel(),
            content: (h) =>
              h(compt, {
                props: Object.assign({}, defProps, props),
                ref: (e) => (el = e),
              }),
          }
        )
      );
    };
  }

  return {
    // data
    list,
    page,
    loading,
    // method
    onSerach,
    onChange,
    createModalEvent,
    createDelEvent,
  };
}
