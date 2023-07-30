<template>
  <!-- 新增/编辑/弹窗 -->
  <a-form-model
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 5 }"
    :wrapper-col="{ span: 19 }"
  >
    <a-form-model-item label="用户名" prop="userName">
      <a-input
        placeholder="请输入"
        v-model="formData.userName"
        :disabled="true"
      />
    </a-form-model-item>
    <a-form-model-item label="邮箱" prop="email">
      <a-input placeholder="请输入" v-model="formData.email" />
    </a-form-model-item>
    <a-form-model-item label="是否激活" prop="activation">
      <a-input placeholder="请输入" v-model="formData.activation" />
    </a-form-model-item>
    <a-form-model-item label="激活码" prop="activationCode">
      <a-input placeholder="请输入" v-model="formData.activationCode" />
    </a-form-model-item>
    <a-form-model-item label="是否超管" prop="isAdmin">
      <a-input placeholder="请输入" v-model="formData.isAdmin" />
    </a-form-model-item>
    <a-form-model-item label="是否禁用" prop="isDisabled">
      <a-input placeholder="请输入" v-model="formData.isDisabled" />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { reactive, ref } from "vue";
import { message } from "ant-design-vue";
import { systemService } from "@/services";
export default {
  props: {
    record: {
      type: Object,
      required: true,
    },
  },
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
  setup(props) {
    // 判断传入的记录
    const formData = reactive(props.record);
    const formRef = ref();

    // 编辑用户
    function updateSysUser() {
      systemService
        .updateSysUser(formData)
        .then(() => {
          message.success("保存成功");
        })
        .catch((err) => {
          message.error(err?.msg || "保存失败");
          return Promise.reject();
        });
    }

    // expose：确认
    function onOk() {
      // 表单校验
      return (
        formRef.value
          // 表单校验
          .validate()
          .then((valid) => {
            // 通过
            if (valid) return updateSysUser();
            // 未通过
            else return Promise.reject();
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
