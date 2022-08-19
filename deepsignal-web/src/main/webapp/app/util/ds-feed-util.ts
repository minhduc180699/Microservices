import { CARD_SIZE, CARD_SIZES, CARD_TYPE, DATA_TYPE_CARDS, POST_TYPE } from '@/shared/constants/feed-constants';
import { CARD_PATTERN, CardModel } from '@/shared/cards/card.model';
import { randomEleInArray } from '@/util/array-util';

export function buildCardSizeByImageSize(width, height) {
  if (!width || !height) {
    return CARD_SIZE._1_1;
  }
  if (height <= 118) {
    if (width <= 250) {
      return CARD_SIZE._1_1;
    } else {
      return CARD_SIZE._2_1;
    }
  } else {
    if (width <= 290) {
      return CARD_SIZE._1_2;
    } else {
      return CARD_SIZE._2_2;
    }
  }
}

export function getMaxImageBySize(imgSizes) {
  if (!imgSizes || imgSizes.length == 0) {
    return;
  }
  return imgSizes.reduce(function (prev, current) {
    return prev.height > current.height && prev.width > current.width ? prev : current;
  });
}

export function isFitContentGroupsCardType(cardModel: CardModel) {
  // if (cardModel.subImages && cardModel.subImages.length >= 5 && cardModel.imageDTOS.length > 5) {
  //   return true;
  // }
  return false;
}

// this function to check new card, if has large image, swipers is ignore
export function checkNewsCardTypeWithoutSwipers(cardModel: CardModel) {
  // if (!cardModel || cardModel.swipers || !cardModel.subImages) {
  //   return false;
  // }
  return checkMaxImageFitCardNew(cardModel);
}

export function checkMaxImageFitCardNew(cardModel: CardModel) {
  if (!cardModel) {
    return false;
  }
  // const imgSizeMax = getMaxImageBySize(cardModel.subImages);
  // if (imgSizeMax.width >= 290 && imgSizeMax.height >= 290) {
  //   return true;
  // }
}

export function buildCardSizeForNews(cardModel: CardModel) {
  // if (contentIsNullOrShorten(cardModel.content) && !cardModel.swipers && !cardModel.imageDTOS) {
  //   return {
  //     dataSize: CARD_SIZE._1_1,
  //     dataType: DATA_TYPE_CARDS[0]._01,
  //   };
  // }
  if (checkNewsCardTypeWithoutSwipers(cardModel)) {
    if (checkMaxImageFitCardNew(cardModel)) {
      // const imgSizeMax = getMaxImageBySize(cardModel.subImages);
      // if (imgSizeMax.width > 582 && imgSizeMax.width < 600 && imgSizeMax.height <= 290) {
      //   return {
      //     dataType: DATA_TYPE_CARDS[1]._02,
      //     dataSize: CARD_SIZE._2_1
      //   }
      // }
      // if (imgSizeMax >= 600) {
      //   return {
      //     dataType: DATA_TYPE_CARDS[1]._02,
      //     dataSize: CARD_SIZE._2_2
      //   }
      // }
      const object = randomEleInArray(CARD_SIZES, CARD_SIZE._1_1);
      return {
        dataType: DATA_TYPE_CARDS[1]._02,
        dataSize: object,
      };
    }
  }
  return {
    dataType: DATA_TYPE_CARDS[0]._01,
    dataSize: CARD_SIZE._1_2,
  };
}

export function contentIsNullOrShorten(content: string): boolean {
  if (!content) {
    return true;
  }
  return content.length <= 255;
}
