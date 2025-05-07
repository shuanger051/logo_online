
export default {
  name: 'AsyncImage',
  props: ['src', 'click'],
  data() {
    return {
      realSrc: 1
    }
  },
  watch: {
    src: {
      async handler(v) {
        if (v) {
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