import Component from 'vue-class-component';
import { Vue, Inject } from 'vue-property-decorator';
import { email, maxLength, minLength, required } from 'vuelidate/lib/validators';
import FaqService from '../faq/faq.service';
import { CategoryModel } from '@/shared/model/category.model';
import QnaService from '@/entities/help/question-and-answer/qna.service';
import { IInquiryQuestionModel, InquiryQuestionModel } from '@/shared/model/inquiryQuestion.model';
import AccountService from '@/account/account.service';
import axios from 'axios';

const validations: any = {
  formModel: {
    title: {
      required,
    },
    content: {
      required,
      maxLength: maxLength(2000),
    },
    name: {
      required,
    },
    email: {
      required,
      email,
    },
  },
};

@Component({
  validations,
  components: {},
})
export default class QnaComponent extends Vue {
  public formModel = {
    category: {
      code: '',
      type: 1,
    },
    title: '',
    content: '',
    email: '',
    name: '',
    file: '',
    isPublic: false,
  };

  public fileUpload;
  public previewImage = null;
  public progress = 0;
  public categories = [];
  public sizeFile = null;
  public userLogin;
  public formCorrect = false;

  @Inject('qnaService') qnaService: () => QnaService;
  @Inject('accountService') accountService: () => AccountService;

  mounted(): void {
    this.userLogin = JSON.parse(localStorage.getItem('ds-connectome'))['user'];
    this.getAllCategory();
  }

  public getAllCategory() {
    FaqService.getAllCategoryFaq(1).then(res => {
      this.formModel.category.code = res.data[0].code;
      res.data.forEach(cate => {
        const category = new CategoryModel(cate, false);
        this.categories.push(category);
      });
    });
  }

  public onClick() {
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.click();
  }

  public uploadImage(e: any) {
    // console.log('event = ', e.target.files.length);
    if (e.target.files.length > 0) {
      this.progress = 0;
      const userId = JSON.parse(localStorage.getItem('ds-connectome'))['user'].id;
      this.sizeFile = (e.target.files[0].size / Math.pow(1024, 2)).toFixed(3);
      this.formModel.file = e.target.files[0].name;
      this.fileUpload = e.target.files[0];
      // this.previewImage = URL.createObjectURL(e.target.files[0]);
      if (this.sizeFile <= 10) {
        // this.sizeFile = null;
        this.qnaService()
          .uploadFile(this.fileUpload, userId, 1, event => {
            // debugger;
            this.progress = Math.round((100 * event.loaded) / event.total);
            console.log('loaded = ', event.loaded);
            console.log('total = ', event.total);
            console.log('progress = ', this.progress);
          })
          .then(() => console.log('upload success'))
          .catch(() => console.log('upload fail'));
      }
    }
  }

  public submit() {
    // console.log('user login: ', this.accountService().getUserLogin());
    this.$v.$touch();
    if (this.$v.$invalid || this.sizeFile > 10) {
      this.formCorrect = true;
    } else {
      this.formCorrect = false;
      const inquiryQuestion = this.createModel();
      this.qnaService()
        .createInquiryQuestion(inquiryQuestion)
        .then(res => {
          console.log('save success');
        })
        .catch(err => {
          console.log('error');
        });
    }
  }

  public createModel(): IInquiryQuestionModel {
    return {
      // ...new InquiryQuestionModel(),
      category: this.formModel.category,
      title: this.formModel.title,
      content: this.formModel.content,
      emailName: this.formModel.email,
      name: this.formModel.name,
      file: this.formModel.file,
      // status: 1,
      public: this.formModel.isPublic,
      email: 1,
      user: this.accountService().getUserLogin(),
    };
  }
}
