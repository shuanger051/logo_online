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
        :disabled="isEdit"
      />
    </a-form-model-item>
    <a-form-model-item label="邮箱" prop="email">
      <a-input placeholder="请输入" v-model="formData.email" />
    </a-form-model-item>
    <!-- 编辑状态不展示密码 -->
    <a-form-model-item label="密码" v-if="!isEdit" prop="password">
      <a-input placeholder="请输入" v-model="formData.password" />
    </a-form-model-item>
    <!-- 编辑状态下展示字段 -->
    <template v-if="isEdit">
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
    </template>
  </a-form-model>
</template>
<script>
import { reactive, ref } from "vue";
import { message } from "ant-design-vue";
import { systemService } from "@/services";
export default {
  props: {
    action: {
      type: String,
    },
    record: {
      type: Object,
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
    // 是否编辑状态
    isEdit() {
      return !!_.get(this.record, "id");
    },
  },
  setup(props, ctx) {
    // 判断传入的记录
    const { record = {} } = props;
    const formData = reactive(record);
    const formRef = ref();

    // 新增用户
    function saveSysUser() {
      return systemService
        .saveSysUser(formData)
        .then((res) => {
          message.success("新增成功");
        })
        .catch((err) => {
          message.error(err?.msg || "新增失败");
          return Promise.reject();
        });
    }

    // 编辑用户
    function updateSysUser() {
      systemService
        .updateSysUser(formData)
        .then((res) => {
          message.success("保存成功");
        })
        .catch((err) => {
          message.error(err?.msg || "保存失败");
        });
    }

    // 重置密码
    function resetPwd() {
      return systemService
        .resetPwd(formData)
        .then(() => message.success("重置成功"))
        .catch((err) => {
          message.error(err?.msg || "重置失败");
        });
    }

    // expose：确认
    function onOk() {
      // 表单校验
      return (
        formRef.value
          .validate()
          // 校验结果
          .then((valid) => {
            if (valid) {
              switch (props.action) {
                case "add":
                  return saveSysUser();
                case "edit":
                  return updateSysUser();
                case "restPwd":
                  return resetPwd();
                default:
                  break;
              }
            }
            // 未通过
            else return Promise.reject();
          })
      );
    }

    return {
      formRef,
      formData,
      onOk,
    };
  },
};
</script>
