<template>
  <a-form-model
    ref="formRef"
    class="edit-form"
    :model="formData"
    :rules="rules"
    :label-col="{ style: { width: '6em', float: 'left' } }"
    :wrapper-col="{ span: 18 }"
  >
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
        <a-form-model-item label="发布时间" prop="contentExt.releaseDate">
          <a-date-picker
            show-time
            placeholder="请选择"
            format="YYYY-MM-DD HH:mm:ss"
            v-model="formData.contentExt.releaseDate"
            style="width: 100%"
          />
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
      <!-- 附件列表 -->
      <a-col :span="24">
        <a-form-model-item label="附件">
          <a-upload
            :remove="doDel"
            :customRequest="doUpload"
            :fileList="formData.contentAttachment"
          >
            <a-button type="link" icon="upload">上传附件</a-button>
          </a-upload>
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
import moment from "moment";
export default {
  components: { quillEditor },
  props: {
    record: {
      type: Object,
      default: () => ({ contentExt: {}, contentAttachment: [] }),
    },
  },
  computed: {
    rules() {
      return {
        channelId: [{ required: true, message: "请输入" }],
        title: [{ required: true, message: "请输入" }],
        shortTitle: [{ required: true, message: "请输入" }],
        author: [{ required: true, message: "请输入" }],
        releaseDate: [{ required: true, message: "请选择" }],
        isRecommend: [{ required: true, message: "请输入" }],
        description: [{ required: true, message: "请输入" }],
        origin: [{ required: true, message: "请输入" }],
        originUrl: [{ required: true, message: "请输入" }],
      };
    },
  },
  setup(props) {
    // 获取表单默认值
    const { list = [], contentCheck, ...record } = props.record;
    const formRef = ref();
    const detail = _.cloneDeep(record);
    // 附件处理
    detail.contentAttachment = list.map(setAttachmentAttr);
    const formData = reactive(detail);

    // 设置附件属性
    function setAttachmentAttr(item, uid) {
      if (!item.filename) item.filename = item.fileName;
      item.uid = item.id || uid;
      item.name = item.filename;
      return item;
    }

    // 新增
    function saveContent(data) {
      return afficheService
        .saveContent(data)
        .then(() => {
          message.success("新增成功");
        })
        .catch((err) => {
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`);
          return Promise.reject();
        });
    }

    // 编辑
    function updateContent(data) {
      return afficheService
        .updateContentById(data)
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
              // 处理日期格式
              let data = _.cloneDeep(formData);
              let { contentExt } = data;
              contentExt.releaseDate = moment(contentExt.releaseDate).format(
                "YYYY-MM-DD HH:mm:ss"
              );

              // 有id则为更新
              if (formData.id) return updateContent(data);
              // 其他为新增
              else return saveContent(data);
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
      setAttachmentAttr,
    };
  },
  methods: {
    // 删除附件
    doDel(file) {
      console.log(file);
      let { contentAttachment: list } = this.formData;
      this.formData.contentAttachment = list.filter(
        (item) => item.uid != file.uid
      );
    },
    // 上传
    doUpload(evt) {
      const formData = new FormData();
      formData.append("file", evt.file);
      return (
        afficheService
          // 上传附件
          .uploadContentAttachment(formData)
          .then((res) => {
            const { contentAttachment: list } = this.formData;
            const uid = Math.random().toString(32).slice(2);
            const data = this.setAttachmentAttr(res.data, uid);
            this.formData.contentAttachment = list.concat(data);
            evt.onSuccess(data, evt);
          })
          .catch((err) => {
            console.error(err);
            evt.onError(err, evt);
          })
      );
    },
  },
};
</script>
<style lang="less" scoped>
.edit-form {
  :deep(.ql-editor) {
    height: 240px;
  }
}
</style>
