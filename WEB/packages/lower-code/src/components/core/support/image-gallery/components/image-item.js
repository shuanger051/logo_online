export default {
  props: {
    item: {
      type: Object,
      default: () => ({})
    },
    resolveUrl: {
      type: Function,
      default: (u) => u
    },
    height: {
      type: Number,
      default: 142
    }
  },
  render (h) {
    if (this.item.loading) {
      return <a-spin>
        <a-card hoverable>
          <div
            style={{
              height: `${this.height}px`
            }}>
          </div>
        </a-card>
      </a-spin>
    }
    return (
      <a-card
        hoverable
      >
        <div
          style={{
            backgroundImage: `url(${this.resolveUrl(this.item.url)})`,
            backgroundSize: 'contain',
            backgroundPosition: 'center',
            backgroundRepeat: 'no-repeat',
            height: `${this.height}px`
          }}>
        </div>
      </a-card>
    )
  }
}
