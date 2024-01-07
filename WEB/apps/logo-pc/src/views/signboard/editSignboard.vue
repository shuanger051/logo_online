<template>
   <edit-wrap />
</template>

<script>
import EditWrap from "core/pc/views/editWrap";
import { mapActions } from "vuex";

export default {
  components: { EditWrap },
  methods: {
    ...mapActions('user', ['setToken'])
  },
  created() {
    // this.setToken('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxODYwNDI5ODMwOSIsImV4cCI6MTcwNDYyMTA5MX0.HyIZNlz3I5yLHovD6v6WKSHLohteJb8dLL2lHd4trb0')
  }
};
</script>
