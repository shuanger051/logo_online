<template>
  <a-form-model ref="formRef" :model="formData" :rules="rules">
    <a-form-model-item prop="checkOpinion">
      <a-textarea
        v-model="formData.checkOpinion"
        placeholder="请输入审核意见"
        :auto-size="{ minRows: 4, maxRows: 6 }"
      />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { reactive, ref } from "vue";
import { message } from "ant-design-vue";
import { afficheService } from "@/services";

export default {
  props: {
    record: {
      type: Object,
      required: true,
    },
  },
  computed: {
    rules() {
      return {
        checkOpinion: [{ required: true, message: "审核意见必填" }],
      };
    },
  },
  setup(props) {
    const { record } = props;
    const formData = reactive({});
    const formRef = ref();

    // 审核
    function auditContent(isRejected) {
      return (
        formRef.value
          // 表单校验
          .validate()
          // 提交审核意见
          .then(() =>
            afficheService
              .auditContent({
                isRejected,
                contentId: record.id,
                checkOpinion: formData.checkOpinion,
              })
              .then(() => {
                message.success("操作成功");
              })
              .catch((err) => {
                message.error(`操作失败：${_.get(err, "msg", "未知错误")}`);
                return Promise.reject();
              })
          )
      );
    }

    // 审核通过
    function onOk() {
      return auditContent("0");
    }

    // 审核退回
    function onCancel() {
      return auditContent("1");
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
