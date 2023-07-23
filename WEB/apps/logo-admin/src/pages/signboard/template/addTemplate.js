import CoreEditor from 'core/index.js'
export default {
  render () {
    return (
      <CoreEditor workId={this.$route.params.workId} local={this.$i18n.locale} />
    )
  }
}
