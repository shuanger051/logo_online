<template>
  <a-form-model
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 6 }"
    :wrapper-col="{ span: 18 }"
  >
    暂未完成
    <!-- <a-form-model-item label="模块ID" prop="modelId">
      <a-input placeholder="请输入" v-model="formData.modelId" />
    </a-form-model-item>
    <a-form-model-item label="父级ID" prop="parentId">
      <a-input placeholder="请输入" v-model="formData.parentId" />
    </a-form-model-item>
    <a-form-model-item label="模块名称" prop="name">
      <a-input placeholder="请输入" v-model="formData.name" />
    </a-form-model-item>
    <a-form-model-item label="排序编号" prop="orderNo">
      <a-input placeholder="请输入" v-model="formData.orderNo" />
    </a-form-model-item>
    <a-form-model-item label="是否显示" prop="isDisplay">
      <a-input placeholder="请输入" v-model="formData.isDisplay" />
    </a-form-model-item>
    <a-form-model-item label="模块描述" prop="description">
      <a-input placeholder="请输入" v-model="formData.description" />
    </a-form-model-item>
    <a-form-model-item label="访问级别" prop="commentControl">
      <a-input placeholder="请输入" v-model="formData.commentControl" />
    </a-form-model-item>
    <a-form-model-item label="是否开放" prop="allowUpdown">
      <a-input placeholder="请输入" v-model="formData.allowUpdown" />
    </a-form-model-item>
    <a-form-model-item label="模块路径" prop="channelPath">
      <a-input placeholder="请输入" v-model="formData.channelPath" />
    </a-form-model-item> -->
  </a-form-model>
</template>

<script>
import { reactive, ref } from "vue";
import { afficheService } from "@/services";
import { message } from "ant-design-vue";
export default {
  props: {
    record: {
      type: Object,
    },
  },
  computed: {
    rules() {
      return {
        modelId: [{ required: true, message: "请输入" }],
        parentId: [{ required: true, message: "请输入" }],
        name: [{ required: true, message: "请输入" }],
        orderNo: [{ required: true, message: "请输入" }],
        isDisplay: [{ required: true, message: "请输入" }],
        description: [{ required: true, message: "请输入" }],
        commentControl: [{ required: true, message: "请输入" }],
        allowUpdown: [{ required: true, message: "请输入" }],
        channelPath: [{ required: true, message: "请输入" }],
      };
    },
  },
  setup(props) {
    const { record = {} } = props;
    const formRef = ref();
    const formData = reactive(record);

    // 新增
    function saveChannel() {
      return afficheService
        .saveChannel(formData)
        .then(() => {
          message.success("新增成功");
        })
        .catch((err) => {
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`);
          return Promise.reject();
        });
    }

    // 编辑
    function updateChannel() {
      return afficheService
        .updateChannel(formData)
        .then(() => {
          message.success("保存成功");
        })
        .catch((err) => {
          message.error(err?.msg || "保存失败");
          return Promise.reject();
        });
    }

    // expose：modal 确认
    function onOk() {
      return (
        formRef.value
          // 表单校验
          .validate()
          .then((valid) => {
            if (valid) {
              // 有id则为更新
              if (record.id) return updateChannel();
              // 其他为新增
              else return saveChannel();
            } else return Promise.reject();
          })
      );
    }

    return {
      formData,
      formRef,
      // method
      onOk,
    };
  },
};
</script>
