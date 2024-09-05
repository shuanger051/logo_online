
import 'core/pc/style/index.scss'
import 'tinymce/tinymce'
import 'tinymce/themes/silver/theme.min.js'
import 'tinymce/skins/ui/oxide/skin.min.css'
import 'tinymce/icons/default'
import { ColorPicker } from 'element-ui'
import colorSelect from './colorSelect.vue';
import { Icon } from '@iconify/vue2';
import Editor from '@tinymce/tinymce-vue'
import upload from './upload'
import TinymceEditorPc from './tinymce-editor-pc.vue'

export default {
   [ColorPicker.name]:  ColorPicker,
   'icon-fa': Icon,
   'tinymce-editor': Editor,
   'color-select': colorSelect,
   'pc-upload': upload,
   'tinymce-editor-pc': TinymceEditorPc
  }
