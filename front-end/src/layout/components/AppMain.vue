<template>
  <section class="app-main">
    <transition :name="transitionName" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>

    <Footer />

  </section>
</template>

<script>
import Footer from './Footer/index.vue'
export default {
  name: 'AppMain',
  components: {
    Footer
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      if (this.$route.name === 'Search') {
        return 'Search'
      }
      return this.$route.fullPath
    },
    transitionName() {
      return this.$route.params.disableTransition ? '' : 'fade-transform'
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header+.app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header+.app-main {
    padding-top: 84px;
  }
}

</style>

