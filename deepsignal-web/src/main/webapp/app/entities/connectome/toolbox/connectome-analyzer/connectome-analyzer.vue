<template>
  <!-- [Contents Connectome Analyzer] -->
  <div>
    <b-container>
      <b-row cols="1">
        <b-col>
          <b-form inline>
            <label class="sr-only" for="inline-form-input-name">Connectome Id</label>
            <b-form-input
              id="inline-form-input-name"
              class="mb-2 mr-sm-2 mb-sm-0"
              v-model="connectomeId"
              placeholder="Enter Connectome Id"
            ></b-form-input>
            <b-button variant="primary" v-on:click="onConnectomeIdInput()">Load</b-button>
          </b-form>
        </b-col>
      </b-row>
      <b-row cols="1">
        <b-col>
          <b-container style="padding-top: 20px">
            <b-row cols="4">
              <b-col>
                <b-list-group>
                  <b-list-group-item variant="primary">
                    <b-container>
                      <b-row>
                        <b-col><b-icon icon="server" variant="primary" scale="2"></b-icon></b-col><b-col>Main Topic</b-col>
                      </b-row>
                    </b-container>
                  </b-list-group-item>
                </b-list-group>
              </b-col>
              <b-col>
                <b-list-group>
                  <b-list-group-item variant="info">
                    <b-container>
                      <b-row>
                        <b-col><b-icon icon="hdd-stack-fill" variant="info" scale="2"></b-icon></b-col><b-col>Sub Topic</b-col>
                      </b-row>
                    </b-container>
                  </b-list-group-item>
                </b-list-group>
              </b-col>
              <b-col>
                <b-list-group>
                  <b-list-group-item variant="warning">
                    <b-container>
                      <b-row>
                        <b-col><b-icon icon="hdd-network-fill" variant="warning" scale="2"></b-icon></b-col><b-col>Topic Entity</b-col>
                      </b-row>
                    </b-container>
                  </b-list-group-item>
                </b-list-group>
              </b-col>
              <b-col>
                <b-list-group>
                  <b-list-group-item variant="warning">
                    <b-container>
                      <b-row>
                        <b-col><b-icon icon="hdd-fill" variant="warning" scale="2"></b-icon></b-col><b-col>Entity</b-col>
                      </b-row>
                    </b-container>
                  </b-list-group-item>
                </b-list-group>
              </b-col>
            </b-row>
            <b-row cols="3" style="padding-top: 20px">
              <b-col>Topics</b-col
              ><b-col
                ><div v-if="topicSelected">
                  Children from <strong>{{ topicSelected.label }}</strong>
                </div></b-col
              ><b-col cols="4"
                ><div v-if="childSelected">
                  entities from <strong>{{ childSelected.label }}</strong>
                </div></b-col
              >
            </b-row>
            <b-row cols="3">
              <b-col class="limitColumn">
                <b-list-group id="listTopics">
                  <b-list-group-item
                    button
                    v-for="(item, index) in topics"
                    :id="`bt-topic-${index}`"
                    :key="index"
                    :variant="item.variant"
                    v-on:click="onTopicClick(item, index)"
                  >
                    <b-container>
                      <b-row>
                        <b-col cols="1">
                          <b-icon :icon="item.icon" :variant="item.variant" scale="2"></b-icon>
                        </b-col>
                        <b-col cols="3">
                          <b-row>
                            <b-col
                              ><label
                                ><strong>{{ item.label }}</strong></label
                              ></b-col
                            >
                          </b-row>
                        </b-col>
                      </b-row>
                      <b-row>
                        <b-col>
                          <b-row>
                            <b-col>{{ item.topic_id }}</b-col>
                          </b-row>
                          <b-row v-if="item.entities">
                            <b-col>{{ item.entities.length }}</b-col>
                          </b-row>
                        </b-col>
                      </b-row>
                    </b-container>
                  </b-list-group-item>
                </b-list-group>
              </b-col>
              <b-col class="limitColumn">
                <b-list-group id="listChildren">
                  <b-list-group-item
                    button
                    :id="`bt-entity-${index}`"
                    v-for="(item, index) in children"
                    :key="index"
                    :variant="item.variant"
                    v-on:click="onChildClick(item)"
                  >
                    <b-container>
                      <b-row>
                        <b-col cols="1">
                          <b-icon :icon="item.icon" :variant="item.variant" scale="2"></b-icon>
                        </b-col>
                        <b-col cols="3">
                          <b-row>
                            <b-col
                              ><label
                                ><strong>{{ item.label }}</strong></label
                              ></b-col
                            >
                          </b-row>
                        </b-col>
                      </b-row>
                      <b-row>
                        <b-col>
                          <b-row>
                            <b-col>{{ item.topic_id }}</b-col>
                          </b-row>
                          <b-row v-if="item.entities">
                            <b-col>{{ item.entities.length }}</b-col>
                          </b-row>
                        </b-col>
                      </b-row>
                    </b-container>
                  </b-list-group-item>
                </b-list-group>
              </b-col>
              <b-col class="limitColumn">
                <b-list-group id="listEntities">
                  <b-list-group-item
                    button
                    :id="`bt-entity-${index}`"
                    v-for="(item, index) in entities"
                    :key="index"
                    v-on:click="onEntityClick(item)"
                    variant="warning"
                  >
                    <b-container>
                      <b-row>
                        <b-col cols="1">
                          <b-icon-hdd-fill variant="warning" scale="2"></b-icon-hdd-fill>
                        </b-col>
                        <b-col cols="3">
                          <b-row>
                            <b-col>
                              <strong>{{ item.label }}</strong></b-col
                            >
                          </b-row>
                          <b-row>
                            <b-col>{{ item.doc_freq }}</b-col>
                          </b-row>
                        </b-col>
                      </b-row>
                    </b-container>
                  </b-list-group-item>
                </b-list-group>
              </b-col>
            </b-row>
          </b-container>
        </b-col>
      </b-row>
      <b-row cols="1">
        <b-col>
          <pre v-html="detail" class="limitColumn" />
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script lang="ts" src="./connectome-analyzer.component.ts"></script>
<style scoped>
.limitColumn {
  padding-top: 20px;
  height: 640px;
  overflow-y: scroll;
  .btn {
    background: #ccc;
  }
  .btn:active {
    background: red;
  }
}
</style>
