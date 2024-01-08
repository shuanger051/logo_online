
import 'core/pc/style/index.scss'
import 'tinymce/tinymce'
import 'tinymce/themes/silver/theme.min.js'
import 'tinymce/skins/ui/oxide/skin.min.css'
import 'tinymce/icons/default'
import { ColorPicker } from 'element-ui'

import { Icon } from '@iconify/vue2';
import Editor from '@tinymce/tinymce-vue'
import upload from './upload'

export default {
   [ColorPicker.name]:  ColorPicker,
   'icon-fa': Icon,
   'tinymce-editor': Editor,
   'pc-upload': upload
  }
