<template>
  <a-form-model ref="formRef" :model="formData" :rules="rules">
    <a-form-model-item prop="checkInfo">
      <a-textarea
        v-model="formData.checkInfo"
        :auto-size="{ minRows: 4, maxRows: 6 }"
        placeholder="请输入审核意见"
      />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { reactive, ref } from "vue";
import { message } from "ant-design-vue";
import { shopService } from "@/services";
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
      return {
        checkInfo: [{ required: true, message: "审核意见必填" }],
      };
    },
  },
  setup(props) {
    const { record } = props;
    const formData = reactive({});
    const formRef = ref();

    // 审核
    function auditContent(isFilings, checkInfo) {
      return shopService
        .updateShopsFilingsStatusAPI({
          isFilings,
          checkInfo,
          id: record.id,
        })
        .then(() => {
          message.success("操作成功");
          props?.refresh()
        })
        .catch((err) => {
          message.error(`操作失败：${_.get(err, "msg", "未知错误")}`);
          return Promise.reject();
        });
    }

    // 审核通过
    function onOk() {
      // 重置表单
      formRef.value.resetFields();
      return auditContent("2");
    }

    // 审核退回
    function onCancel() {
      return (
        formRef.value
          // 表单校验
          .validate()
          // 提交审核意见
          .then(() => auditContent("3", formData.checkInfo))
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
