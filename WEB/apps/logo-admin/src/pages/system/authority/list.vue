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
      :bordered="true"
      :data-source="list"
      :pagination="page"
      :columns="columns"
      @change="onChange"
    >
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <a-button type="link" size="small" @click="onEdit({ record })"
          >修改</a-button
        >
        <!-- btn:删除 -->
        <a-popconfirm title="是否确认删除该权限？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import useTable from "@/hooks/useTable";
import { mapState } from "vuex";
import { systemService } from "@/services";
import FormSerach from "../../../components/form/FormSerach.vue";
import { mapDictObject } from "@/store/helpers";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 字典项
    ...mapState({
      // 权限类型
      DictPermissionType: mapDictObject("permissionType"),
    }),
    // 表格列配置
    columns() {
      return [
        {
          title: "ID",
          dataIndex: "id",
          key: "id",
        },
        {
          title: "权限名称",
          dataIndex: "permissionName",
          key: "permissionName",
        },
        {
          title: "父级ID",
          dataIndex: "parentId",
          key: "parentId",
        },
        {
          title: "权限类型",
          dataIndex: "permissionType",
          key: "permissionType",
          customRender: (val) => this.DictPermissionType[val],
        },
        {
          title: "权限等级",
          dataIndex: "permissionLevel",
          key: "permissionLevel",
        },
        {
          title: "权限路径",
          dataIndex: "permissionPath",
          key: "permissionPath",
        },
        {
          title: "权限组",
          dataIndex: "permissionGroup",
          key: "permissionGroup",
        },
        {
          title: "图标",
          dataIndex: "iconPath",
          key: "iconPath",
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
        { name: "permissionName", label: "权限名称" },
        { name: "permissionType", label: "权限类型" },
        { name: "permissionLevel", label: "权限等级" },
        { name: "permissionGroup", label: "权限组" },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      list,
      page,
      formData,
      onSerach,
      onChange,
      createModalEvent,
    } = useTable(systemService.getSysPermissionListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, {
      title: "新增权限",
    });
    // 编辑事件
    const onEdit = createModalEvent(Detail, {
      title: "编辑权限",
    });
    return {
      formData,
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
    // 获取字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["permissionType"],
    });
  },
  methods: {
    onDel(record) {
      systemService
        .deleteSysPermissionById(_.pick(record, ["id"]))
        .then(() => message.success("删除成功"))
        .catch((err) =>
          message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped></style>
