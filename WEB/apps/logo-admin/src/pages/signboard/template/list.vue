<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <form-serach :fields="serachFields" @serach="onSerach">
      <a-button type="primary" @click="onAdd">新增模板</a-button>
    </form-serach>
    <!-- 结果列表 -->
    <a-table
      rowKey="id"
      size="small"
      :loading="loading"
      :bordered="true"
      :data-source="list"
      :pagination="page"
      :columns="columns"
      @change="onChange"
    >
      <template slot="releaseStatus" slot-scope="text">
        {{ text == "1" ? "已发布" : "未发布" }}
      </template>
      <template slot="shortImage" slot-scope="text, record">
        <img
          v-if="!!getImageSrc(record)"
          width="100px"
          height="100px"
          :style="{ objectFit: 'contain' }"
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
          >删除</a-button>
          <a-button type="link" size="small" @click="onPublish({ record, index })"
          >{{record.releaseStatus == '1' ? '取消发布': '发布'}}</a-button>
        
      </template>
    </a-table>
  </div>
</template>
<script>
import useTable from "@/hooks/useTable";
import { ref } from "vue";
import { mapState } from "vuex";
import { Modal,message } from "ant-design-vue";

import { signboardService,systemService } from "@/services";
import FormSerach from "@/components/form/FormSerach.vue";


export default {
  components: { FormSerach },
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
                return this.styleMap.find((s) => key == s.value).label;
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
          align: "center",
          scopedSlots: { customRender: "shortImage" },
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
    // 查询字段
    serachFields() {
      return [
        { name: "tempName", label: "模版名称" },
        {
          name: "style",
          label: "模版风格",
          component: "select",
          props: {
            options: this.styleMap,
          },
        },
        {
          name: "releaseStatus",
          label: "发布状态",
          component: "select",
          props: {
            options: [
              { value: "1", label: "已发布" },
              { value: "2", label: "未发布" },
            ],
          },
        },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      loading,
      list,
      page,
      onSerach,
      onChange,
      createDelEvent,
    } = useTable(signboardService.getTemplateListByPage);

    const formStyle = ref([]);
    const formData = ref({});
    const styleMap = ref([]);
    // 删除事件
    const onDel = createDelEvent(async ({ record, index }) => {
      const data = await signboardService.deleteTemplateByID(
        _.pick(record, ["id"])
      );
      list.value.splice(index, 1);
      return data;
    });
    const onPublish = ({record}) => {
      Modal.confirm({
        content: "是否"+ record.releaseStatus == "1" ? '取消发布': "发布",
        okText: "确定",
        onOk: () => { 
          signboardService.updateTemplateStatusById({
            id: record.id,
            releaseStatus: record.releaseStatus == "1" ? "2" : "1" 
          })
          .then(() => {message.success("更改成功"); onSerach()})
          .catch(() => message.error("更改失败"))
        }
      });
    }
    systemService
        .getItemsByDictKeyInDB({ dictKey: 'style' })
        .then((res) => (styleMap.value = res.data.map((item) => {
          return {
            value: item.itemKey,
            label: item.itemValue
          }
        })))
    const changeStyle = (v) => {
      formData.style = v.join(",");
    };
    return {
      formData,
      loading,
      list,
      page,
      onDel,
      onSerach,
      onChange,
      styleMap,
      formStyle,
      onPublish
    };
  },
  methods: {
    getImageSrc(record) {
      let src = "";
      let json;
      let isDev = process.env.NODE_ENV === "development";
      try {
        json = JSON.parse(record.domItem);
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
