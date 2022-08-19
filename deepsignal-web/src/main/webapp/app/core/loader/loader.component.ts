import { Component, Vue } from 'vue-property-decorator';

@Component({
  data() {
    return {
      show: false,
    };
  },
  computed: {
    loading() {
      return this.$store.state.loaderStore.isLoading;
    },
  },
  methods: {
    load() {
      this.$store.commit('SET_LOADING', true);
    },
  },
})
export default class Loader extends Vue {}
