<script>
import CoreEditor from 'core/index.js'
export default {
  render () {
    return (
      <CoreEditor workId={this.$route.params.workId} />
    )
  }
}
</script>