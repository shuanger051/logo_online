<template>
  <a-modal v-model="show" title="图片库" width="700px" class="upload-dialog">
    <div>
      <a-card>
        <template #extra>
          <a-upload name="file" :customRequest="upload">
            <a-button> <a-icon type="upload" /> 上传 </a-button>
          </a-upload>
        </template>

        <a-list :pagination="pagination" :grid="{ gutter: 12, column: 4 }">
          <a-list-item
            @click="selectItem(item.url)"
            v-for="(item, i) in list"
            :key="`img-${i}`"
          >
            <a-card hoverable>
              <div
                :style="{
                  backgroundImage: `url(${resolveImgUrl(item.url, true)})`,
                  backgroundSize: 'contain',
                  backgroundPosition: 'center',
                  backgroundRepeat: 'no-repeat',
                  height: '142px',
                }"
              ></div>
            </a-card>
          </a-list-item>
        </a-list>
      </a-card>
    </div>
  </a-modal>
</template>
<script>
import { appGetMaterial, appUploadMaterialAttachment } from "core/api/";
import { resolveImgUrl } from "core/support/imgUrl";

export default {
  data() {
    return {
      show: false,
      list: [],
      pagination: {},
    };
  },
  methods: {
    resolveImgUrl,
    async upload(evt) {
      const form = new FormData();
      form.append("file", evt.file);
      const info = await appUploadMaterialAttachment(form);
      this.selectItem(info.data.urlPath);
    },
    async getList() {
      const res = await appGetMaterial({
        pageNum: this.pagination.current,
      });
      if (res) {
        const data = res.data;
        this.pagination.total = data.total;
        this.list = data.list.map((item) => {
          return {
            id: item.id,
            url: item.urlPath,
          };
        });
      }
    },
    selectItem(url) {
      this.$emit("selectHandler", resolveImgUrl(url, true));
      this.showHandler(false);
    },
    reset() {
      this.list = [];
      this.pagination = {
        onChange: (page) => {
          this.pagination.current = page;
          this.getList();
        },
        current: 1,
        total: 0,
      };
    },
    showHandler(flag) {
      this.show = !!flag;
      if (flag) {
        this.getList();
      } else {
        this.reset();
      }
    },
  },
  created() {
    this.reset();
  },
};
</script>
<style scoped lang="scss">
.upload-dialog {
  :deep(.ant-card-body) {
    max-height: 400px;
    overflow-y: scroll;
  }
  :deep(.ant-col) {
    padding: 0px 6px;
  }
  :deep(.ant-list-pagination) {
    clear: both;
  }
}
</style>
