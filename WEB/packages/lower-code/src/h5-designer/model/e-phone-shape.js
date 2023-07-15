export default {
  props: ['extra', 'id'],
  data() {
    return {
      hide: this.extra.initStatus == 0
    }
  },
  render() {
    return (
      <div
        class='u-phone-shap'
        id={this.id}
        style={{
          display: this.hide ? 'none' : 'block',
          position: 'absolute'
        }}
      >
        {
          this.$slots.default
        }
      </div>
    )
  }
}
