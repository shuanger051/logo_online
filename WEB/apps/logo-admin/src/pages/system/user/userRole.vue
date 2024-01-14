<template>
  <a-form-model
    ref="formRef"
    :model="formData"
    :label-col="{ span: 5 }"
    :wrapper-col="{ span: 19 }"
  >
    <a-form-model-item label="用户名">
      <a-input disabled readonly :value="record.userName" />
    </a-form-model-item>
    <a-form-item label="用户角色">
      <a-select> </a-select>
    </a-form-item>
  </a-form-model>
</template>
<script>
import { reactive, ref } from "vue";
import { systemService } from "@/services";
import { message } from "ant-design-vue";
export default {
  props: {
    record: {
      type: Object,
      required: true,
    },
    // 刷新
    refresh: {
      type: Function,
    },
  },
  computed: {
    rules() {
      return {};
    },
  },
  setup(props) {
    const { record } = props;
    const formData = reactive({});
    const formRef = ref();

    // 查询所有角色信息
    systemService.getSysRoleListByPage({
      pageSize: 100,
    });
    // 查询用户角色信息
    systemService
      .getSysUserRoleById({
        id: record.id,
      })
      .then((res) => {
        const { data } = res;
        // 存在数据则赋值展示
        if (data) {
          ["id", "userId", "roleId"].forEach((key) => {
            formData[key] = data[key];
          });
        }
      });

    // 新增
    function saveUserRole() {}

    // 修改
    function updateUserRole() {}

    function onOk() {
      return (
        formRef.value
          // 表单校验
          .validate()
          .then((valid) => {
            if (valid) {
              // 已存在则更新
              if (formData.id) return updateUserRole();
              // 不存在则新增
              else return saveUserRole();
            }
            return Promise.reject();
          })
      );
    }

    return {
      formRef,
      formData,
      onOk,
      onCancel,
    };
  },
};
</script>
