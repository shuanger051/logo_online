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
  Image,
  Form,
  Field,
  Popup,
  Toast,
  Picker,
  Stepper,
  Uploader
} from "vant";
import SubmitBar from "@/components/SubmitBar";

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


Vue.component("submit-bar", SubmitBar);
