import {Vue, Component, Watch} from 'vue-property-decorator';
import vueCustomScrollbar from 'vue-custom-scrollbar';
import axios from "axios";
import {COLLECTION_SERVER} from "@/shared/constants/ds-constants";

@Component({
  components: {
    vueCustomScrollbar: vueCustomScrollbar,
  },
})
export default class LearningCenterAsideComponent extends Vue {
  private collections = [];

  private scrollSettings = {
    wheelPropagation: false,
  };

  data() {
    return {
      collections: this.collections,
    }
  }

  @Watch('collections')
  collectionsChange() {
    const documents = [];
    this.collections.forEach(collection => {
      if (collection.documentIdList?.length > 0) {
        collection.documentIdList.forEach(document => {
          documents.push(document);
        });
      }
    });

    // axios
    //   .post(COLLECTION_SERVER + 'api/personal-documents/getByDocIds', documents)
    //   .then( res => {
    //       if (res?.data?.connectomePersonalDocuments) {
    //         this.collections.forEach(collection => {
    //           for (let document of res.data.connectomePersonalDocuments) {
    //             // debugger;
    //             const item = collection?.documentList.find(item => item?.docId == document?.docId);
    //             if (collection?.documentList?.length > 0 && item) {
    //               const image = document.ogImageUrl
    //                 || document.ogImageBase64
    //                 || (document.imageUrl && document.imageUrl[0])
    //                 || (document.imageBase64 && document.imageBase64[0])
    //                 || '';
    //
    //               collection.documentList = [];
    //               collection.documentList.push({
    //                 docId: document.docId,
    //                 image: image,
    //               });
    //             }
    //           }
    //         })
    //       }
    //     }
    //   );
  }

  created(): void {
    const connectomeId = localStorage.getItem('ds-connectome')
      ? JSON.parse(localStorage.getItem('ds-connectome'))?.connectomeId
      : JSON.parse(sessionStorage.getItem('ds-connectome'))?.connectomeId;
    axios
      .get(COLLECTION_SERVER + 'api/connectome/collection/get/all', {
        headers: {
          'connectomeId': connectomeId ? connectomeId : null,
        }
      })
      .then(res => {
        this.collections = res.data?.body;
      })
  }
}
