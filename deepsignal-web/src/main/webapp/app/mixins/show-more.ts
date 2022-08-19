import Vue from 'vue';
import Component from 'vue-class-component';
import { Inject, Prop, Watch } from 'vue-property-decorator';
import { InteractionUserService } from '@/service/interaction-user.service';
import { interactionLike } from '@/shared/cards/footer/ds-card-footer.component';
import axios from 'axios';

@Component
export class ShowMoreMixin extends Vue {
  // variables
  connectomeId;
  cardItems = [];
  cardItemSearch = [];
  isSearching = false;

  // lifecycle hook
  created(): void {
    this.connectomeId = JSON.parse(localStorage.getItem('ds-connectome'))
      ? JSON.parse(localStorage.getItem('ds-connectome')).connectomeId
      : null;
  }
}
