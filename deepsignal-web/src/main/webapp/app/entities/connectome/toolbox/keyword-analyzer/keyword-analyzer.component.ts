import { Console } from 'console';
import { json } from 'd3-fetch';
import { number } from 'echarts';
import { Component, Inject, Vue, Watch } from 'vue-property-decorator';
import ConnectomeService from '../../connectome.service';
@Component({})
export default class KeywordAnalyzerComponent extends Vue {
  requestId = '';
  headVariant = 'light';
  languageSelected = 'ko';
  languageOptions: Array<{ value: string; text: string }> = [
    { value: 'en', text: 'English' },
    { value: 'ko', text: 'Korean' },
  ];
  textToAnalyze = '';

  keywordsResult = '';
  textAnalyzed = '';

  fields: Array<string> = ['keyword', 'entity', 'esa', 'gmuse'];
  rows: Array<{
    keyword: string;
    entity: string;
    esa: number;
    gmuse: number;
    _rowVariant: string;
  }> = [];
  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  onDataToAnalyze() {
    const apiCallKeyword = this.connectomeService().postKeywordAnalysis(
      this.requestId,
      this.languageSelected ? this.languageSelected : null,
      this.textToAnalyze
    );
    //console.log('data to analyze requested ', apiCallKeyword);

    const apiCallentity_disambiguation = this.connectomeService().postKeyword_entity_disambiguation(
      this.requestId,
      this.languageSelected ? this.languageSelected : null,
      this.textToAnalyze
    );

    if (!apiCallKeyword) {
      return;
    }

    if (!apiCallentity_disambiguation) {
      return;
    }

    apiCallKeyword.then(res => {
      if (!res.data) {
        return;
      }

      if (!res.data.result) {
        return;
      }
      this.keywordsResult = JSON.stringify(res.data.result, undefined, 2);
      console.log('keywords:', res.data.result.keywords);
      this.highlightKeywords(res.data.result.keywords);
    });

    apiCallentity_disambiguation.then(res => {
      if (!res.data) {
        return;
      }

      if (!res.data.result) {
        return;
      }

      this.entitylinkingReport(res.data.result.entitylinking);
    });
  }

  highlightKeywords(
    keywords: Array<{
      word: string;
      start: number;
      end: number;
      word_id: number;
    }>
  ) {
    if (!this.textToAnalyze) {
      return;
    }

    if (!keywords) {
      return;
    }
    const start = [];
    const end = [];
    keywords.forEach(Element => {
      start.push(Element.start);
      end.push(Element.end);
    });
    this.textAnalyzed = '';
    for (let i = 0; i < this.textToAnalyze.length; i++) {
      if (start.indexOf(i) >= 0) {
        this.textAnalyzed += '<span style="color:red">[';
      }
      this.textAnalyzed += this.textToAnalyze.charAt(i);
      if (end.indexOf(i + 1) >= 0) {
        this.textAnalyzed += ']</span>';
      }
    }
    console.log('this.textAnalyzed', this.textAnalyzed);
  }

  entitylinkingReport(
    entitylinking: Array<{
      freq: number;
      word: string;
      word_id: number;
      entity: Array<{
        entity_id: number;
        entity_title: string;
        similarities: Array<{
          method: string;
          sim: number;
        }>;
      }>;
    }>
  ) {
    if (!entitylinking || entitylinking.length < 1) {
      return;
    }
    this.rows = [];
    entitylinking.forEach(element => {
      let row: {
        keyword: string;
        entity: string;
        esa: number;
        gmuse: number;
        _rowVariant: string;
      } = {
        keyword: '',
        entity: '',
        esa: 0,
        gmuse: 0,
        _rowVariant: '',
      };

      if (element.entity && element.entity.length > 0) {
        const entityrows: Array<{
          keyword: string;
          entity: string;
          esa: number;
          gmuse: number;
          _rowVariant: string;
        }> = [];
        let maxSim = 0;
        let entityMaxSim: string = null;

        element.entity.forEach(entity => {
          row = {
            keyword: '',
            entity: '',
            esa: 0,
            gmuse: 0,
            _rowVariant: null,
          };
          row.keyword = element.word;
          row.entity = entity.entity_title;
          if (entity.similarities && entity.similarities.length > 1) {
            entity.similarities.forEach(similiraty => {
              if (similiraty.method == 'esa') {
                row.esa = similiraty.sim;
              }
              if (similiraty.method == 'gmuse') {
                row.gmuse = similiraty.sim;
              }
            });
          }
          const simSum: number = row.esa + row.gmuse;
          if (simSum > maxSim || entityMaxSim === null) {
            entityMaxSim = entity.entity_title;
            maxSim = row.esa + row.gmuse;
          }
          entityrows.push(row);
        });

        entityrows.forEach(entitiesRow => {
          if (entitiesRow.entity === entityMaxSim) {
            entitiesRow._rowVariant = 'info';
          }
          this.rows.push(entitiesRow);
        });
      }
    });
  }
}
