<template>
  <a-form-model
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="{ span: 5 }"
    :wrapper-col="{ span: 19 }"
  >
    <a-form-model-item label="子项值" prop="itemValue">
      <a-input v-model="formData.itemValue" placeholder="请输入" />
    </a-form-model-item>
    <a-form-model-item label="子项key" prop="itemKey">
      <a-input v-model="formData.itemKey" placeholder="请输入" />
    </a-form-model-item>
  </a-form-model>
</template>
<script>
import { ref, reactive } from "vue";
import { systemService } from "@/services";
import { message } from "ant-design-vue";
export default {
  props: {
    dictKey: String,
    record: {
      type: Object,
      default: () => ({}),
    },
  },
  computed: {
    rules() {
      return {
        itemValue: [{ reqiured: true, message: "请输入" }],
        itemKey: [{ reqiured: true, message: "请输入" }],
      };
    },
  },
  setup(props) {
    // 判断传入的记录
    const { record, dictKey } = props;
    const formData = reactive(_.extend({}, record, { dictKey }));
    const formRef = ref();

    // 新增字典项
    function saveDictItem() {
      return systemService
        .saveDictItem(formData)
        .then(() => message.success("新增成功"))
        .catch((err) =>
          message.error(`新增失败：${_.get(err, "msg", "未知错误")}`)
        );
    }

    function updateDictItemById() {
      systemService
        .updateDictItemById(formData)
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
          if (record.itemKey) return updateDictItemById();
          // 新增
          else return saveDictItem();
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
