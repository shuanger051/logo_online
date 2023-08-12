<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <form-serach :fields="serachFields" @serach="onSerach">
      <a-button type="primary" @click="onAdd">新增</a-button>
    </form-serach>
    <!-- 结果列表 -->
    <a-table
      rowKey="id"
      size="small"
      :bordered="true"
      :data-source="list"
      :columns="columns"
      :pagination="page"
      @change="onChange"
    >
      <template slot="operation" slot-scope="text, record">
        <!-- btn:修改 -->
        <a-button type="link" size="small" @click="onEdit({ record })"
          >修改</a-button
        >
        <!-- btn:删除 -->
        <a-popconfirm title="是否确认删除该角色？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import { mapState } from "vuex";
import { systemService } from "@/services";
import useTable from "@/hooks/useTable";
import { message } from "ant-design-vue";
import FormSerach from "../../../components/form/FormSerach.vue";
import { mapDictObject } from "@/store/helpers";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 字典项
    ...mapState({
      // 角色状态
      DictRoleStatus: mapDictObject("roleStatus"),
    }),
    // table 列字段
    columns() {
      return [
        {
          title: "角色名称",
          dataIndex: "roleName",
          key: "roleName",
        },
        {
          title: "角色等级",
          dataIndex: "roleLevel",
          key: "roleLevel",
        },
        { title: "描述", dataIndex: "description", key: "description" },
        {
          title: "角色状态",
          dataIndex: "roleStatus",
          key: "roleStatus",
          customRender: (val) => this.DictRoleStatus[val],
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
    // 查询字段
    serachFields() {
      return [
        { name: "roleName", label: "角色名" },
        { name: "roleLevel", label: "角色等级" },
        { name: "roleStatus", label: "角色状态" },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      formData,
      list,
      page,
      onSerach,
      onReset,
      onChange,
      createModalEvent,
    } = useTable(systemService.getSysRoleListByPage);

    // 新增
    const onAdd = createModalEvent(Detail, {
      title: "新增角色",
      props: {
        action: "add",
      },
    });

    // 编辑
    const onEdit = createModalEvent(Detail, {
      title: "修改",
      props: {
        action: "edit",
      },
    });

    return {
      formData,
      list,
      page,
      onAdd,
      onEdit,
      onSerach,
      onReset,
      onChange,
    };
  },
  created() {
    this.onSerach();
    // 获取字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["roleStatus"],
    });
  },
  methods: {
    // event：删除
    onDel(record) {
      systemService
        .deleteSysRoleById(_.pick(record, ["id"]))
        .then(() => message.success("删除成功"))
        .catch((err) =>
          message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped></style>
