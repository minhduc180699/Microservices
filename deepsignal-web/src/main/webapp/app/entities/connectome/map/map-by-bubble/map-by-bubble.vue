<template>
  <div>
    <div id="bubble-chart"></div>
  </div>
</template>
<script lang="ts" src="./map-by-bubble.component.ts"></script>
<style scoped>
#bubble-chart {
  font-weight: 400;
  color: #4c4c4c;
  background-color: black;
  text-align: center;
  cursor: default;
}
.nodeTooltip {
  color: #333333;
  font-family: 'Oswald', sans-serif;
  text-anchor: middle;
  font-size: 12px;
  text-align: center;
}
.node {
  cursor: pointer;
}
.node:hover {
  stroke-width: 1.5px;
}
.node--leaf {
  fill: white;
}
.label,
.hiddenArcWrapper,
.barWrapperOuter,
.popover,
.node--root {
  pointer-events: none;
}
.circleText {
  font-family: 'Oswald', sans-serif;
  text-anchor: middle;
  /*color: white;*/
  /*fill: white;*/
}
.innerCircleTitle {
  text-anchor: middle;
  font-family: 'Oswald', sans-serif;
}
.subTotal {
  /*fill: white;*/
  font-weight: 400;
}
.innerText {
  /*color: #4a4a4a;*/
  /*text-anchor: end;*/
}
.tooltip {
  position: absolute;
  z-index: 1070;
  display: block;
  margin: 0;
  font-style: normal;
  font-weight: 400;
  line-height: 1.5;
  text-align: left;
  text-align: start;
  text-decoration: none;
  text-shadow: none;
  text-transform: none;
  letter-spacing: normal;
  word-break: normal;
  word-spacing: normal;
  white-space: normal;
  line-break: auto;
  font-size: 0.875rem;
  word-wrap: break-word;
  opacity: 0;
}
</style>
