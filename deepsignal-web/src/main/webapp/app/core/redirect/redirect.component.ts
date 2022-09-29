import { Vue, Component, Prop } from 'vue-property-decorator';
import axios from 'axios';

@Component({})
export default class RedirectComponent extends Vue {
  @Prop() readonly shortUrl: any | undefined;

  created(): void {
    if (this.shortUrl) {
      axios.get('api/external-url/deepsignal/' + this.shortUrl).then(res => {
        if (res.data) {
          window.open(res.data, '_self');
        }
      });
    }
  }
}
