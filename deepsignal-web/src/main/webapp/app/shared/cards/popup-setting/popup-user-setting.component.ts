import { Component, Vue, Inject, Prop, Watch } from 'vue-property-decorator';
import { UserSettingService } from '@/service/usersetting.service';
import { CHART_STOCK_STYLE, LOCAL_STORAGE_CONSTANTS, ResponseCode, Setting, TEMPERATURE_UNIT } from '@/shared/constants/ds-constants';
import { UserSetting } from '@/shared/model/usersetting.model';
import PublicService from '@/service/public.service';
import { UserSettingModel } from '@/shared/cards/popup-setting/popup-user-setting.model';
import { namespace } from 'vuex-class';
import TranslationService from '@/locale/translation.service';
const userSettingStore = namespace('userSettingStore');

@Component({
  computed: {
    getCurrentLanguage() {
      return this.$store.getters.currentLanguage;
    },
  },
  watch: {
    getCurrentLanguage(value) {
      this.currentLanguage = value;
      this.language = value;
    },
  },
})
export default class PopupUserSetting extends Vue {
  @Inject('userSettingService')
  private userSettingService: () => UserSettingService;
  @Inject('publicService')
  private publicService: () => PublicService;
  @Inject('translationService')
  private translationService: () => TranslationService;

  private locWeather;
  private language;

  @userSettingStore.Getter
  private userSetting;

  @userSettingStore.Action
  public setUserSetting!: (payload: { connectomeId: string }) => Promise<any>;

  @userSettingStore.Action
  public saveUserSetting!: (payload: { setting: any; connectomeId: string }) => Promise<any>;

  private UNIT = TEMPERATURE_UNIT;
  private STYLE = CHART_STOCK_STYLE;

  private formModel = null;

  private langKey = 'en';
  private currentLanguage = this.$store.getters.currentLanguage;
  private languages: any = this.$store.getters.languages;

  @Watch('userSetting')
  onUserSettingChange() {
    this.formModel = this.userSetting;
  }

  created() {
    this.language = this.currentLanguage;
    this.locWeather = this.getIpFromCache();
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    this.setUserSetting({ connectomeId: connectomeId })
      .then(res => {
        this.formModel = this.userSetting;
      })
      .catch(error => {
        this.formModel = Setting.DEFAULT_SETTING;
      });
  }

  saveSetting() {
    if (this.language !== this.currentLanguage) {
      this.changeLanguage(this.language);
    }
    const connectomeId = JSON.parse(localStorage.getItem('ds-connectome')).connectomeId;
    this.formModel.locWeather = this.locWeather;
    this.saveUserSetting({ setting: this.formModel, connectomeId: connectomeId })
      .then(res => {
        this.showReponseMessage('Save setting successfully!', 'Success', 'success', 5000);
      })
      .catch(error => {
        this.showReponseMessage('Save setting failure!', 'Error', 'error', 5000);
      });
    this.$bvModal.hide('modal-member-setting');
  }

  getIpFromCache(): string {
    return localStorage.getItem(LOCAL_STORAGE_CONSTANTS.IP);
  }

  public getCurrentLangName(): string {
    return this.$store.getters.languages[this.langKey].name;
  }

  public changeLanguage(newLanguage: string): void {
    this.currentLanguage = newLanguage;
    this.$store.commit('currentLanguage', newLanguage);
    this.langKey = newLanguage;
    this.translationService().refreshTranslation(newLanguage);
    this.$store.dispatch('connectomeNetworkStore/shiftConnectomeByLanguage', newLanguage);
    this.$root.$emit('update-new-lang', newLanguage);
  }

  showReponseMessage(mess, title, variant, hideDelay) {
    this.$bvToast.toast(mess, {
      toaster: 'b-toaster-bottom-right',
      title: title,
      variant: variant,
      solid: true,
      autoHideDelay: hideDelay,
    });
  }

  onCloseSetting() {
    if (this.language !== this.currentLanguage) {
      this.language = this.currentLanguage;
    }
  }
}
