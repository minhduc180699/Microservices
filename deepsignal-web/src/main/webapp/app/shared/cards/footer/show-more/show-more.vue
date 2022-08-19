<template>
  <div>
    <div
      class="tooltip-menu"
      id="show-more"
      v-show="isShowMore"
      v-bind:style="{ top: yCoordinate + 'px', left: xCoordinate + 'px', position: 'absolute', minHeight: '100px', zIndex: '2' }"
    >
      <!--      { top: yCoordinate + 'px', left: xCoordinate + 'px', position: 'absolute', minHeight: '200px', zIndex: '2' }-->
      <ul>
        <li>
          <a @click="handleShare()"><i class="icon-common icon-share"></i>Share</a>
        </li>
        <li>
          <a v-bind:style="isBookmark ? { color: '#516eff' } : { color: '#000' }" @click="handleBookmark"
            ><i class="icon-common icon-boormark"></i>Bookmark</a
          >
        </li>
        <li class="bar"></li>
        <li>
          <a v-bind:style="isLike ? { color: '#516eff' } : { color: '#000' }" @click="handleLike"
            ><i class="icon-common icon-like"></i>Like</a
          >
        </li>
        <li>
          <a v-bind:style="isDislike ? { color: '#516eff' } : { color: '#000' }" @click="handleDislike"
            ><i class="icon-common icon-dislike"></i>Dislike</a
          >
        </li>
        <li class="bar"></li>
        <li>
          <a @click="handleHide"><i class="icon-common icon-visibility"></i>Hide Card</a>
        </li>
      </ul>
    </div>
    <!--    modal for sharing-->
    <b-modal id="shareModal" hide-footer centered title="Share on social network">
      <div style="padding-left: 15px; padding-top: 5px">
        <h6>
          Post: <a style="font-size: 15px" :href="post.sourceId" target="_blank">{{ post.title }}</a> - {{ post.writer }}
        </h6>
        <!--        <b-button variant="primary" style="margin-top: 5px; float: right">Update</b-button>-->
        <h6>Share on:</h6>
      </div>
      <div class="learning-tool" style="padding: 15px 0 15px 0">
        <ul>
          <li>
            <a @click="shareByLink" style="margin-top: 15px">
              <h3>Link</h3>
            </a>
          </li>
          <li>
            <a @click="shareOnFacebook">
              <span class="ico"><img src="content/images/resrouces-social-facebook.png" alt="" /></span>
              <span class="name">Facebook</span>
            </a>
          </li>
          <li>
            <a @click="shareOnTwitter">
              <span class="ico"><img src="content/images/resrouces-social-twitter.png" alt="" /></span>
              <span class="name">Twitter</span>
            </a>
          </li>
          <li>
            <a @click="shareOnLinkedIn">
              <span class="ico"><img src="content/images/resrouces-social-linkedin.png" alt="" /></span>
              <span class="name">Linkedin</span>
            </a>
          </li>
        </ul>
      </div>
      <div style="margin-top: 10px; padding: 0 15px 15px 15px" class="share-link" v-if="isSharingLink">
        <hr style="margin-bottom: 10px" />
        <dl class="top-title">
          <h6>Share with link:</h6>
          <b-form-group label="Sharing mode">
            <b-form-radio-group v-model="selected" :options="options"></b-form-radio-group>
          </b-form-group>
          <div v-if="selected === 'RESTRICTED'">
            <p>Write something about this post:</p>
            <b-form-textarea
              size="sm"
              style="max-height: 100px; margin-top: 5px; margin-bottom: 5px"
              placeholder="Write something..."
              v-model="textShare"
            ></b-form-textarea>
            <div class="invalid-feedback" style="display: block" v-if="checkTextShare">You have to enter at least 10 characters</div>
            <b-input-group prepend="Recipients" class="mb-2">
              <b-form-input
                aria-label="First name"
                @input="searchEmail()"
                v-model="textSearchEmail"
                placeholder="Search email..."
              ></b-form-input>
            </b-input-group>
            <div style="max-height: 150px; overflow-y: scroll" v-if="textSearchEmail">
              <div
                class="result-search-email"
                style="text-align: right; margin-right: 5px; margin-bottom: 5px"
                v-for="item in resultSearch"
                :key="item.email"
                @click="chooseEmail(item.email)"
              >
                {{ item.email }}
              </div>
              <div v-if="resultSearch.length === 0" style="text-align: right; margin-right: 5px; margin-bottom: 5px">Email not found!</div>
            </div>
            <dd class="connected-keyword" :id="'hashTag'" style="margin-bottom: 0">
              <a class="active" v-for="item in emailsSending" :key="item"
                >{{ item }}
                <button type="button" class="close" aria-label="Close" @click="removeEmail(item)"><i class="bi bi-x"></i></button>
              </a>
            </dd>
            <div class="button-bot">
              <b-button
                class="but-share"
                style="margin-top: 10px; margin-bottom: 10px"
                size="sm"
                @click="sendEmail()"
                v-if="isSendingEmail"
              >
                Send Email
              </b-button>
              <b-button disabled v-if="!isSendingEmail" class="but-share" style="margin-top: 10px; margin-bottom: 10px" size="sm">
                <b-spinner small type="grow"></b-spinner>
                Sending...
              </b-button>
            </div>
          </div>
          <div v-else>
            <hr style="margin-bottom: 10px" />
            <h6>Link:</h6>
            <b-input-group size="sm" class="mb-3">
              <b-form-input v-model="linkPublic"></b-form-input>
              <b-input-group-append>
                <b-button size="sm" text="Button" :variant="isCopy ? '' : 'outline-primary'" @click="handleCopy()"
                  >{{ isCopy ? 'Copied' : 'Copy' }}
                </b-button>
              </b-input-group-append>
            </b-input-group>
          </div>
        </dl>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./show-more.component.ts"></script>

<style scoped>
.button-bot > but-share {
  float: right;
}
</style>
