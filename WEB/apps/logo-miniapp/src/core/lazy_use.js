import Vue from "vue";
import {
  Panel,
  Button,
  Grid,
  GridItem,
  Icon,
  NavBar,
  List,
  Cell,
  CellGroup,
  Collapse,
  CollapseItem,
  Image,
  Form,
  Field,
  Popup,
  Toast,
  Picker,
  Stepper,
  Uploader,
  Pagination,
  Dialog,
  Empty,
  Divider,
  Overlay,
  Sidebar,
  SidebarItem,
  Checkbox,
  CheckboxGroup,
} from "vant";
import SubmitBar from "@/components/SubmitBar";
import FieldPicker from "@/components/FieldPicker";

Vue.use(Panel);
Vue.use(Button);
Vue.use(Grid);
Vue.use(GridItem);
Vue.use(Icon);
Vue.use(NavBar);
Vue.use(List);
Vue.use(Cell);
Vue.use(CellGroup);
Vue.use(Image);
Vue.use(Form);
Vue.use(Field);
Vue.use(Popup);
Vue.use(Toast);
Vue.use(Stepper);
Vue.use(Picker);
Vue.use(Uploader);
Vue.use(Pagination);
Vue.use(Dialog);
Vue.use(Empty);
Vue.use(Divider);
Vue.use(Overlay);
Vue.use(Sidebar);
Vue.use(SidebarItem);
Vue.use(Checkbox);
Vue.use(CheckboxGroup);
Vue.use(Collapse);
Vue.use(CollapseItem);

// 自定义组件
Vue.component("submit-bar", SubmitBar);
Vue.component("field-picker", FieldPicker);
