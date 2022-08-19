import { TYPE_NODE_IN_MAP } from '@/shared/constants/ds-constants';
import { Vue, Component, Prop, Watch } from 'vue-property-decorator';

@Component
export default class DsSearchDropdownComponent extends Vue {
  @Prop({ default: () => 'dropdown', required: false }) name: string;

  @Prop({ default: () => [], required: true }) options: Array<any>;

  @Prop({ default: () => 'Please select an option', required: false }) placeholder: string;

  @Prop({ default: () => false, required: false }) disabled: boolean;

  @Prop({ default: () => 6, required: false }) maxItem: number;

  selected: any = {};
  optionsShown = false;
  searchFilter = '';

  data() {
    return {
      selected: this.selected,
      optionsShown: this.optionsShown,
      searchFilter: this.searchFilter,
    };
  }

  created() {
    this.$emit('selected', this.selected);
  }

  get filteredOptions() {
    const filtered = [];
    const regOption = new RegExp(this.searchFilter, 'ig');
    for (const option of this.options) {
      if (this.searchFilter?.length < 1 || option.name.match(regOption)) {
        if (filtered.length < this.maxItem) filtered.push(option);
      }
    }
    return filtered;
  }

  selectOption(option) {
    this.selected = option;
    this.optionsShown = false;
    this.searchFilter = this.selected.name;
    this.$emit('selected', this.selected);
  }

  showOptions() {
    if (!this.disabled) {
      this.searchFilter = '';
      this.optionsShown = true;
    }
  }

  exit() {
    if (!this.selected.id) {
      this.selected = {};
      this.searchFilter = '';
    } else {
      this.searchFilter = this.selected.name;
    }
    this.$emit('selected', this.selected);
    this.optionsShown = false;
  }

  // Selecting when pressing Enter
  keyMonitor(event) {
    if (event.key === 'Enter' && this.filteredOptions[0]) this.selectOption(this.filteredOptions[0]);
  }
}
