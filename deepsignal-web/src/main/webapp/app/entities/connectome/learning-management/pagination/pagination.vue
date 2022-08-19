<template>
  <div class="row" style="margin-bottom: 10px">
    <div class="col-7"></div>
    <div class="col-auto" style="margin-bottom: 15px">
      <nav aria-label="Page navigation">
        <div>
          <ul class="pagination justify-content-center">
            <li class="page-item" v-if="firstPageVal">
              <a class="page-link" href="#" @click="firstPage($event)">
                <svg width="13.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="M10.858 11.498 5.499 5.997l5.359-5.5" />
                  <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                </svg>
              </a>
            </li>
            <li class="page-item disabled" v-if="!firstPageVal">
              <a class="page-link" href="#" @click="firstPage($event)">
                <svg width="13.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="M10.858 11.498 5.499 5.997l5.359-5.5" />
                  <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                </svg>
              </a>
            </li>
            <li class="page-item" v-if="previousPageVal">
              <a class="page-link" href="#" @click="previousPage($event)">
                <svg width="8.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                </svg>
              </a>
            </li>
            <li class="page-item disabled" v-if="!previousPageVal">
              <a class="page-link" href="#" @click="previousPage($event)">
                <svg width="8.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="M5.858 11.498.499 5.997l5.359-5.5" />
                </svg>
              </a>
            </li>
            <li class="page-item" aria-current="page"><a class="page-link" v-if="prev">...</a></li>
            <div v-for="index in total" :key="index">
              <li v-if="index <= page + 1 && index >= page - 1" class="page-item" aria-current="page" :id="'page' + index">
                <a class="page-link" href="#" @click="changePage(index, $event)">{{ index }}</a>
              </li>
            </div>
            <li class="page-item" aria-current="page"><a class="page-link" v-if="next">...</a></li>
            <li class="page-item" v-if="nextPageVal">
              <a class="page-link" href="#" @click="nextPage($event)">
                <svg width="8.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="m2.141.497 5.359 5.5-5.359 5.501" />
                </svg>
              </a>
            </li>
            <li class="page-item disabled" v-if="!nextPageVal">
              <a class="page-link" href="#" @click="nextPage($event)">
                <svg width="8.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="m2.141.497 5.359 5.5-5.359 5.501" />
                </svg>
              </a>
            </li>
            <li class="page-item" v-if="lastPageVal">
              <a class="page-link" href="#" @click="lastPage($event)">
                <svg width="13.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="m2.141.497 5.358 5.5-5.358 5.501" />
                  <path stroke="#516eff" fill="none" d="m7.141.497 5.358 5.5-5.358 5.501" />
                </svg>
              </a>
            </li>
            <li class="page-item disabled" v-if="!lastPageVal">
              <a class="page-link" href="#" @click="lastPage($event)">
                <svg width="13.5" height="12.5">
                  <path stroke="#516eff" fill="none" d="m2.141.497 5.358 5.5-5.358 5.501" />
                  <path stroke="#516eff" fill="none" d="m7.141.497 5.358 5.5-5.358 5.501" />
                </svg>
              </a>
            </li>
          </ul>
        </div>
      </nav>
    </div>
  </div>
</template>

<script lang="ts" src="./pagination.component.ts"></script>
