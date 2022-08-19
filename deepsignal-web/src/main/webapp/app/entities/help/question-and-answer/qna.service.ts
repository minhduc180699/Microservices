import axios from 'axios';
import { IInquiryQuestionModel } from '@/shared/model/inquiryQuestion.model';

export const API_PATH_FILE = 'api/file-storage';
export const API_PATH_QNA = 'api/inquiry-question';

export default class QnaService {
  public createInquiryQuestion(inquiryQuestion?: IInquiryQuestionModel) {
    // console.log('inquiry question: ', inquiryQuestion);
    return axios.post(API_PATH_QNA + '/save', inquiryQuestion);
  }

  public uploadFile(file?: any, userId?: string, type?: number, onUploadProgress?: any) {
    const formData = new FormData();
    formData.append('file', file);
    return axios.post(API_PATH_FILE + `/upload/${userId}` + '?type=' + type, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress,
    });
  }
}
