<template>
  <div>
     <div class="button-container">
       <button @click="clearAll" class="button hide">Clear</button>
       <button @click="exportStage" class="export">Export Your Drawing</button>
       <button @click="addElipse" class="button shape"><b>Ellipse</b></button>
       <button @click="addCircle" class="button shape"><b>Circle</b></button>
       <button @click="addTriangle" class="button shape"><b>Triangle</b></button>
       <button @click="addRectangle" class="button shape"><b>Rectangle</b></button>
       <button @click="addSquare" class="button shape"><b>Square</b></button>
       <button @click="addStraightLine" class="button shape"><b>Straight Line</b></button>
       <button class="button undo" @click="undo"><b>Undo</b></button>
       <button class="button redo" @click="redo"><b>Redo</b></button>
       <button @click="clone" class="button"><b>Copy</b></button>
       <button @click="changeColor" class="button shape"><b>Set color</b></button>
       <button @click="savexml" class="button xml save"><b>Save...XML</b></button>
       <button @click="savejson" class="button  json save"><b>Save.JSON</b></button>
       <button @click="loadxml" class="button load xml"><b>Load...XML </b></button>
       <button @click="loadjson" class="button load json"><b>Load.JSON</b></button>
 
       <div class="color-picker-container">
        <Picker  v-model="color"/>
       </div>
     <div class="save-container">
       <InputText type="text" id="path" v-model="saveText" />
       <label for="path" v-if="!issaveTextNotEmpty">Enter the desired path</label>
     </div>
        <div class="load-container">
       <InputText type="text" id="path" v-model="loadText" />
       <label for="path" v-if="!isloadTextNotEmpty">Open existing file</label>
     </div> 
 
     </div>
     <div class="screen-container">
       <div ref="container" class="display-screen" @dblclick="deleteShape"></div>
     </div>
   </div>
 </template>
 
 <script>
 import Konva from 'konva';
 import ColorPicker from 'primevue/colorpicker'
 import InputText from 'primevue/inputtext';
 import { Ellipse } from 'konva/lib/shapes/Ellipse';
 import axios from "axios";
 
 export default {
   components: {
     InputText,
   },
   data() {
     return {
       shapearray:[],
       map:{},
       curentShape:null,
       shapes :0,
       x:100,
       y:100,
       fill:'#0047b3',
       width:200,
       height:100,
       radius:80,
       strokeWidth:2,
       drawingLine: false,
       line: null,
       startPoint: { x: 0, y: 0 },
       endPoint: { x: 0, y: 0 },
       tr : new Konva.Transformer(),
       color: null,
       saveText: '',
       loadText:'',
     };
   },
   computed: {
     issaveTextNotEmpty() {
       return this.saveText.trim() !== '';
     },
     isloadTextNotEmpty(){
       return this.loadText.trim() !=='';
     }
     ,
   },
   mounted() {
     
     const screenWidth = 2355;
     const screenHeight = 850;
 
     this.stage = new Konva.Stage({
       container: this.$refs.container,
       width: screenWidth,
       height: screenHeight,
     });
 
     this.stage.on('dragend', (event) => {
       const shape = event.target;
       this.onModify(shape);
     });
     this.layer = new Konva.Layer();
     this.stage.add(this.layer);
     this.layer.add(this.tr);
   }, 
   methods: {
     undo1(){
       if(this.undo.length>0){
       const shape = this.undo.pop();
       this.redo.push(shape);
       shape.destroy();
       }
     },
     redo1(){
       if(this.redo.length>0){
       const shape=this.redo.pop();
       this.layer.add(shape);
       this.undo.push(shape);
       this.stage.batchDraw();
       }
     },
     savexml() {
       console.log(this.saveText);
       fetch("http://localhost:8081/save/xml", {
         method: "POST",
         body: JSON.stringify({
           path: this.saveText,
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
         
        
     },
 
     savejson() {
       console.log(this.saveText);
       fetch("http://localhost:8081/save/json", {
         method: "POST",
         body: JSON.stringify({
           path: this.saveText,
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
         
 
 
     },
     loadjson() {
       axios.post('http://localhost:8081/load/json',{path:this.loadText})
           .then(response => {
             console.log(response.data);
             let todraw = response.data;
             for(let i =0;i<this.shapearray.length;i++)
       {
          this.shapearray[i].destroy();
       }
 
  
 
           for (const shapeid in todraw){
 
              if(todraw[shapeid].type==="Ellipse")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
 
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width*scalex;
               this.height=todraw[shapeid].height*scaley;
               this.fill=todraw[shapeid].fill;
               
               const elipse = new Konva.Ellipse({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id:shapeid,
                });
               this.layer.add(elipse);
               this.shapearray.push(elipse);
               this.addDoubleClickEvent(elipse);
 
 
              }
              else if(todraw[shapeid].type==="Circle")
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const circle = new Konva.Circle({
               x: this.x, 
               y: this.y,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(circle);
             this.shapearray.push(circle);
             this.addDoubleClickEvent(circle);
 
 
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===3)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const triangle = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               sides: 3,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(triangle);
             this.shapearray.push(triangle);
             this.addDoubleClickEvent(triangle);
 
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===4)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const square = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               rotation:45,
               sides: 4,
               radius: this.radius,
               fill: this.fill,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               opacity: 0.7,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(square);
             this.shapearray.push(square);
             this.addDoubleClickEvent(square);
 
              }
              else if(todraw[shapeid].type==="Rectangle")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width;
               this.height=todraw[shapeid].height;
               this.fill=todraw[shapeid].fill;
               const rectangle = new Konva.Rect({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               rotation:0,
               id:shapeid,
 
             });
             this.layer.add(rectangle);
             this.shapearray.push(rectangle);
             this.addDoubleClickEvent(rectangle);
 
 
              }
              else{
               const stline = new Konva.Line({
               points: todraw[shapeid].points, // Initialize with zero-length line
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id: shapeid,
           });
 
             this.layer.add(stline);
             this.shapearray.push(stline);
             this.addDoubleClickEvent(stline);
 
              }
              this.shapes=shapeid;
 
 
 
           }
 
           this.stage.batchDraw();
           this.x = 100;
           this.y = 100;
           this.fill = '#0047b3';
           this.width = 200;
           this.height = 100;
           this.points=[100, 100, 200, 400];
 
           this.radius = 80;
         
           })
           .catch(error => {
             console.error('There was an error!', error);
           })
     },
     loadxml() {
       axios.post('http://localhost:8081/load/xml',{path:this.loadText})
           .then(response => {
             console.log(response.data);
             let todraw = response.data;
             for(let i =0;i<this.shapearray.length;i++)
       {
          this.shapearray[i].destroy();
       }
 
  
 
           for (const shapeid in todraw){
 
              if(todraw[shapeid].type==="Ellipse")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
 
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width*scalex;
               this.height=todraw[shapeid].height*scaley;
               this.fill=todraw[shapeid].fill;
               
               const elipse = new Konva.Ellipse({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id:shapeid,
                });
               this.layer.add(elipse);
               this.shapearray.push(elipse);
               this.addDoubleClickEvent(elipse);
 
 
              }
              else if(todraw[shapeid].type==="Circle")
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const circle = new Konva.Circle({
               x: this.x, 
               y: this.y,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(circle);
             this.shapearray.push(circle);
             this.addDoubleClickEvent(circle);
 
 
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===3)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const triangle = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               sides: 3,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(triangle);
             this.shapearray.push(triangle);
             this.addDoubleClickEvent(triangle);
 
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===4)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const square = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               rotation:45,
               sides: 4,
               radius: this.radius,
               fill: this.fill,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               opacity: 0.7,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(square);
             this.shapearray.push(square);
             this.addDoubleClickEvent(square);
 
              }
              else if(todraw[shapeid].type==="Rectangle")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width;
               this.height=todraw[shapeid].height;
               this.fill=todraw[shapeid].fill;
               const rectangle = new Konva.Rect({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               rotation:0,
               id:shapeid,
 
             });
             this.layer.add(rectangle);
             this.shapearray.push(rectangle);
             this.addDoubleClickEvent(rectangle);
 
 
              }
              else{
               const stline = new Konva.Line({
               points: todraw[shapeid].points, // Initialize with zero-length line
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id: shapeid,
           });
 
             this.layer.add(stline);
             this.shapearray.push(stline);
             this.addDoubleClickEvent(stline);
 
              }
              this.shapes=shapeid;
 
 
 
           }
 
           this.stage.batchDraw();
           this.x = 100;
           this.y = 100;
           this.fill = '#0047b3';
           this.width = 200;
           this.height = 100;
           this.points=[100, 100, 200, 400];
 
           this.radius = 80;
           
 
          
             
 
 
 
           })
           .catch(error => {
             console.error('There was an error!', error);
           })
     },
     changeColor() {
   if (this.tr.nodes().length > 0) {
     const selectedShape = this.tr.nodes()[0];
 
     // Check if the selected shape is a line
     if (selectedShape instanceof Konva.Line) {
       selectedShape.stroke('#' + this.color);
     } else {
       // For other shapes, change the fill color
       selectedShape.fill('#' + this.color);
     }
 
     this.stage.batchDraw();
     this.onModify(selectedShape);
   }
 },
 clone() {
   if (this.tr.nodes().length > 0) {
     this.x = this.tr.nodes()[0].x() + 50;
     this.y += this.tr.nodes()[0].y();
     this.fill = this.tr.nodes()[0].fill();
 
     if (this.tr.nodes()[0] instanceof Konva.Rect) {
       const scalex = this.tr.nodes()[0].scaleX();
       const scaley = this.tr.nodes()[0].scaleY();
       this.width = this.tr.nodes()[0].width() * scalex;
       this.height = this.tr.nodes()[0].height() * scaley;
       this.addRectangle();
     } else if (this.tr.nodes()[0] instanceof Konva.Ellipse) {
       const scalex = this.tr.nodes()[0].scaleX();
       const scaley = this.tr.nodes()[0].scaleY();
       this.width = this.tr.nodes()[0].width() * scalex;
       this.height = this.tr.nodes()[0].height() * scaley;
       this.addElipse();
     } else if (this.tr.nodes()[0] instanceof Konva.Circle) {
       const scale = this.tr.nodes()[0].scaleX();
       this.radius = this.tr.nodes()[0].radius() * scale;
       this.strokeWidth = this.tr.nodes()[0].strokeWidth() * scale;
       this.addCircle();
     } else if (this.tr.nodes()[0] instanceof Konva.RegularPolygon && this.tr.nodes()[0].sides() === 4) {
       const scale = this.tr.nodes()[0].scaleX();
       this.radius = this.tr.nodes()[0].radius() * scale;
       this.strokeWidth = this.tr.nodes()[0].strokeWidth() * scale;
       this.addSquare();
     } else if (this.tr.nodes()[0] instanceof Konva.RegularPolygon && this.tr.nodes()[0].sides() === 3) {
       const scale = this.tr.nodes()[0].scaleX();
       this.radius = this.tr.nodes()[0].radius() * scale;
       this.strokeWidth = this.tr.nodes()[0].strokeWidth() * scale;
       this.addTriangle();
     }
     else{
      console.log(this.tr.nodes()[0].points());
const originalPoints = this.tr.nodes()[0].points();
const temp = [...originalPoints]; // Create a deep copy of the original points array
temp[0] += 50;
temp[1] += 50;
temp[2] += 50;
temp[3] += 50;

this.shapes = this.shapes + 1;
const id = this.shapes;
const stline = new Konva.Line({
  points: temp,
  stroke: 'black',
  strokeWidth: 2,
  draggable: true,
  id: id,
});

fetch("http://localhost:8081/create", {
  method: "POST",
  body: JSON.stringify({
    type: "line",
    key: stline.id(),
    points: stline.points(),
    stroke: stline.stroke(),
    strokeWidth: stline.strokeWidth(),
    draggable: stline.draggable(),
  }),
  headers: {
    "Content-type": "application/json; charset=UTF-8",
  },
})
  .then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error("Network response was not ok");
    }
  })
  .then((json) => {
    // Handle the JSON response if needed
  })
  .catch((error) => {
    console.error("Fetch Error:", error);
  });

this.addDoubleClickEvent(stline);
this.layer.add(stline);
this.shapearray.push(stline);
this.stage.batchDraw(); // Update the stage
     }
 
 
     
     this.x = 100;
     this.y = 100;
     this.fill = '#0047b3';
     this.width = 200;
     this.height = 100;
     this.radius = 80;
   }
 },
 
     addRectangle() {
       this.shapes=this.shapes+1;
       const id = this.shapes; 
       const rectangle = new Konva.Rect({
         x: this.x, // Adjust the X position as needed
         y: this.y, // Adjust the Y position as needed
         width: this.width,
         height: this.height,
         fill: this.fill,
         opacity: 0.7,
         stroke: 'black',
         strokeWidth: this.strokeWidth,
         draggable: true,
         rotation:0,
         id:id,
 
       });
       fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "rectangle",
           key: rectangle.id(),
           width: rectangle.width(),
           height: rectangle.height(),
           fill: rectangle.fill(),
           opacity: rectangle.opacity(),
           stroke: rectangle.stroke(),
           strokeWidth: rectangle.strokeWidth(),
           draggable: rectangle.draggable(),
           position: { x: rectangle.x(), y: rectangle.y() },
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
          // console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
          // console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
 
      
 
       this.addDoubleClickEvent(rectangle);
       this.layer.add(rectangle);
       this.shapearray.push(rectangle);
      // this.undo.push(rectangle);
 
       this.stage.batchDraw(); // Update the stage
     },
     addCircle() {
       this.shapes=this.shapes+1;
       const id = this.shapes; 
       const circle = new Konva.Circle({
         x: this.x, 
         y: this.y,
         radius: this.radius,
         fill: this.fill,
         opacity: 0.7,
         stroke: 'black',
         strokeWidth: this.strokeWidth,
         draggable: true,
         id:id,
       });
       fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "circle",
           key: circle.id(),
           radius: circle.radius(),
           fill: circle.fill(),
           opacity: circle.opacity(),
           stroke: circle.stroke(),
           strokeWidth: circle.strokeWidth(),
           draggable: circle.draggable(),
           position: { x: circle.x(), y: circle.y() },
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           //console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
       this.addDoubleClickEvent(circle);
       this.layer.add(circle);
      // this.undo.push(circle);
      this.shapearray.push(circle);
 
       this.stage.batchDraw(); // Update the stage
     },
     addElipse() {
       this.shapes=this.shapes+1;
       const id = this.shapes; 
       const elipse = new Konva.Ellipse({
         x: this.x, // Adjust the X position as needed
         y: this.y, // Adjust the Y position as needed
         width: this.width,
         height: this.height,
         fill: this.fill,
         opacity: 0.7,
         stroke: 'black',
         strokeWidth: 2,
         draggable: true,
         id:id,
       });
       fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "ellipse",
           key: elipse.id(),
           width: elipse.width(),
           height: elipse.height(),
           fill: elipse.fill(),
           opacity: elipse.opacity(),
           stroke: elipse.stroke(),
           strokeWidth: elipse.strokeWidth(),
           draggable: elipse.draggable(),
           position: { x: elipse.x(), y: elipse.y() },
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
          // console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
          // console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
       this.addDoubleClickEvent(elipse);
       this.layer.add(elipse);
      // this.undo.push(elipse);
      this.shapearray.push(elipse);
       this.stage.batchDraw(); // Update the stage
     },
     addStraightLine() {
       console.log("here");
       this.shapes=this.shapes+1;
       const id = this.shapes; 
       const stline = new Konva.Line({
           points: [100, 100, 200, 400], // Initialize with zero-length line
           stroke: 'black',
           strokeWidth: 2,
           draggable: true,
           id: id,
       });
       fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "line",
           key:  stline.id(),
           points: stline.points(),
           stroke:  stline.stroke(),
           strokeWidth:  stline.strokeWidth(),
           draggable:  stline.draggable(),
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           //console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
       this.addDoubleClickEvent(stline);
       this.layer.add(stline);
      // this.undo.push(stline);
      this.shapearray.push(stline);
 
       this.stage.batchDraw(); // Update the stage
 
     },
     addTriangle() {
       this.shapes=this.shapes+1;
       const id = this.shapes; 
       const triangle = new Konva.RegularPolygon({
         x: this.x, // Adjust the X position as needed
         y: this.y, // Adjust the Y position as needed
         sides: 3,
         radius: this.radius,
         fill: this.fill,
         opacity: 0.7,
         stroke: 'black',
         strokeWidth: this.strokeWidth,
         draggable: true,
         id:id,
       });
       const numberOfSides = parseInt(triangle.sides());
 
       fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "polygon",
           key: triangle.id() ,
           radius: triangle.radius(),
           fill: triangle.fill(),
           opacity: triangle.opacity(),
           stroke: triangle.stroke(),
           strokeWidth: triangle.strokeWidth(),
           draggable: triangle.draggable(),
           position: { x: triangle.x(), y: triangle.y() },
           numberOfSides: 3,
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
          // console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
 
       this.addDoubleClickEvent(triangle);
       this.layer.add(triangle);
       //this.undo.push(triangle);
       this.shapearray.push(triangle);
 
       this.stage.batchDraw(); // Update the stage
     },
     addSquare() {
       this.shapes=this.shapes+1;
       const id = this.shapes; 
       const square = new Konva.RegularPolygon({
         x: this.x, // Adjust the X position as needed
         y: this.y, // Adjust the Y position as needed
         rotation:45,
         sides: 4,
         radius: this.radius,
         fill: this.fill,
         stroke: 'black',
         strokeWidth: this.strokeWidth,
         opacity: 0.7,
         draggable: true,
         id:id,
       });
       fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "polygon",
           key: square.id(),
           radius: square.radius(),
           fill: square.fill(),
           opacity: square.opacity(),
           stroke: square.stroke(),
           strokeWidth: square.strokeWidth(),
           draggable: square.draggable(),
           position: { x: square.x(), y: square.y() },
           numberOfSides: 4,
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           //console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
 
       this.addDoubleClickEvent(square);
       this.layer.add(square);
      // this.undo.push(square);
      this.shapearray.push(square);
 
       this.stage.batchDraw(); // Update the stage
     },
     onModify(shape) {
       if (shape instanceof Konva.Circle) {
         fetch("http://localhost:8081/create", {
           method: "POST",
           body: JSON.stringify({
             type: "circle",
             key: shape.id(),
             radius: shape.radius(),
             fill: shape.fill(),
             opacitY: shape.opacity(),
             stroke: shape.stroke(),
             strokeWidth: shape.strokeWidth(),
             draggable: shape.draggable(),
             position: { x: shape.x(), y: shape.y() },
             scaleX:shape.scaleX(),
             scaleY: shape.scaleY(),
             rotation:shape.rotation(),
           }),
           headers: {
             "Content-type": "application/json; charset=UTF-8",
           },
         })
           .then((response) => {
             // Display the status code in the console for debugging
            // console.log("Response Status:", response.status);
 
             // Check if the response status is OK (200)
             if (response.ok) {
               return response.json();
             } else {
               // If the response status is not OK, throw an error
               throw new Error("Network response was not ok");
             }
           })
           .then((json) => {
             // Display the JSON response in the console for debugging
             //console.log("Response JSON:", json);
           })
           .catch((error) => {
             // Display any errors in the console for debugging
             console.error("Fetch Error:", error);
           });
       }
       if(shape instanceof Konva.Rect){
         fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "rectangle",
           key: shape.id(),
           width:shape.width(),
           height: shape.height(),
           fill: shape.fill(),
           opacity: shape.opacity(),
           stroke: shape.stroke(),
           strokeWidth: shape.strokeWidth(),
           draggable: shape.draggable(),
           position: { x:  shape.x(), y:  shape.y() },
           scaleX:shape.scaleX(),
             scaleY: shape.scaleY(),
             rotation:shape.rotation(),
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           //console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
       }
       if(shape instanceof Konva.Ellipse){
         fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "ellipse",
           key:  shape.id(),
           width:  shape.width(),
           height:  shape.height(),
           fill:  shape.fill(),
           opacity:  shape.opacity(),
           stroke:  shape.stroke(),
           strokeWidth:  shape.strokeWidth(),
           draggable:  shape.draggable(),
           position: { x:  shape.x(), y:  shape.y() },
           scaleX:shape.scaleX(),
             scaleY: shape.scaleY(),
             rotation:shape.rotation(),
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           //console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
 
       }
       if (shape instanceof Konva.RegularPolygon && shape.sides() === 3) {
         console.log(shape.id())
         fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "polygon",
           key:  shape.id(),
           radius: shape.radius(),
           fill:  shape.fill(),
           opacity:  shape.opacity(),
           stroke:  shape.stroke(),
           strokeWidth: shape.strokeWidth(),
           draggable:  shape.draggable(),
           position: { x:  shape.x(), y:  shape.y() },
           numberOfSides: 3,
           scaleX:shape.scaleX(),
             scaleY: shape.scaleY(),
             rotation:shape.rotation(),
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
          // console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
          // console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
  
       }
       if(shape instanceof Konva.RegularPolygon && shape.sides()===4){
         fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "polygon",
           key:  shape.id(),
           radius: shape.radius(),
           fill:  shape.fill(),
           opacity:  shape.opacity(),
           stroke:  shape.stroke(),
           strokeWidth:  shape.strokeWidth(),
           draggable:  shape.draggable(),
           position: { x:  shape.x(), y:  shape.y() },
           numberOfSides: 4,
           scaleX:shape.scaleX(),
             scaleY: shape.scaleY(),
             rotation:shape.rotation(),
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
           //console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
       }
       if(shape instanceof Konva.Line){
         fetch("http://localhost:8081/create", {
         method: "POST",
         body: JSON.stringify({
           type: "line",
           key:  shape.id(),
           points: shape.points(),
           stroke:  shape.stroke(),
           strokeWidth:  shape.strokeWidth(),
           draggable:  shape.draggable(),
           scaleX:shape.scaleX(),
           scaleY: shape.scaleY(),
           rotation:shape.rotation(),
         }),
         headers: {
           "Content-type": "application/json; charset=UTF-8",
         },
       })
         .then((response) => {
           // Display the status code in the console for debugging
           //console.log("Response Status:", response.status);
 
           // Check if the response status is OK (200)
           if (response.ok) {
             return response.json();
           } else {
             // If the response status is not OK, throw an error
             throw new Error("Network response was not ok");
           }
         })
         .then((json) => {
           // Display the JSON response in the console for debugging
          // console.log("Response JSON:", json);
         })
         .catch((error) => {
           // Display any errors in the console for debugging
           console.error("Fetch Error:", error);
         });
       }
     
     },
   addDoubleClickEvent(shape) {
   shape.on('click', () => {
     shape.moveToTop();
     if (!(shape instanceof Konva.Line)) {
       this.color = shape.fill().slice(1);
     } else {
       this.color = shape.stroke();
     }
     if (shape instanceof Konva.Circle || (shape instanceof Konva.RegularPolygon)) {
       const anchors = ['top-left', 'top-right', 'bottom-left', 'bottom-right'];
       this.tr.setAttr('keepRatio', true);
       this.tr.enabledAnchors(anchors);
       this.tr.nodes([shape]);
     } else {
       this.tr.setAttr('keepRatio', false);
       this.tr.enabledAnchors(Konva.Transformer.prototype._resetAnchors);
       this.tr.nodes([shape]);
     }
     this.stage.batchDraw();
   });
 
   this.stage.on('click', (e) => {
     if (e.target === this.stage) {
       if (this.tr.nodes().length > 0) {
         // Commented out to prevent calling onModify when clicking outside the shape
         // this.onModify(this.tr.nodes()[0]);
       }
       this.tr.nodes([]);
       return;
     }
   });
 
   shape.on('dblclick', () => {
     this.curentShape = shape.id();
     console.log("curr", this.curentShape)
     shape.destroy();
     this.tr.nodes([]);
     this.stage.batchDraw();
     
 
 // Make a fetch request to delete the shape
 fetch(`http://localhost:8081/delete/${encodeURIComponent(this.curentShape)}`, {
   method: 'GET',
 })
 
     .then((response) => {
         // Display the status code in the console for debugging
         console.log("Response Status:", response.status);
 
         // Check if the response status is OK (200)
         if (response.ok) {
             // Check if the response has a Content-Type of 'application/json'
             const contentType = response.headers.get("content-type");
             if (contentType && contentType.includes("application/json")) {
                 return response.json();
             } else {
                 // If the response does not contain JSON, return an empty object
                 return {};
             }
         } else {
             // If the response status is not OK, throw an error with additional details
             throw new Error(`Network response was not ok. Status: ${response.status}`);
         }
     })
     .then((json) => {
         // Display the JSON response in the console for debugging
         //console.log("Response JSON:", json);
     })
     .catch((error) => {
         // Display detailed error information in the console for debugging
         console.error("Fetch Error:", error.message);
     });
 });
 
   // Handle transformer events
   this.tr.on('transformend', () => {
     // Call onModify when the transformation is finished
     if (this.tr.nodes().length > 0) {
       this.onModify(this.tr.nodes()[0]);
     }
   });
 },
     clearAll() {
       this.shapes=0;
       
       for(let i =0;i<this.shapearray.length;i++)
       {
          this.shapearray[i].destroy();
       }
       //this.layer.destroyChildren();
       this.stage.batchDraw();
       
   fetch("http://localhost:8081/clear")
     .then((response) => {
       // Display the status code in the console for debugging
      // console.log("Response Status:", response.status);
 
       // Check if the response status is OK (200)
       if (response.ok) {
         // Check if the response has a Content-Type of 'application/json'
         const contentType = response.headers.get("content-type");
         if (contentType && contentType.includes("application/json")) {
           return response.json();
         } else {
           // If the response does not contain JSON, return an empty object
           return {};
         }
       } else {
         // If the response status is not OK, throw an error
         throw new Error("Network response was not ok");
       }
     })
     .then((json) => {
       // Display the JSON response in the console for debugging
       //console.log("Response JSON:", json);
     })
     .catch((error) => {
       // Display any errors in the console for debugging
       console.error("Fetch Error:", error);
     });
     },
     deleteShape() {
       const pos = this.stage.getPointerPosition();
       const shape = this.stage.getIntersection(pos);
       
       if (shape) {
         this.curentShape= shape.id();
         shape.destroy();
         this.stage.batchDraw();
       }
     },
     exportStage() {
       const dataURL = this.stage.toDataURL({ pixelRatio: 3 });
       this.downloadURI(dataURL, 'Your_Drawing.png');
     },
     downloadURI(uri, name) {
       var link = document.createElement('a');
       link.download = name;
       link.href = uri;
       document.body.appendChild(link);
       link.click();
       document.body.removeChild(link);
     },
     undo(){
       axios.get('http://localhost:8081/undo')
           .then(response => {
             console.log(response.data);
             let todraw = response.data;
             for(let i =0;i<this.shapearray.length;i++)
       {
          this.shapearray[i].destroy();
       }
 
  
 
           for (const shapeid in todraw){
 
              if(todraw[shapeid].type==="Ellipse")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
 
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width*scalex;
               this.height=todraw[shapeid].height*scaley;
               this.fill=todraw[shapeid].fill;
               
               const elipse = new Konva.Ellipse({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id:shapeid,
                });
               this.layer.add(elipse);
               this.shapearray.push(elipse);
               this.addDoubleClickEvent(elipse);
 
 
              }
              else if(todraw[shapeid].type==="Circle")
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const circle = new Konva.Circle({
               x: this.x, 
               y: this.y,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(circle);
             this.shapearray.push(circle);
             this.addDoubleClickEvent(circle);
 
 
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===3)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const triangle = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               sides: 3,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(triangle);
             this.shapearray.push(triangle);
             this.addDoubleClickEvent(triangle);
 
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===4)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const square = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               rotation:45,
               sides: 4,
               radius: this.radius,
               fill: this.fill,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               opacity: 0.7,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(square);
             this.shapearray.push(square);
             this.addDoubleClickEvent(square);
 
              }
              else if(todraw[shapeid].type==="Rectangle")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width;
               this.height=todraw[shapeid].height;
               this.fill=todraw[shapeid].fill;
               const rectangle = new Konva.Rect({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               rotation:0,
               id:shapeid,
 
             });
             this.layer.add(rectangle);
             this.shapearray.push(rectangle);
             this.addDoubleClickEvent(rectangle);
 
 
              }
              else{
               const stline = new Konva.Line({
               points: todraw[shapeid].points, // Initialize with zero-length line
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id: shapeid,
           });
 
             this.layer.add(stline);
             this.shapearray.push(stline);
             this.addDoubleClickEvent(stline);
 
              }
 
 
 
           }
 
           this.stage.batchDraw();
           this.x = 100;
           this.y = 100;
           this.fill = '#0047b3';
           this.width = 200;
           this.height = 100;
           this.points=[100, 100, 200, 400];
 
           this.radius = 80;
           
 
          
             
 
 
 
           })
           .catch(error => {
             console.error('There was an error!', error);
           })
       
 
     },  
     redo(){
       axios.get('http://localhost:8081/redo')
           .then(response => {
             console.log(response.data);
             let todraw = response.data;
             for(let i =0;i<this.shapearray.length;i++)
       {
          this.shapearray[i].destroy();
       }
       
  
 
           for (const shapeid in todraw){
 
              if(todraw[shapeid].type==="Ellipse")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
 
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width*scalex;
               this.height=todraw[shapeid].height*scaley;
               this.fill=todraw[shapeid].fill;
               
               const elipse = new Konva.Ellipse({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id:shapeid,
                });
               this.layer.add(elipse);
               this.shapearray.push(elipse);
 
              }
              else if(todraw[shapeid].type==="Circle")
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const circle = new Konva.Circle({
               x: this.x, 
               y: this.y,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(circle);
             this.shapearray.push(circle);
 
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===3)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const triangle = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               sides: 3,
               radius: this.radius,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(triangle);
             this.shapearray.push(triangle);
 
              }
              else if(todraw[shapeid].type==="Polygon"&&todraw[shapeid].numberOfSides===4)
              {
               let scalex=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.radius=todraw[shapeid].radius*scalex;
               this.fill=todraw[shapeid].fill;
               const square = new Konva.RegularPolygon({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               rotation:45,
               sides: 4,
               radius: this.radius,
               fill: this.fill,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               opacity: 0.7,
               draggable: true,
               id:shapeid,
             });
             this.layer.add(square);
             this.shapearray.push(square);
              }
              else if(todraw[shapeid].type==="Rectangle")
              {
               let scalex=1;
               let scaley=1;
               if(todraw[shapeid].scaleX!=null)
               {
                 scalex=todraw[shapeid].scaleX;
               }
               if(todraw[shapeid].scaleY!=null)
               {
                 scaley=todraw[shapeid].scaleY;
               }
               
               this.x=todraw[shapeid].position.x;
               this.y=todraw[shapeid].position.y;
               this.width=todraw[shapeid].width;
               this.height=todraw[shapeid].height;
               this.fill=todraw[shapeid].fill;
               const rectangle = new Konva.Rect({
               x: this.x, // Adjust the X position as needed
               y: this.y, // Adjust the Y position as needed
               width: this.width,
               height: this.height,
               fill: this.fill,
               opacity: 0.7,
               stroke: 'black',
               strokeWidth: this.strokeWidth,
               draggable: true,
               rotation:0,
               id:shapeid,
 
             });
             this.layer.add(rectangle);
             this.shapearray.push(rectangle);
 
              }
              else{
               const stline = new Konva.Line({
               points: todraw[shapeid].points, // Initialize with zero-length line
               stroke: 'black',
               strokeWidth: 2,
               draggable: true,
               id: shapeid,
           });
 
             this.layer.add(stline);
             this.shapearray.push(stline);
             this.addDoubleClickEvent(stline);
 
              }
 
 
 
           }
 
           this.stage.batchDraw();
           this.x = 100;
           this.y = 100;
           this.points=[100, 100, 200, 400];
           this.fill = '#0047b3';
           this.width = 200;
           this.height = 100;
           this.radius = 80;
 
          
             
 
 
 
           })
           .catch(error => {
             console.error('There was an error!', error);
           })
 
     }
   },
 };
 </script>
 
 <style>
 body {
   margin: 1%;
   padding: 0;
   overflow: hidden;
   background-color:lightskyblue;
 }
 
 
 .button-container {
   display: flex;
   justify-content: center;
   margin-top: 5%;
 }
 
 .button {
   scale: 2.2;
   margin: 3%;
   border-radius: 4px;
   background-color:white;
 }
 
 .screen-container {
   margin: 3%;
   display: flex;
   justify-content: center;
   align-items: center;
 }
 
 .display-screen {
   border: 2px solid #000000;
   background-color: #fff;
   width: 100%;
 }
 
 .hide {
   scale: 2;
   position: absolute;
   left: 5%;
   top: 23%;
   margin-top: 10px;
   display: block;
 }
 
 .xml{
   scale: 1.5;
   position: absolute;
   left: 38%;
   top: 22%;
   margin-top: 10px;
   display: block;
 }
 .json{
   scale: 1.5;
   position: absolute;
   left: 38%;
   top: 24.5%;
   margin-top: 10px;
   display: block;
 }
 
 .save-container{
   scale:1.5;
   position: absolute;
   left: 29.5%;
   top: 23.7%;
 
 }
 
 .save-container label {
   position: absolute;
   top: 5px; /* Adjust this value for desired vertical positioning */
   left: 8px; /* Adjust this value for desired horizontal positioning */
   pointer-events: none; /* Ensures label does not interfere with input focus */
   color: #333; /* Adjust label color */
   font-size: 12px; /* Adjust label font size */
 }
 
 .load-container{
   scale:1.5;
   position: absolute;
   left: 47.5%;
   top: 23.7%;
 
 }
 
 .load-container label {
   position: absolute;
   top: 5px; /* Adjust this value for desired vertical positioning */
   left: 8px; /* Adjust this value for desired horizontal positioning */
   pointer-events: none; /* Ensures label does not interfere with input focus */
   color: #333; /* Adjust label color */
   font-size: 12px; /* Adjust label font size */
 }
 
 .button:hover {
   background-color: #cbc1c1;
   scale: 2.5;
 }
 .export{
   scale: 2;
   position: absolute;
   left: 15%;
   top: 23%;
   margin-top: 10px;
   background-color:#db2121;
   border: 0;
   border-radius: 3px;
 }
 .export:hover{
   background-color: rgb(193, 255, 234);
 }
 .save{
   scale: 1.5;
   left:20.5%
 }
 </style>
 