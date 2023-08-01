<template>
  <a-form-model
    ref="formRef"
    class="edit-form"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 6 }"
    :wrapper-col="{ span: 18 }"
  >
    <!-- 富文本编辑器 -->
    <a-row>
      <a-col :span="12">
        <a-form-model-item label="所属栏目" prop="channelId">
          <a-input placeholder="请输入" v-model="formData.channelId" />
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
        <a-form-model-item label="标题" prop="contentExt.title">
          <a-input placeholder="请输入" v-model="formData.contentExt.title" />
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
        <a-form-model-item label="副标题" prop="contentExt.shortTitle">
          <a-input
            placeholder="请输入"
            v-model="formData.contentExt.shortTitle"
          />
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
        <a-form-model-item label="作者" prop="contentExt.author">
          <a-input placeholder="请输入" v-model="formData.contentExt.author" />
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
        <a-form-model-item label="是否推荐" prop="isRecommend">
          <a-select placeholder="请选择" v-model="formData.isRecommend">
            <a-select-option value="0">否</a-select-option>
            <a-select-option value="1">是</a-select-option>
          </a-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
        <a-form-model-item label="摘要" prop="contentExt.description">
          <a-input
            placeholder="请输入"
            v-model="formData.contentExt.description"
          />
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
        <a-form-model-item label="来源" prop="contentExt.origin">
          <a-input placeholder="请输入" v-model="formData.contentExt.origin" />
        </a-form-model-item>
      </a-col>
      <a-col :span="12">
        <a-form-model-item label="来源地址" prop="contentExt.originUrl">
          <a-input
            placeholder="请输入"
            v-model="formData.contentExt.originUrl"
          />
        </a-form-model-item>
      </a-col>
    </a-row>
    <!-- 富文本编辑器 -->
    <quill-editor
      :content="formData.contentExt.content"
      @change="onEditorChange"
    />
  </a-form-model>
</template>

<script>
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";

import { quillEditor } from "vue-quill-editor";
import { reactive, ref } from "vue";
import { afficheService } from "@/services";
import { message } from "ant-design-vue";
export default {
  components: { quillEditor },
  props: {
    record: {
      type: Object,
      default: () => ({ contentExt: {} }),
    },
  },
  computed: {
    rules() {
      return {
        channelId: [{ required: true, message: "请输入" }],
        title: [{ required: true, message: "请输入" }],
        shortTitle: [{ required: true, message: "请输入" }],
        author: [{ required: true, message: "请输入" }],
        isRecommend: [{ required: true, message: "请输入" }],
        description: [{ required: true, message: "请输入" }],
        origin: [{ required: true, message: "请输入" }],
        originUrl: [{ required: true, message: "请输入" }],
      };
    },
  },
  setup(props) {
    // 获取表单默认值
    const defVal = _.cloneDeep(props.record);
    const formRef = ref();
    const formData = reactive(defVal);

    // 新增
    function saveContent() {
      return afficheService
        .saveContent(formData)
        .then(() => {
          message.success("新增成功");
        })
        .catch((err) => {
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`);
          return Promise.reject();
        });
    }

    // 编辑
    function updateContent() {
      return afficheService
        .updateContentById(formData)
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
              if (formData.id) return updateContent();
              // 其他为新增
              else return saveContent();
            } else return Promise.reject();
          })
      );
    }

    function onEditorChange(evt) {
      formData.contentExt.content = evt.html;
    }

    return {
      formData,
      formRef,
      // method
      onOk,
      onEditorChange,
    };
  },
};
</script>

<style lang="less" scoped>
.edit-form {
  :deep(.ql-editor) {
    height: 280px;
  }
}
</style>
