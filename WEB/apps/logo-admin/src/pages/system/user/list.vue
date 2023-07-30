<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <form-serach :fields="serachFields" @serach="onSerach">
      <!-- 其他交互按钮 -->
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
      <!-- 禁用 -->
      <template slot="isDisabled" slot-scope="val, record">
        <a-popconfirm
          :title="`是否确认${val == '1' ? '启用' : '禁用'}该账号？`"
          @confirm="onDisabled(record)"
        >
          <a-switch
            :checked="val == '1'"
            checked-children="是"
            un-checked-children="否"
          ></a-switch>
        </a-popconfirm>
      </template>
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <a-button type="link" size="small" @click="onEdit({ record })"
          >修改</a-button
        >
        <a-popconfirm
          title="是否确认重置该账号密码？"
          @confirm="onResetPwd(record)"
        >
          <a-button type="link" size="small">重置密码</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import Add from "./add.vue";
import Edit from "./edit.vue";
import FormSerach from "@/components/form/FormSerach.vue";
import useTable from "@/hooks/useTable";
import { mapState } from "vuex";
import { message } from "ant-design-vue";
import { systemService } from "@/services";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "用户名称",
          dataIndex: "userName",
          key: "userName",
        },
        {
          title: "邮箱",
          dataIndex: "email",
          key: "email",
        },
        {
          title: "是否超管",
          dataIndex: "isAdmin",
          key: "isAdmin",
        },
        {
          title: "是否禁用",
          dataIndex: "isDisabled",
          key: "isDisabled",
          scopedSlots: { customRender: "isDisabled" },
        },
        {
          title: "登录时间",
          dataIndex: "lastLoginTime",
          key: "lastLoginTime",
        },
        {
          title: "登录IP",
          dataIndex: "lastLoginIp",
          key: "lastLoginIp",
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
        { name: "userName", label: "用户名" },
        { name: "email", label: "邮箱" },
        {
          name: "isAdmin",
          label: "是否超管",
          component: "select",
          props: {
            options: [
              { value: "1", label: "是" },
              { value: "0", label: "否" },
            ],
          },
        },
        {
          name: "isDisabled",
          label: "是否禁用",
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
      list,
      page,
      loading,
      onSerach,
      onChange,
      createModalEvent,
    } = useTable(systemService.getSysUserList);

    // 新增事件
    const onAdd = createModalEvent(Add, { title: "新增用户" });
    // 编辑事件
    const onEdit = createModalEvent(Edit, { title: "编辑用户" });

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
  },
  methods: {
    // 重置密码
    onResetPwd(record) {
      let { userName } = record;
      systemService
        .resetPwd({ userName })
        .then(() => message.success("重置成功"))
        .catch((err) => message.error(_.get(err, "msg", "重置失败")));
    },
    // 锁定账户
    onDisabled(record) {
      let { userName, isDisabled } = record;
      isDisabled = isDisabled == "1" ? "0" : "1";
      systemService
        .lockSysUser({
          userName,
          isDisabled,
        })
        .then(() => {
          record.isDisabled = isDisabled;
          message.success("操作成功");
        })
        .catch((err) => message.error(_.get(err, "msg", "操作失败")));
    },
  },
};
</script>
<style lang="less" scoped></style>
