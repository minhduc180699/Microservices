export class PageableModel {
  page?: number;
  size?: number;
  sort?: string[];
  isFirst: boolean;
  isLast: boolean;
  totalElements: number;
  totalPages: number;
  contents: any[];
  contentsOrigin: any[];

  // if you don't pass value to constructor, value default will be set default value
  constructor(contentsOrigin?: any[], page = 1, size = 10, sort = ['id,asc']) {
    this.contentsOrigin = contentsOrigin;
    this.page = page;
    this.size = size;
    this.sort = sort;
    this.paging();
  }

  // pass your list data to paging
  paging(): any[] {
    if (!this.contentsOrigin) {
      return;
    }
    this.contents = [];
    const start = (this.page - 1) * this.size;
    let end = start + this.size;
    this.totalElements = this.contentsOrigin.length;
    if (end > this.totalElements) {
      end = this.totalElements;
      this.isLast = true;
    }
    this.totalPages = Math.ceil(this.totalElements / this.size);
    for (let i = start; i < end; i++) {
      this.contents.push(this.contentsOrigin[i]);
    }
  }

  nextPage(isCheck?) {
    if (this.page < this.totalPages) {
      this.page += 1;
      if (!isCheck) this.paging();
    }
  }

  previousPage() {
    this.page -= 1;
    if (this.page < 1) {
      this.page = 1;
      this.isFirst = true;
      return;
    }
    this.paging();
  }

  getFirstPage() {
    this.page = 1;
  }

  getSizeDefault() {
    this.size = 10;
  }
}
