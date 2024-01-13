<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <form-serach :fields="serachFields" @serach="onSerach">
      <a-button type="primary" @click="onAdd">新增</a-button>
    </form-serach>
    <!-- 结果列表 -->
    <a-table
      rowKey="id"
      size="small"
      :loading="loading"
      :bordered="true"
      :data-source="list"
      :pagination="page"
      :columns="columns"
      :scroll="{ x: 1800 }"
      @change="onChange"
    >
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <a-button
          v-if="record.status == '0'"
          type="link"
          size="small"
          @click="onEdit({ record })"
          >修改</a-button
        >
        <a-button
          v-if="record.status == '1'"
          type="link"
          size="small"
          @click="onAudit({ record })"
          >审核</a-button
        >
        <!-- btn:删除 -->
        <a-popconfirm title="是否确认删除该文章？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import Audit from "./audit";
import useTable, { queryDictCache } from "@/hooks/useTable";
import { mapDictObject, mapDictSelect } from "@/store/helpers";
import { mapState } from "vuex";
import { afficheService } from "@/services";
import FormSerach from "@/components/form/FormSerach.vue";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    ...mapState({
      // 文章状态
      DictStatus: mapDictObject("status"),
      // 文章状态
      StatusArr: mapDictSelect("status"),
      // 栏目列表
      ColumnArr: (state) => {
        const list = _.get(state, ["cache", "channel"], []);
        // select 使用
        return list.map((item) => {
          item.label = item.name;
          item.value = item.id;
          return item;
        });
      },
    }),
    ColumnObject() {
      return this.ColumnArr.reduce((dtm, item) => {
        const { name, id } = item;
        dtm[id] = name;
        return dtm;
      }, {});
    },
    // 表格列配置
    columns() {
      return [
        {
          title: "标题",
          dataIndex: "contentExt.title",
          key: "contentExt.title",
          width: "260px",
          fixed: "left",
        },
        {
          title: "副标题",
          dataIndex: "contentExt.shortTitle",
          key: "contentExt.shortTitle",
          width: "260px",
        },
        {
          title: "所属栏目",
          dataIndex: "channelId",
          key: "channelId",
          customRender: (val) => this.ColumnObject[val],
          width: "120px",
        },
        {
          title: "是否推荐",
          dataIndex: "isRecommend",
          key: "isRecommend",
          customRender: (val) => (val == "1" ? "是" : "否"),
          width: "80px",
        },
        {
          title: "状态",
          dataIndex: "status",
          key: "status",
          customRender: (val) => this.DictStatus[val],
          width: "100px",
        },
        {
          title: "日访问数",
          dataIndex: "viewsDay",
          key: "viewsDay",
          width: "100px",
        },
        {
          title: "作者",
          dataIndex: "contentExt.author",
          key: "contentExt.author",
          width: "140px",
        },
        {
          title: "摘要",
          dataIndex: "contentExt.description",
          key: "contentExt.description",
        },
        {
          title: "来源",
          dataIndex: "contentExt.origin",
          key: "contentExt.origin",
          width: "160px",
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
          width: "160px",
          fixed: "right",
        },
      ];
    },
    serachFields() {
      // 栏目列表
      return [
        {
          name: "channelId",
          label: "所属栏目",
          component: "select",
          props: {
            options: this.ColumnArr,
          },
        },
        { name: "title", label: "文章标题" },
        {
          name: "status",
          label: "状态",
          component: "select",
          props: {
            options: this.StatusArr,
          },
        },
        {
          name: "isRecommend",
          label: "是否推荐",
          component: "select",
          props: {
            options: [
              { value: "1", label: "是" },
              { value: "0", label: "否" },
            ],
          },
        },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      formData,
      loading,
      list,
      page,
      onSerach,
      onChange,
      onRefresh,
      createModalEvent,
    } = useTable(afficheService.getContentListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, {
      props: {
        refresh: onSerach,
      },
      title: "新增文章",
      width: "800px",
    });
    // 编辑事件
    const onEdit = createModalEvent(Detail, {
      props: {
        refresh: onRefresh,
      },
      title: "编辑文章",
      width: "800px",
    });
    // 审核
    const onAudit = createModalEvent(Audit, {
      props: {
        refresh: onRefresh,
      },
      title: "审核意见",
      okText: "通过",
      cancelText: "退回",
      closable: true,
      maskClosable: true,
    });

    return {
      formData,
      loading,
      list,
      page,
      onAdd,
      onEdit,
      onAudit,
      onSerach,
      onChange,
    };
  },
  created() {
    this.onSerach();
    // 获取字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["status"],
    });
    // 栏目列表
    afficheService
      .getChannelList()
      // 添加到缓存
      .then((res) => {
        const list = _.get(res, "data");
        this.$store.commit("cache/setCache", { key: "channel", val: list });
      });
  },
  methods: {
    // event：删除
    onDel(record) {
      afficheService
        .deleteContentById(_.pick(record, ["id"]))
        .then(() => this.$message.success("删除成功"))
        .catch((err) =>
          this.$message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped></style>
