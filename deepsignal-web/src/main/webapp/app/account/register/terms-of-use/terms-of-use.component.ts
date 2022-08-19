import { Component, Vue } from 'vue-property-decorator';

@Component
export default class TermsOfUse extends Vue {
  scrollToSection(id) {
    document.getElementById(id).scrollIntoView();
  }
}
