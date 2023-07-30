<template>
  <a-form-model
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 5 }"
    :wrapper-col="{ span: 19 }"
  >
    <a-form-model-item label="用户名" prop="userName">
      <a-input placeholder="请输入" v-model="formData.userName" />
    </a-form-model-item>
    <a-form-model-item label="邮箱" prop="email">
      <a-input placeholder="请输入" v-model="formData.email" />
    </a-form-model-item>
    <a-form-model-item label="密码" prop="password">
      <a-input placeholder="请输入" v-model="formData.password" />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { reactive, ref } from "vue";
import { systemService } from "@/services";
import { message } from "ant-design-vue";
export default {
  computed: {
    // 表单校验规则
    rules() {
      return {
        email: [{ required: true, message: "请输入" }],
        userName: [{ required: true, message: "请输入" }],
        password: [{ required: true, message: "请输入" }],
      };
    },
  },
  setup() {
    const formData = reactive({});
    const formRef = ref();

    function saveSysUser() {
      return systemService
        .saveSysUser(formData)
        .then(() => {
          message.success("新增成功");
        })
        .catch((err) => {
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`);
          return Promise.reject();
        });
    }

    // @expose modal confirm
    function onOk() {
      return (
        formRef.value
          // 表单校验
          .validate()
          .then((valid) => {
            if (valid) return saveSysUser();
            return Promise.reject();
          })
      );
    }

    return {
      // data
      formRef,
      formData,
      // event
      onOk,
    };
  },
};
</script>
