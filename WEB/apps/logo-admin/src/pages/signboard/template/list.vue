<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <a-form layout="inline" class="serach-form" :model="formData">
      <a-form-item label="模板名称" name="tempName">
        <a-input v-model="formData.tempName" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="模板风格" name="style">
        <a-select
          v-model="formStyle"
          style="width: 200px"
          allowClear
          mode="multiple"
          :maxTagCount="1"
          @change="changeStyle"
          placeholder="请选择"
        >
          <a-select-option
            v-for="(item, index) in styleMap"
            :key="`style-${index}`"
            :value="item.value"
            >{{ item.label }}</a-select-option
          >
        </a-select>
      </a-form-item>
      <a-form-item label="是否发布" name="releaseStatus">
        <a-select
          v-model="formData.releaseStatus"
          style="width: 120px"
          allowClear
          placeholder="请选择"
        >
          <a-select-option value="1">是</a-select-option>
          <a-select-option value="2">否</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
    <!-- 操作栏 -->
    <div class="serach-action-bar">
      <a-space>
        <a-button type="primary" @click="onSerach">查询</a-button>
        <a-button type="danger" @click="onReset">重置</a-button>
      </a-space>
      <a-space>
        <a-button type="primary" @click="onAdd">新增模板</a-button>
      </a-space>
    </div>
    <!-- 结果列表 -->
    <a-table
      rowKey="id"
      size="small"
      :bordered="true"
      :data-source="list"
      :pagination="page"
      :columns="columns"
      @change="onChange"
    >
      <template slot="releaseStatus" slot-scope="text, record, index">
        {{ text == "1" ? "已发布" : "未发布" }}
      </template>
      <template slot="shortImage" slot-scope="text, record, index">
        <img
          v-if="!!getImageSrc(record)"
          width="100px"
          height="100px"
          :style="{objectFit: 'cover'}"
          :src="getImageSrc(record)"
        />
        <span v-else>无缩略图</span>
      </template>
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record, index">
        <a-button type="link" size="small" @click="onEdit({ record })"
          >编辑</a-button
        >
        <a-button type="link" size="small" @click="onDel({ record, index })"
          >删除</a-button
        >
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import useTable from "@/hooks/useTable";
import { ref } from "vue";
import { mapState } from "vuex";
import { signboardService } from "@/services";
const styleMap = [
  { value: "1", label: "古典风" },
  { value: "2", label: "现代风" },
  { value: "3", label: "商务风" },
  { value: "4", label: "极简风" },
  { value: "5", label: "欧式风" },
  { value: "6", label: "美式风" },
  { value: "7", label: "原木风" },
  { value: "8", label: "工业风" },
  { value: "9", label: "田园风" },
];

export default {
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "模板名",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "模板风格",
          dataIndex: "style",
          key: "style",
          customRender: (text) => {
            if (text.length) {
              const styleLists = text.split(",");
              const nameLists = styleLists.map((key) => {
                return styleMap.find((s) => key == s.value).label;
              });
              return <span>{nameLists.join(",")}</span>;
            } else {
              return null;
            }
          },
        },
        {
          title: "是否发布",
          dataIndex: "releaseStatus",
          key: "releaseStatus",
          scopedSlots: { customRender: "releaseStatus" },
        },
        {
          title: "缩略图",
          key: "shortImage",
          align: 'center',
          scopedSlots: { customRender: "shortImage" },
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      formData,
      list,
      page,
      onSerach,
      onReset,
      onChange,
      createDelEvent,
      createModalEvent,
    } = useTable(signboardService.getTemplateListByPage);

    const formStyle = ref([]);

    // 删除事件
    const onDel = createDelEvent(async ({ record, index }) => {
      const data = await signboardService.deleteTemplateByID(
        _.pick(record, ["id"])
      );
      list.value.splice(index, 1);
      return data;
    });
    const changeStyle = (v) => {
      formData.style = v.join(",");
    };

    return {
      formData,
      list,
      page,
      onDel,
      onSerach,
      onReset: () => {
        formStyle.value = [];
        return onReset;
      },
      onChange,
      styleMap,
      formStyle,
      changeStyle,
    };
  },
  methods: {
    getImageSrc(record) {
      let src = "";
      let json;
      let isDev = process.env.NODE_ENV === "development";
      let prefix = process.env.VUE_APP_API_BASE_URL;
      try {
        json = JSON.parse(record.domItem);
        // src = isDev
        //   ? '/api/logo' + json.cover_image_url
        //   : prefix + '/logo/'+ json.cover_image_url;
        src = '/api/logo' + json.cover_image_url
      } catch (e) {
        console.log(e);
      }
      return src;
    },
    // 重置密码
    onEdit({ record }) {
      this.$router.push(`/addTemplate/${record.id}`);
    },
    onAdd() {
      this.$router.push(`/addTemplate`);
    },
  },
  created() {
    this.onSerach();
  },
};
</script>
<style lang="less" scoped></style>
