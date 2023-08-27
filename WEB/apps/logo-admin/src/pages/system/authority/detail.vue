<template>
  <a-form-model
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 5 }"
    :wrapper-col="{ span: 19 }"
  >
    <a-form-model-item label="权限名称" prop="permissionName">
      <a-input placeholder="请输入" v-model="formData.permissionName" />
    </a-form-model-item>
    <a-form-model-item label="权限类型" prop="permissionType">
      <a-select
        placeholder="请选择"
        v-model="formData.permissionType"
        :options="DictPermissionType"
      />
    </a-form-model-item>
    <a-form-model-item label="权限分组" prop="permissionGroup">
      <a-input placeholder="请输入" v-model="formData.permissionGroup" />
    </a-form-model-item>
    <a-form-model-item label="权限路径" prop="permissionPath">
      <a-input placeholder="请输入" v-model="formData.permissionPath" />
    </a-form-model-item>
    <a-form-model-item label="权限等级" prop="permissionLevel">
      <a-input placeholder="请输入" v-model="formData.permissionLevel" />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import store from "@/store";
import { ref, reactive } from "vue";
import { mapDictOptions } from "@/store/helpers";
import { systemService } from "@/services";
import { message } from "ant-design-vue";
export default {
  props: {
    action: {
      type: String,
    },
    record: {
      type: Object,
      default: () => ({ parentId: "0" }),
    },
  },
  computed: {
    rules() {
      return {};
    },
    // 权限类型
    DictPermissionType() {
      return mapDictOptions("permissionType")(store.state);
    },
  },
  setup(props) {
    // 判断传入的记录
    const formData = reactive(_.cloneDeep(props.record));
    const formRef = ref();

    // 获取字典项
    store.dispatch("cache/queryDictByKey", {
      keys: ["permissionType"],
    });

    // 新增权限信息
    function save() {
      return systemService
        .saveSysPermission(formData)
        .then(() => message.success("新增成功"))
        .catch((err) =>
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`)
        );
    }

    // 修改权限信息
    function update() {
      systemService
        .updateSysPermissionById(formData)
        .then(() => message.success("修改成功"))
        .catch((err) =>
          message.error(`修改失败：${_.get(err, "msg", "未知错误")}`)
        );
    }

    // expose
    function onOk() {
      formRef.value.validate().then((valid) => {
        if (valid) {
          // 存在id则为新增
          if (formData.id) return update();
          // 其他为修改
          else return save();
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
