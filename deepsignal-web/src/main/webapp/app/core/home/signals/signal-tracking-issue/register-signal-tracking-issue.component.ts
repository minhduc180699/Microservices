import { Component, Inject, Vue } from 'vue-property-decorator';
import { SignalService } from '@/service/signal.service';
import { PrincipalService } from '@/service/principal.service';
import { SignalKeywordModel } from '@/core/home/signals/signal-tracking-issue/signal-keyword.model';

@Component({
  components: {},
})
export default class RegisterSignalTrackingIssueComponent extends Vue {
  @Inject('signalService') private signalService: () => SignalService;
  @Inject('principalService') private principalService: () => PrincipalService;
  model = new SignalKeywordModel(this.$store.getters.currentLanguage);
  loading = false;
  isShowMainKeyword = false;

  addToListText(e) {
    if (!this.model.messageKeyword || SignalKeywordModel.isDuplicateKeyword(this.model)) {
      return;
    }
    if (e.type === 'submit') {
      this.model.keywordsToShow.push(this.model.messageKeyword);
      if (!this.model.mainKeyword) {
        this.setMainKeyword(this.model.keywordsToShow[0]);
      }
      this.model.messageKeyword = '';
    }
  }

  removeIssueKeywords(keyword: string) {
    if (!keyword) {
      return;
    }
    this.model.keywordsToShow = this.model.keywordsToShow.filter(item => item !== keyword);
    if (!this.model.mainKeyword) {
      this.setMainKeyword(this.model.keywordsToShow[0]);
    }
  }

  setMainKeyword(mainKeyword: string) {
    if (!mainKeyword) return;
    this.model.mainKeyword = mainKeyword;
    this.isShowMainKeyword = true;
  }

  addToRepresentative() {
    this.setMainKeyword(this.model.messageMainKeyword);
    this.model.messageMainKeyword = '';
  }

  removeMainKeyword() {
    this.model.mainKeyword = null;
    this.model.messageMainKeyword = '';
    this.isShowMainKeyword = false;
  }

  apply() {
    const connectomeId = this.principalService().getConnectomeInfo().connectomeId;
    const signalKeywordModel = SignalKeywordModel.prepareSignalModelToSave(this.model);
    if (!signalKeywordModel) return;
    this.loading = true;
    this.signalService()
      .saveSignalKeywords(connectomeId, signalKeywordModel)
      .then(res => {
        this.resetModel();
        this.$emit('onSaved');
      })
      .finally(() => (this.loading = false));
  }

  cancel() {
    this.resetModel();
  }

  resetModel() {
    this.model = new SignalKeywordModel(this.$store.getters.currentLanguage);
    this.isShowMainKeyword = false;
  }
}
