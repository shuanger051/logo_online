import { resolveImgUrlBase64 } from "core/support/imgUrl";
import blank from './blank.png'

export default {
  name: 'AsyncImage',
  props: ['src', 'click'],
  data() {
    return {
      realSrc: blank
    }
  },
  watch: {
    src: {
      async handler(v) {
        if (v) {
          const src= await resolveImgUrlBase64(this.src, false)
          this.realSrc = src
        }
      },
      immediate: true
    }
  },
  methods: {
    getRealSrc() {
      
    },
    handlerClick(e) {
      if (this.click) {
        this.click(e)
      }
    }
  },
  render() {
    return <img src={this.realSrc} onClick={this.handlerClick}/>
  }
}