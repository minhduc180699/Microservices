import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
const BASE_URL = 'api/notification';

export class NotificationService {
  public paging(type, pageable?) {
    if (!type) {
      type = '';
    }
    let req = {};
    if (pageable) {
      req = {
        page: pageable.page ? pageable.page - 1 : 0,
        size: pageable.size ? pageable.size : 10,
        sort: pageable.sort ? pageable.sort : ['createdDate,desc'],
      };
    }
    return axios.get(BASE_URL + '/paging?type=' + type + '&' + buildPaginationQueryOpts(req));
  }

  public markAllRead() {
    return axios.get(BASE_URL + '/markAllRead');
  }

  public markAsRead(id) {
    return axios.get(BASE_URL + '/markAsRead?id=' + id);
  }

  public countAllUnread() {
    return axios.get(BASE_URL + '/countAllUnread');
  }

  public getAllNotificationType() {
    return axios.get(BASE_URL + '/getAllType');
  }

  public markAllChecked() {
    return axios.get(BASE_URL + '/markAllChecked');
  }

  public deleteReadNotification() {
    return axios.delete(BASE_URL + '/deleteReadNotification');
  }

  public deleteAllNotification() {
    return axios.delete(BASE_URL + '/deleteAll');
  }

  public deleteNotificationById(id) {
    return axios.delete(BASE_URL + '/delete/' + id);
  }
}
