import Component from 'vue-class-component';
import { Prop, Vue } from 'vue-property-decorator';
import { CategoriesNetworkChart, LinkNetworkChart, NodeNetworkChart } from '@/shared/chart/ds-network-chart/network-chart-model';

@Component({})
export default class DsNetworkChart extends Vue {
  @Prop(Object) readonly graph: any | undefined;
  @Prop(String) title: string | '';

  data() {
    return {
      option: {
        title: {
          text: this.title,
          subtext: 'Default layout',
          top: 'bottom',
          left: 'center',
        },
        tooltip: {},
        legend: [
          {
            // selectedMode: 'single',
            data: this.buildNameLegend(),
          },
        ],
        series: [
          {
            name: 'Les Miserables',
            type: 'graph',
            layout: 'force',
            data: this.buildNodes(),
            links: this.buildLinks(),
            categories: this.buildCategories(),
            roam: true,
            label: {
              position: 'center',
            },
            force: {
              repulsion: 200,
            },
          },
        ],
      },
    };
  }

  buildNameLegend(): string[] {
    if (!this.graph.categories) {
      return;
    }
    return this.graph.categories.map(a => a.name);
  }

  buildNodes(): NodeNetworkChart[] {
    if (!this.graph.nodes) {
      return;
    }
    return this.graph.nodes;
  }

  buildLinks(): LinkNetworkChart[] {
    if (!this.graph.links) {
      return;
    }
    return this.graph.links;
  }

  buildCategories(): CategoriesNetworkChart[] {
    if (!this.graph.categories) {
      return;
    }
    return this.graph.categories;
  }
}
