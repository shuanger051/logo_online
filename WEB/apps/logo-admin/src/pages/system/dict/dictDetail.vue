<template>
  <a-form-model
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 5 }"
    :wrapper-col="{ span: 19 }"
  >
    <a-form-model-item label="条目键值" prop="dictKey">
      <a-input v-model="formData.dictKey" placeholder="请输入" />
    </a-form-model-item>
    <a-form-model-item label="条目名称" prop="dictName">
      <a-input v-model="formData.dictName" placeholder="请输入" />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { ref, reactive } from "vue";
import { systemService } from "@/services";
import { message } from "ant-design-vue";
export default {
  props: {
    record: {
      type: Object,
      default: () => ({}),
    },
  },
  computed: {
    rules() {
      return {
        dictName: [{ reqiured: true, message: "请输入" }],
        dictKey: [{ reqiured: true, message: "请输入" }],
      };
    },
  },
  setup(props) {
    // 判断传入的记录
    const formData = reactive(_.cloneDeep(props.record));
    const formRef = ref();

    // 新增字典项
    function saveDict() {
      return systemService
        .saveDict(formData)
        .then(() => message.success("新增成功"))
        .catch((err) =>
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`)
        );
    }

    function updateDictById() {
      systemService
        .updateDictById(formData)
        .then(() => message.success("修改成功"))
        .catch((err) =>
          message.error(`修改失败：${_.get(err, "msg", "未知错误")}`)
        );
    }

    // expose
    function onOk() {
      formRef.value.validate().then((valid) => {
        if (valid) {
          // 修改
          if (formData.id) return updateDictById();
          // 新增
          else return saveDict();
        }
        // 校验未通过
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
