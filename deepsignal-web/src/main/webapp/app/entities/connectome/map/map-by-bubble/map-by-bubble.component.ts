// @ts-nocheck
import { Component, Vue, Prop, Inject, Watch, Emit } from 'vue-property-decorator';
import ConnectomeService from '../../connectome.service';
import * as d3 from 'd3';
import { TYPE_NODE_IN_MAP } from '@/shared/constants/ds-constants';
import { emit } from 'process';

const API_PATH = 'api/connectome';
@Component({})
export default class ConnectomeMapByBubble extends Vue {
  @Prop(String) connectomeId: string | undefined;
  @Prop(Object) connectomeMap:
    | {
        connectomeId: string;
        dataset: Object;
        color: any;
        colorLabel: any;
      }
    | undefined;
  @Inject('connectomeService')
  private connectomeService: () => ConnectomeService;

  private dataSet: any;
  private color: any;
  private labelColor: any;
  private plotWrapper: d3.Selection<SVGGElement, d3.HierarchyCircularNode<unknown>, SVGGElement, unknown> = null;

  @Watch('connectomeMap')
  onConnectomeMapChanged(newConnectomeMap: { connectomeId: string; dataset: Object; color: any; colorLabel: any }) {
    if (!newConnectomeMap) {
      return;
    }
    console.log('onConnectomeMapChanged newConnectomeId', newConnectomeMap.connectomeId);
    console.log('onConnectomeMapChanged dataset', newConnectomeMap.dataset);
    const mapNodeAndColor = new Map();
    this.nodesById = new Map();
    this.dataSet = newConnectomeMap.dataset;
    this.color = newConnectomeMap.color;
    this.labelColor = newConnectomeMap.colorLabel;
    // const mapNodesById = this.mapNodesById;
    // const nodesById = this.nodesById

    this.renderMap(this.color, this.labelColor, mapNodeAndColor);
  }

  renderConnectomeMap(newConnectomeMap: { connectomeId: string; dataset: Object; color: any; colorLabel: any }) {
    if (!newConnectomeMap) {
      return;
    }
    console.log('renderConnectomeMap newConnectomeId', newConnectomeMap.connectomeId);
    console.log('renderConnectomeMap dataset', newConnectomeMap.dataset);
    const mapNodeAndColor = new Map();
    this.nodesById = new Map();
    this.dataSet = newConnectomeMap.dataset;
    this.color = newConnectomeMap.color;
    this.labelColor = newConnectomeMap.colorLabel;

    this.renderMap(this.color, this.labelColor, mapNodeAndColor);
  }

  renderMap(color: any, labelColor: any, mapNodeAndColor: any) {
    let diameter,
      commaFormat = d3.format(','),
      root,
      Tooltip,
      allOccupations = [],
      focus,
      focus0,
      k0,
      scaleFactor,
      barsDrawn = false,
      multicolor = true,
      hexcolor = '#0099cc',
      rotationText = [
        -14, 4, 23, -18, -10.5, -20, 20, 20, 46, -30, -25, -20, 20, 15, -30, -15, -45, 12, -15, -16, 15, 15, 5, 18, 5, 15, 20, -20, -25,
      ]; //The rotation of each arc tex
    const vue = this;
    //function drawAll() {
    //////////////////////////////////////////////////////////////
    ////////////////// Create Set-up variables  //////////////////
    //////////////////////////////////////////////////////////////

    const width = Math.max($('#bubble-chart').width(), 350),
      height = window.innerWidth < 768 ? width : window.innerHeight;

    const mobileSize = window.innerWidth < 768 ? true : false;

    //////////////////////////////////////////////////////////////
    /////////////////////// Create SVG  ///////////////////////
    //////////////////////////////////////////////////////////////

    const svg = d3
      .select('#bubble-chart')
      .append('svg')
      .attr('width', width)
      .attr('height', height)
      .append('g')
      .attr('transform', 'translate(' + width / 2 + ',' + height / 2 + ')');

    //////////////////////////////////////////////////////////////
    /////////////////////// Create Scales  ///////////////////////
    //////////////////////////////////////////////////////////////
    diameter = Math.min(width, height);

    const pack = data =>
      d3.pack().size([width, height]).padding(3)(
        d3
          .hierarchy(data)
          .sum(d => (d.size <= 2 ? 2 : d.size))
          .sort(d => d.data.topic_id)
      );
    Tooltip = d3
      .select('#bubble-chart')
      .append('div')
      .style('opacity', 0)
      .attr('class', 'tooltip')
      .style('background-color', 'white')
      .style('border', 'solid')
      .style('border-width', '2px')
      .style('border-radius', '5px')
      .style('padding', '5px');

    //////////////////////////////////////////////////////////////
    //////// Function | Draw the bars inside the circles /////////
    //////////////////////////////////////////////////////////////

    function drawBars() {
      //Inside each wrapper create the complete bar chart
      d3.selectAll('.barWrapperOuter').each(function (d, i) {
        if (d.children == undefined || d.children.length < 0) {
          barsDrawn = true;

          //Save current circle data in separate variable
          const current = d;

          //Title inside circle
          d3.select(this)
            .append('text')
            .attr('class', 'innerCircleTitle')
            // .attr('y', function (d, i) {
            //   d.titleHeight = (-0.5 + 0.35) * current.r;
            //   return d.titleHeight;
            // })
            // .attr('dy', '0em')
            .text(function (d, i) {
              return d.data.label;
            })
            .style('fill', 'white')
            .style('font-size', function (d) {
              //Calculate best font-size
              d.fontTitleSize = current.r / 10; //this.getComputedTextLength() * 20;
              return Math.round(d.fontTitleSize + 50) + 'px';
            })
            .each(function (d) {
              d.textLength = current.r * 2 * 0.7;
              wrap(this, d.textLength);
            });
        }
      }); //each barWrapperOuter
    } //drawBars

    //////////////////////////////////////////////////////////////
    ///////////////// Function | Initiates ///////////////////////
    //////////////////////////////////////////////////////////////

    //Create the bars inside the circles
    function runCreateBars() {
      // create a deferred object
      const r = $.Deferred();

      let counter = 0;
      while (!barsDrawn && counter < 10) {
        drawBars();
        counter = counter + 1;
      }

      setTimeout(function () {
        // and call `resolve` on the deferred object, once you're done
        r.resolve();
      }, 100);
      // return the deferred object
      return r;
    }

    //Call to the zoom function to move everything into place
    function runAfterCompletion() {
      focus0 = root;
      k0 = 1;
      d3.select('#loadText').remove();
      zoomTo(root);
    }

    //Hide the tooltip when the mouse moves away
    function removeTooltip() {
      Tooltip.style('opacity', 0);
      // d3.select(this).style('stroke', 'none').style('opacity', 0.8);
      // d3.select(this).style('opacity', 0.8);
    }

    //Show the tooltip on the hovered over slice
    function showTooltip(event, d) {
      const [x, y] = d3.pointer(event, d);
      Tooltip.html("<p class='nodeTooltip'>" + d.data.label + "</p><span class='nodeTooltip'> Value: " + d.value + '</span>')
        .style('top', event.layerY + 'px')
        .style('left', x + 'px');

      Tooltip.style('opacity', 1);
      d3.select(this).style('opacity', 1);
    }

    //////////////////////////////////////////////////////////////
    /////////// Read in Occupation Circle data ///////////////////
    //////////////////////////////////////////////////////////////
    const nodes = pack(this.dataSet);
    root = nodes;
    focus = nodes;
    //this.mapNodesById(dataSet);

    //////////////////////////////////////////////////////////////
    /////////// Create a wrappers for each occupation ////////////
    //////////////////////////////////////////////////////////////
    this.plotWrapper = svg
      .selectAll('g')
      .data(nodes)
      .enter()
      .append('g')
      .attr('class', 'plotWrapper')
      // .attr('stroke', function (d) {
      //   return d == root ? 'rgb(31, 119, 180)' : null;
      // })
      .attr('id', function (d, i) {
        allOccupations[i] = d.data.label;
        if (d.data.topic_id != undefined) return 'plotWrapper_' + d.data.topic_id;
        else return 'plotWrapper_node';
      });

    if (!mobileSize) {
      //Mouseover only on leaf nodes
      this.plotWrapper
        .filter(function (d) {
          return typeof d.children === 'undefined';
        })
        .on('mouseover', showTooltip)
        .on('mouseout', removeTooltip);
    } //if

    //////////////////////////////////////////////////////////////
    ///////////////////// Draw the circles ///////////////////////
    //////////////////////////////////////////////////////////////
    const circle = this.plotWrapper
      .append('circle')
      .attr('id', 'nodeCircle')
      .attr('class', function (d, i) {
        return d.parent ? (d.children ? 'node' : 'node node--leaf') : 'node node--root';
      })
      // .style('fill', function (d) {
      //   return d.children ? setCircleColor(d) : null;
      // })
      .style('fill', setCircleColor)
      .attr('stroke', setStrokeColor)
      .attr('r', function (d) {
        if (d.data.topic_id === 'CIDSALTLUXHQKAY-n0') scaleFactor = d.value / (d.r * d.r);
        return d.r;
      })
      .on('click', (event, d) => {
        if (focus !== d) {
          console.log(event);
          vue.onLeftClickOnNode(d);
          zoomTo(d);
        } else zoomTo(root);
      })
      .on('contextmenu', (event, d) => {
        console.log(event);
        console.log(d);
        vue.onRightClickOnNode(d);
        event.preventDefault();
      });

    /////////////////////////////////////////////////////////////
    //////// Draw the titles of parent circles on the Arcs ///////
    //////////////////////////////////////////////////////////////

    //Create the data for the parent circles only
    const overlapNode = [];
    circle
      .filter(function (d, i) {
        return d3.select(this).attr('class') === 'node';
      })
      .each(function (d, i) {
        overlapNode[i] = {
          label: d.data.label,
          depth: d.depth,
          r: d.r,
          x: d.x,
          y: d.y,
          topic_id: d.data.topic_id,
        };
      });

    //Create a wrapper for the arcs and text
    const hiddenArcWrapper = svg.append('g').attr('class', 'hiddenArcWrapper').style('opacity', 0);
    //Create the arcs on which the text can be plotted - will be hidden
    const hiddenArcs = hiddenArcWrapper
      .selectAll('.circleArcHidden')
      .data(overlapNode)
      .enter()
      .append('path')
      .attr('class', 'circleArcHidden')
      .attr('id', function (d, i) {
        return 'circleArc_' + i;
      })
      .attr('d', function (d, i) {
        return 'M ' + -d.r + ' 0 A ' + d.r + ' ' + d.r + ' 0 0 1 ' + d.r + ' 0';
      })
      .style('fill', 'none');
    //Append the text to the arcs
    const arcText = hiddenArcWrapper
      .selectAll('.circleText')
      .data(overlapNode)
      .enter()
      .append('text')
      .attr('class', 'circleText')
      .style('fill', (d, i) => setColorTextTopic(circle, d))
      .style('font-size', function (d) {
        //Calculate best font-size
        d.fontSize = d.r / 10;
        const fontSize = d.fontSize > 15 ? d.fontSize : 15;
        return Math.round(d.fontSize) + 'px';
      })
      .append('textPath')
      .attr('startOffset', '50%')
      .attr('xlink:href', function (d, i) {
        return '#circleArc_' + i;
      })
      .text(function (d) {
        return d.label ? d.label.replace(/ and /g, ' & ') : d.label;
      });

    //////////////////////////////////////////////////////////////
    ////////////////// Draw the Bar charts ///////////////////////
    //////////////////////////////////////////////////////////////

    //Create a wrapper for everything inside a leaf circle
    const barWrapperOuter = this.plotWrapper
      .append('g')
      .attr('id', function (d) {
        if (d.data.topic_id != undefined) return d.data.topic_id;
        else return 'node';
      })
      .style('opacity', 0)
      .attr('class', 'barWrapperOuter');

    // call runCreateBars and use the `done` method
    // with `runAfterCompletion` as it's parameter
    setTimeout(function () {
      runCreateBars().done(runAfterCompletion);
    }, 100);
    //} //drawAll
    //////////////////////////////////////////////////////////////
    //////////////////// The zoom function ///////////////////////
    //////////////////////////////////////////////////////////////

    //The zoom function
    //Change the sizes of everything inside the circle and the arc texts
    function zoomTo(d) {
      focus = d;

      const v = [focus.x, focus.y, focus.r * 2.05],
        k = diameter / v[2];
      //Remove the tspans of all the titles
      d3.selectAll('.innerCircleTitle').selectAll('tspan').remove();

      //Hide the bar charts, then update them
      d3.selectAll('.barWrapperOuter').transition().duration(0).style('opacity', 0);

      //Hide the node titles, update them
      d3.selectAll('.hiddenArcWrapper').transition().duration(0).style('opacity', 0).call(endall, changeReset);

      function changeReset() {
        //////////////////////////////////////////////////////////////
        /////////////// Change titles on the arcs ////////////////////
        //////////////////////////////////////////////////////////////

        //Update the arcs with the new radii
        d3.selectAll('.hiddenArcWrapper')
          .selectAll('.circleArcHidden')
          .attr('d', function (d, i) {
            return 'M ' + -d.r * k + ' 0 A ' + d.r * k + ' ' + d.r * k + ' 45 0 1 ' + d.r * k + ' 0';
          })
          .attr('transform', function (d, i) {
            const rotate = rotationText[Math.floor(Math.random() * rotationText.length)];
            return 'translate(' + (d.x - v[0]) * k + ',' + (d.y - v[1]) * k + ')rotate(' + rotate + ')';
          });

        //Save the names of the circle itself and first children
        const kids = [d.data.label];
        if (typeof d.children !== 'undefined') {
          for (let i = 0; i < d.children.length; i++) {
            kids.push(d.children[i].label);
          }
        } //if

        //Update the font sizes and hide those not close the the parent
        d3.selectAll('.hiddenArcWrapper')
          .selectAll('.circleText')
          .style('font-size', function (d) {
            const fontSize = d.fontSize * k > 11 ? d.fontSize * k : 11;
            return Math.round(fontSize + 2) + 'px';
          })
          .style('opacity', function (d) {
            return $.inArray(d.label, kids) >= 0 ? 1 : 0;
          });

        //The title inside the circles
        d3.selectAll('.innerCircleTitle')
          // .style("display", "none")
          //If the font-size becomes to small do not show it or if the ID does not start with currentID
          // .filter(function (d) {
          //   return Math.round(d.fontTitleSize * k) > 4;
          // })
          .style('display', null)
          // .attr('y', function (d) {
          //   return d.titleHeight * k;
          // })
          .style('font-size', function (d) {
            const fontSize = d.fontTitleSize * k > 11 ? d.fontTitleSize * k : 11;
            return Math.round(fontSize) + 'px';
          })
          .style('text-shadow', (d, i) => {
            const color = setLabelColor(d);

            return (
              color +
              ' 3px 0px 0px,' +
              color +
              ' -3px 0px 0px,' +
              color +
              ' 0px 3px 0px, ' +
              color +
              ' 0px -3px 0px,' +
              color +
              ' 2px 2px,' +
              color +
              ' -2px -2px 0px,' +
              color +
              ' 2px -2px 0px,' +
              color +
              ' -2px 2px 0px'
            );
          })
          .text(function (d, i) {
            return 'Total ' + commaFormat(d.size) + ' | ' + d.data.label;
          })
          .each(function (d) {
            wrap(this, k * d.textLength);
          });

        setTimeout(function () {
          changeLocation(d, v, k);
        }, 50);
      } //changeReset
    } //zoomTo

    //Move to the new location - called by zoom
    function changeLocation(d, v, k) {
      //////////////////////////////////////////////////////////////
      //////////////// Overal transform and resize /////////////////
      //////////////////////////////////////////////////////////////
      //Calculate the duration
      //If they are far apart, take longer
      //If it's a big zoom in/out, take longer
      const duration = 1750;

      //Transition the circles to their new location
      d3.selectAll('.plotWrapper')
        .transition()
        .duration(duration)
        .attr('transform', function (d) {
          return 'translate(' + (d.x - v[0]) * k + ',' + (d.y - v[1]) * k + ')';
        });

      //Transition the circles to their new size
      d3.selectAll('#nodeCircle')
        .transition()
        .duration(duration)
        .attr('r', function (d) {
          //Found on http://stackoverflow.com/questions/24293249/access-scale-factor-in-d3s-pack-layout
          if (d.data.topic_id === 'METAVERSE-n0') scaleFactor = d.value / (d.r * d.r * k * k);
          return d.r * k;
        });

      setTimeout(function () {
        //Hide the node titles, update them at once and them show them again
        d3.selectAll('.hiddenArcWrapper').transition().duration(1000).style('opacity', 1);

        //Hide the bar charts, then update them at once and show the magain
        d3.selectAll('.barWrapperOuter').transition().duration(1000).style('opacity', 1);

        focus0 = focus;
        k0 = k;
      }, duration);
    } //changeSizes

    //////////////////////////////////////////////////////////////
    ///////////////////// Helper Functions ///////////////////////
    //////////////////////////////////////////////////////////////

    //Wraps SVG text - Taken from http://bl.ocks.org/mbostock/7555321
    function wrap(text, width) {
      let textd3 = d3.select(text),
        words = textd3.text().split(/\s+/).reverse(),
        currentSize = +textd3.style('font-size').replace('px', ''),
        word,
        line = [],
        lineNumber = 0,
        lineHeight = 1.2, // ems
        extraHeight = 0.2,
        y = textd3.attr('y'),
        dy = parseFloat(textd3.attr('dy')),
        //First span is different - smaller font
        tspan = textd3
          .text(() => {
            if (!words || words.length == 0) {
              return '';
            }
            return clipText(width, words[0], 4, currentSize);
          })
          .append('tspan')
          .attr('class', 'subTotal')
          .attr('x', 0)
          // .attr('y', y)
          .style('fill', 'white')
          .style('stroke', setStrokeColor)
          // .attr('dy', dy + 'em')
          .style('font-size', (Math.round(currentSize * 0.5) <= 5 ? 0 : Math.round(currentSize * 0.5)) + 'px');
    } //wrap

    function isNodeChildOfRoot(node) {
      if (!root || !node) {
        return false;
      }
      return root.children.findIndex(item => node.data.topic_id == item.data.topic_id) > -1;
    }

    function setCircleColor(obj) {
      if (isNodeChildOfRoot(obj)) {
        return 'transparent';
      }
      const depth = obj.depth;
      while (obj.depth > 1) {
        obj = obj.parent;
      }
      const newcolor = multicolor ? d3.hsl(color(obj.data.label)) : d3.hsl(hexcolor);
      newcolor.l += depth == 1 ? 0 : depth * 0.09;
      return newcolor;
    }

    function setLabelColor(obj) {
      if (isNodeChildOfRoot(obj)) {
        return 'transparent';
      }
      while (obj.depth > 1) {
        obj = obj.parent;
      }
      const newcolor = multicolor ? d3.hsl(labelColor(obj.data.label)) : d3.hsl(hexcolor);
      return newcolor;
    }

    function setColorTextTopic(circle, d) {
      return d3.hsl(labelColor(d.label)) || 'white';
    }

    function setStrokeColor(obj) {
      if (isNodeChildOfRoot(obj)) {
        while (obj.depth > 1) {
          obj = obj.parent;
        }
        const strokecolor = multicolor ? d3.hsl(color(obj.data.label)) : d3.hsl(hexcolor);
        mapNodeAndColor.set(obj.data.topic_id, strokecolor);
        return strokecolor;
      }
      return null;
    }

    function clipText(r, label, scale, fontsize) {
      if (r < fontsize / scale) {
        return '';
      }
      let name = label.substring(0, r / scale);
      if (name.length < label.length) {
        name = name.substring(0, name.length - Math.min(2, name.length)) + '...';
      }
      return name;
    }

    //Calls a function only after the total transition ends
    function endall(transition, callback) {
      let n = 0;
      transition
        .each(function () {
          ++n;
        })
        .on('end', function () {
          if (!--n) callback.apply(this, arguments);
        });
    } //endall

    //drawAll();
    //this.onNodeClick()
  }

  data() {
    return {};
  }

  mounted() {}

  @Emit('showTopicBar')
  showTopicBar(elementId: string) {
    return elementId;
  }

  focusOnNode(targetNode: { type: TYPE_NODE_IN_MAP; label: string; id: string; parent: { topic_id: string; label: string } }) {
    if (!targetNode) {
      return;
    }
    console.log('targetNode', targetNode);
    this.plotWrapper.selectAll('circle').each(element => {
      let elementType = element.data.entities && element.data.entities.length === 1 ? TYPE_NODE_IN_MAP.ENTITY : TYPE_NODE_IN_MAP.CLUSTER;
      if (targetNode.type === elementType && element.data.label === targetNode.label) {
        if (targetNode.parent && targetNode.parent.label && targetNode.parent.topic_id) {
          if (
            element.parent &&
            element.parent.data.label === targetNode.parent.label &&
            element.parent.data.topic_id === targetNode.parent.topic_id
          ) {
            console.log(element);
          }
        } else {
          console.log(element);
        }
      }
    });
  }

  @Emit('onLeftClickOnMapNode')
  onLeftClickOnNode(node: any) {
    if (!node) {
      return;
    }

    if (!node.data) {
      return;
    }

    const nodeClicked: { id: string; label: string; type: TYPE_NODE_IN_MAP; parent: { topic_id: string; label: string } } = {
      id: node.data.topic_id,
      label: node.data.label,
      type: node.data.entities && node.data.entities.length === 1 ? TYPE_NODE_IN_MAP.ENTITY : TYPE_NODE_IN_MAP.CLUSTER,
    };

    if (node.parent) {
      nodeClicked.parent = { topic_id: node.parent.data.topic_id, label: node.parent.data.label };
    }
    return nodeClicked;
  }

  @Emit('onRightClickOnMapNode')
  onRightClickOnNode(node: any) {
    if (!node) {
      return;
    }

    if (!node.data) {
      return;
    }

    const nodeClicked: { id: string; label: string; type: TYPE_NODE_IN_MAP; parent: { topic_id: string; label: string } } = {
      id: node.data.topic_id,
      label: node.data.label,
      type: node.data.entities && node.data.entities.length === 1 ? TYPE_NODE_IN_MAP.ENTITY : TYPE_NODE_IN_MAP.CLUSTER,
    };

    if (node.parent) {
      nodeClicked.parent = { topic_id: node.parent.data.topic_id, label: node.parent.data.label };
    }
    return nodeClicked;
  }
}
