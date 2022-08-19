import axios from 'axios';

export default class RegisterService {
  // public processRegistration(account: any): Promise<any> {
  //   return axios.post('api/register', account);
  // }

  public getCountryCode() {
    return axios.get('api/public/getCountryCode');
  }

  // public getAllPurpose() {
  //   return axios.get('api/purpose/getAll');
  // }

  public sendEmailService(account: any) {
    return axios.post('api/email/send', account);
  }

  public verifyEmailCode(account: any) {
    return axios.post('api/email/verify', account);
  }

  public userReceive(account: any) {
    return axios.post('api/public/receive', account);
  }

  public getPurposes(account: any) {
    return axios.post('api/register', account);
  }

  public getConnectome(account: any) {
    return axios.post('api/public/connectome', account);
  }

  public uploadImageProfile(data) {
    return axios({
      method: 'POST',
      url: 'api/public/upload',
      data: data,
    });
  }

  public checkEmailExisted(email) {
    return axios.get('api/public/checkEmailExisted/' + email);
  }

  public checkUsernamExisted(username) {
    return axios.get('api/public/checkUsernameExisted/' + username);
  }
}
