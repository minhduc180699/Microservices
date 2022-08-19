<template>
  <!-- [Contents Group] -->

  <!--  apply when call API-->
  <!--  <div class="card">-->
  <!--    <div class="card-header">-->
  <!--      <div class="badge" v-for="name in item.web_source_category">{{ name }}</div>-->
  <!--    </div>-->
  <!--    <div class="card-body">-->
  <!--      <a href="">-->
  <!--        <div class="tit">{{ item.title }}</div>-->
  <!--        <div class="desc">{{ item.content }}</div>-->
  <!--      </a>-->
  <!--      <div class="grid">-->
  <!--        <div class="row row-10">-->
  <!--          <div class="col-auto first">-->
  <!--            <a href="" class="box">-->
  <!--              <div class="img">-->
  <!--                <img :src="images[0].url" alt="" />-->
  <!--              </div>-->
  <!--            </a>-->
  <!--          </div>-->
  <!--          <div class="col">-->
  <!--            <div class="row row-10">-->
  <!--              <div class="col">-->
  <!--                <a href="" class="box">-->
  <!--                  <div class="img">-->
  <!--                    <img :src="images[1].url" alt="" />-->
  <!--                  </div>-->
  <!--                </a>-->
  <!--              </div>-->
  <!--              <div class="col">-->
  <!--                <a href="" class="box">-->
  <!--                  <div class="img">-->
  <!--                    <img :src="images[2].url" alt="" />-->
  <!--                  </div>-->
  <!--                </a>-->
  <!--              </div>-->
  <!--            </div>-->
  <!--            <div class="row row-10 m-t-10">-->
  <!--              <div class="col">-->
  <!--                <a href="" class="box">-->
  <!--                  <div class="img">-->
  <!--                    <img :src="images[3].url" alt="" />-->
  <!--                  </div>-->
  <!--                </a>-->
  <!--              </div>-->
  <!--              <div class="col">-->
  <!--                <a href="" class="box">-->
  <!--                  &lt;!&ndash; <div class="d-flex align-items-center justify-content-center">-->
  <!--                      <div class="only-text">-->
  <!--                          <span>중국해킹 <br>들어오겠네..</span>-->
  <!--                      </div>-->
  <!--                  </div> &ndash;&gt;-->
  <!--                  <div class="img">-->
  <!--                    <img :src="images[4].url" alt="" />-->
  <!--                  </div>-->
  <!--                </a>-->
  <!--              </div>-->
  <!--            </div>-->
  <!--          </div>-->
  <!--        </div>-->
  <!--      </div>-->
  <!--      <a href="" class="btn btn-lightgray">{{ item.imageDTOS.length - images.length }} more Contents +</a>-->
  <!--    </div>-->
  <!--    <ds-card-footer />-->
  <!--  </div>-->

  <!--  apply when don't call API-->
  <div class="card">
    <div class="card-header">
      <div class="badge">{{ item.name }}</div>
    </div>
    <div class="card-body">
      <a href="">
        <div class="tit">{{ item.title }}</div>
        <div class="desc">{{ item.desc }}</div>
      </a>
      <div class="grid">
        <div class="row row-10">
          <div class="col-auto first">
            <a href="" class="box">
              <div class="img">
                <img src="content/images/@group-img-1.png" alt="" />
              </div>
            </a>
          </div>
          <div class="col">
            <div class="row row-10">
              <div class="col">
                <a href="" class="box">
                  <div class="img">
                    <img src="content/images/@group-img-2.png" alt="" />
                  </div>
                </a>
              </div>
              <div class="col">
                <a href="" class="box">
                  <div class="img">
                    <img src="content/images/@group-img-3.png" alt="" />
                  </div>
                </a>
              </div>
            </div>
            <div class="row row-10 m-t-10">
              <div class="col">
                <a href="" class="box">
                  <div class="img">
                    <img src="content/images/@group-img-4.png" alt="" />
                  </div>
                </a>
              </div>
              <div class="col">
                <a href="" class="box">
                  <!-- <div class="d-flex align-items-center justify-content-center">
                      <div class="only-text">
                          <span>중국해킹 <br>들어오겠네..</span>
                      </div>
                  </div> -->
                  <div class="img">
                    <img src="content/images/@group-img-5.png" alt="" />
                  </div>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <a href="" class="btn btn-lightgray">7 more Contents +</a>
    </div>
    <ds-card-footer :item="item" />
  </div>
</template>
<script lang="ts" src="./ds-card-group.component.ts"></script>
