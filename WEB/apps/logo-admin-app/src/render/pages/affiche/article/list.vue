<template>
  <div class="page-wrap" :style="`min-height: 0px; padding:24px 24px 0`">
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
      @change="onChange"
    >
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <a-button
          type="link"
          size="small"
          @click="onEdit({ record })"
          >修改</a-button
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
import useTable, { queryDictCache } from "@/hooks/useTable";
import { mapDictObject, mapDictSelect } from "@/store/helpers";
import { mapState } from "vuex";
import { afficheService } from "@/services";
import FormSerach from "@/components/form/FormSerach.vue";
export default {
  components: { FormSerach },
  computed: {
    ...mapState({
      // 栏目列表
      ColumnArr: (state) => {
        const list = mapDictSelect('channel')(state);
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
          width: "170px",
        },
        {
          title: "所属栏目",
          dataIndex: "channelId",
          key: "channelId",
          customRender: (val) => this.ColumnObject[val],
          width: "80px",
        },
        {
          title: "作者",
          dataIndex: "contentExt.author",
          key: "contentExt.author",
          width: "80px",
        },
        {
          title: "是否推荐",
          dataIndex: "isRecommend",
          key: "isRecommend",
          customRender: (val) => (val == "1" ? "是" : "否"),
          width: "80px",
        },

        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
          width: "80px",
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
        { name: "title", label: "文章标题" }
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

    return {
      formData,
      loading,
      list,
      page,
      onAdd,
      onEdit,
      onSerach,
      onChange,
    };
  },
  created() {
    this.onSerach();
    // 栏目列表
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["channel"],
    });
  },
  methods: {
    // event：删除
    onDel(record) {
      afficheService
        .deleteContentById(_.pick(record, ["id"]))
        .then(() => this.$message.success("删除成功"), this.onSerach())
        .catch((err) =>
          this.$message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped></style>
