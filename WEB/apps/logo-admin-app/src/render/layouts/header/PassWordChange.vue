<template>
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
        readonly
        disabled
      />
    </a-form-model-item>
    <a-form-model-item label="旧密码" prop="email">
      <a-input
        placeholder="请输入旧密码"
        type="password"
        v-model="formData.oldPassword"
      />
    </a-form-model-item>
    <a-form-model-item label="新密码" prop="password">
      <a-input
        placeholder="请输入新密码"
        type="password"
        v-model="formData.newPassword"
      />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { reactive, ref } from "vue";
import { message, Modal } from "ant-design-vue";
export default {
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  computed: {
    // 表单校验规则
    rules() {
      return {
        oldPassword: [{ required: true, message: "请输入" }],
        newPassword: [{ required: true, message: "请输入" }],
      };
    },
  },
  setup(props) {
    const { user } = props;
    const formData = reactive({ userName: user.userName });
    const formRef = ref();

    function doSubmit() {

    }

    // @expose modal confirm
    function onOk() {
      return (
        formRef.value
          // 表单校验
          .validate()
          .then((valid) => {
            if (valid) return doSubmit();
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
