<template>
  <a-form-model
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 5 }"
    :wrapper-col="{ span: 19 }"
  >
    <a-form-model-item label="角色名称" prop="roleName">
      <a-input placeholder="请输入" v-model="formData.roleName" />
    </a-form-model-item>
    <a-form-model-item label="角色等级" prop="roleLevel">
      <a-input placeholder="请输入" v-model="formData.roleLevel" />
    </a-form-model-item>
    <a-form-model-item label="角色描述" prop="description">
      <a-input placeholder="请输入" v-model="formData.description" />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { ref, reactive } from "vue";
import { systemService } from "@/services";
import { message } from "ant-design-vue";
export default {
  props: {
    action: {
      type: String,
    },
    record: {
      type: Object,
      default: () => ({}),
    },
  },
  computed: {
    rules() {
      return {};
    },
  },
  setup(props) {
    // 判断传入的记录
    const formData = reactive(_.cloneDeep(props.record));
    const formRef = ref();

    // 新增角色
    function save() {
      return systemService
        .saveSysRole(formData)
        .then(() => message.success("新增成功"))
        .catch((err) =>
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`)
        );
    }

    // 修改角色
    function update() {
      systemService
        .updateSysRoleById(formData)
        .then(() => message.success("修改成功"))
        .catch((err) =>
          message.error(`修改失败：${_.get(err, "msg", "未知错误")}`)
        );
    }

    // expose
    function onOk() {
      formRef.value.validate().then((valid) => {
        if (valid) {
          // 存在id则为修改
          if (formData.id) return update();
          // 其他为新增
          else save();
        }
        // 未通过
        else return Promise.reject();
      });
    }

    return {
      formData,
      formRef,
      onOk,
    };
  },
};
</script>
