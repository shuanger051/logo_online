import Vue from "vue";
import dayjs from "dayjs";

// 日期格式化
Vue.filter("date", (val, format = "YYYY-MM-DD hh:mm:ss") =>
  dayjs(val).format(format)
);

// 字典项映射
Vue.filter("dict", (val, dict) => {
  return dict[val];
});
