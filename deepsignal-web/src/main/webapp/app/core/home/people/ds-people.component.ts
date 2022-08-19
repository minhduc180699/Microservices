import { Component, Vue, Inject } from 'vue-property-decorator';
import DsCards from '@/shared/cards/ds-cards.vue';
import PeopleService from '@/core/home/people/people.service';
import DsCardPeople from '@/shared/cards/people/ds-card-people.vue';
import DsCardRisingPeople from '@/shared/cards/rising-people/ds-card-rising-people.vue';
import { mixins } from 'vue-class-component';
import { ShowMoreMixin } from '@/mixins/show-more';
import ShowMore from '@/shared/cards/footer/show-more/show-more.vue';
import PageTopComponent from '@/core/home/page-top.vue';
@Component({
  name: 'ds-people',
  components: {
    DsCards,
    DsCardPeople,
    DsCardRisingPeople,
    'show-more': ShowMore,
    'page-top': PageTopComponent,
  },
})
export default class DsPeople extends mixins(ShowMoreMixin) {
  cardItems = [];
  cardRisings = [];
  totalPage = -1;
  page = 1;
  size = 6;
  risingPeopleList = [];
  loaderDisable = true;
  dataTransfer = [];

  mounted() {}

  showCardList() {
    let content = '';
    this.risingPeopleList.forEach(data => {
      if (!data.googleInfobox.description && !data.wikipediaInfobox.title) {
        content = '';
        Object.keys(data.googleInfobox).forEach(key => {
          content += key + ': ' + data.googleInfobox[key] + '\n';
        });
      }
      const item = {
        dataTemplate: data.type,
        dataSize: '1x2',
        dataType: '01',
        name: data.label,
        title: data.title,
        date: '01',
        image: data.imageUrl,
        content: data.googleInfobox.description || data.wikipediaInfobox.title || content,
        source: data.googleInfobox.description_url || data.wikiUrl || '',
        stockCode: (data.stock && data.stock.stockCode) || '',
      };
      this.cardRisings.push(item);
    });
  }

  beforeDestroy() {
    this.$store.commit('setNode', '');
  }
}
