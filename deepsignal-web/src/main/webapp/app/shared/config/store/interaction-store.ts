import Vuex, { Module } from 'vuex';

export const interactionStore: Module<any, any> = {
  state: {
    isShow: false,
    coordinate: { x: 0, y: 0 },
    isLike: false,
    isDislike: false,
    _id: '',
    post: {
      url: '',
      title: '',
      writer: '',
      docId: ''
    },
    isBookmark: false,
    isDeleted: false,
    isLearning: false,
    isDetailFeed: false,
    learnMore: '',
  },
  getters: {
    show: state => state.isShow,
    getCoordinate: state => state.coordinate,
    getLike: state => state.isLike,
    getDislike: state => state.isDislike,
    getId: state => state._id,
    getPost: state => state.post,
    getBookmark: state => state.isBookmark,
    getDelete: state => state.isDeleted,
    getLearning: state => state.isLearning,
    getDetailFeed: state => state.isDetailFeed,
    getLearnMore: state => state.learnMore,
  },
  mutations: {
    setShow(state, show) {
      state.isShow = show;
    },
    setCoordinate(state, coordinate) {
      state.coordinate = coordinate;
    },
    setLike(state, isLike) {
      state.isLike = isLike;
    },
    setId(state, _id) {
      state._id = _id;
    },
    setPost(state, post) {
      state.post = post;
    },
    setBookmark(state, isBookmark) {
      state.isBookmark = isBookmark;
    },
    setDelete(state, isDelete) {
      state.isDelete = isDelete;
    },
    setLearning(state, isLearning) {
      state.isLearning = isLearning;
    },
    setDislike(state, isDislike) {
      state.isDislike = isDislike;
    },
    setDetailFeed(state, isDetailFeed) {
      state.isDetailFeed = isDetailFeed;
    },
    setLearnMore(state, learnMore) {
      state.learnMore = learnMore;
    },
  },
};
