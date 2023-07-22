<template>
  <!-- 新增编辑弹窗 -->
  <a-form layout="vertical">
    <a-form-item label="用户名">
      <a-input
        placeholder="请输入"
        v-model="formData.userName"
        :disabled="isEdit"
      />
    </a-form-item>
    <a-form-item label="邮箱">
      <a-input placeholder="请输入" v-model="formData.email" />
    </a-form-item>
    <!-- 编辑状态不展示密码 -->
    <a-form-item label="密码" v-if="!isEdit">
      <a-input placeholder="请输入" v-model="formData.password" />
    </a-form-item>
  </a-form>
</template>
<script>
import _ from "lodash";
import { reactive } from "vue";
import { message } from "ant-design-vue";
import { systemService } from "@/services";
export default {
  props: {
    record: {
      type: Object,
    },
  },
  computed: {
    // 是否编辑状态
    isEdit() {
      return !!_.get(this.record, "id");
    },
  },
  setup(props) {
    // 判断传入的记录
    const { record = {} } = props;
    const formData = reactive(record);

    // expose：确认
    function onOk() {
      return systemService
        .saveSysUser(formData)
        .then((res) => {
          if (res.code === "0") message.success("新增成功");
          // 新增失败
          else return Promise.reject(res);
        })
        .catch((err) => {
          console.log(err);
          message.error(err?.msg || "新增失败");
          return Promise.reject();
        });
    }

    return {
      formData,
      onOk,
    };
  },
};
</script>
