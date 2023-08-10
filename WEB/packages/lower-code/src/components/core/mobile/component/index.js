import MobileSelect from './MobileSelect.vue'
import { ColorPicker,Button } from 'element-ui'
import { Icon } from '@iconify/vue2';
import 'core/mobile/style/index.scss'
import 'tinymce/tinymce'
import 'tinymce/themes/silver/theme.min.js'
import 'tinymce/skins/ui/oxide/skin.min.css'
import 'tinymce/icons/default'
import Editor from '@tinymce/tinymce-vue'

export default {
   [ColorPicker.name]:  ColorPicker,
   [Button.name]: Button,
   'icon-fa': Icon,
   'mobile-select': MobileSelect,
   'tinymce-editor': Editor
  }
