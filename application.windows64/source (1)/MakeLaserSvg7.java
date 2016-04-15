import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MakeLaserSvg7 extends PApplet {



//still needs: 
//setting for orientation (any angle) - this is a fairly major change
//arbitrary shapes?
//input error check

//BONUS: variable areas?
//contrast/brightness adjust??   

int windowSizeX=1280;
int windowSizeY=800;

ControlP5 cp5;
RadioButton units,gridType,shapeType;
Textfield outputScale,outputSizeX,outputSizeY,elementSpacing,elementMaxSize,elementMinSize,minCutSize;
Toggle negativeImage;
Slider gridAngle;

svgShapeSystem grid;
 
PImage inputImage;
String inputString="not loaded";

boolean imageLoaded=false;

//---------------------------------------------------------------------------------------------------------------------SETUP
public void setup(){
  size(windowSizeX,windowSizeY,P2D);
  smooth();
  grid=new svgShapeSystem();
  cp5 = new ControlP5(this);
  cp5.addButton("LoadImage")
    .setPosition(width*.76f,height*.03f)
    .setSize(PApplet.parseInt(width*.23f),19)
    .setLabel("Load image (.gif, .jpg, .tga, .png)");
    ;
 units=cp5.addRadioButton("units")
     .setPosition(width*.76f,height*.10f)
     .setSize(PApplet.parseInt(width*.0575f),19)
     .setItemsPerRow(2)
     .setSpacingColumn(PApplet.parseInt(width*.0575f))
     .addItem("inches",1)
     .addItem("mm",2)
     .setNoneSelectedAllowed(false)
     .activate(1); //default mm
     ;
     
  outputScale=cp5.addTextfield("outputScale")
     .setValue("1.00")
     .setPosition(width*.76f,height*.17f)
     .setSize(PApplet.parseInt(width*.23f),19)
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Output scale");
     ;
  outputSizeX=cp5.addTextfield("outputSizeX")
     .setPosition(width*.76f,height*.24f)
     .setSize(PApplet.parseInt(width*.11f),19)
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Output Size X");
     ;
  outputSizeY=cp5.addTextfield("outputSizeY")
     .setPosition(width*.88f,height*.24f)
     .setSize(PApplet.parseInt(width*.11f),19)
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Output Size Y");
     ;
  negativeImage=cp5.addToggle("negativeImage")
     .setPosition(width*.76f,height*.31f)
     .setSize(PApplet.parseInt(width*.23f),19)
     .setValue(false)
     .setLabel("Negative output");
     ;
  gridType=cp5.addRadioButton("gridType")
     .setPosition(width*.76f,height*.38f)
     .setSize(PApplet.parseInt(width*.0383f),19)
     .setItemsPerRow(3)
     .setSpacingColumn(PApplet.parseInt(width*.0383f))
     .addItem("TRIANGLE",1)
     .addItem("SQUARE",2)
     .addItem("HEXAGON",3)
     .setNoneSelectedAllowed(false)
     .activate(2); //default HEX
     
     ;
  shapeType=cp5.addRadioButton("shapeType")
     .setPosition(width*.76f,height*.45f)
     .setSize(PApplet.parseInt(width*.0575f),19)
     .setItemsPerRow(2)
     .setSpacingColumn(PApplet.parseInt(width*.0575f))
     .addItem("CIRCLE",1)
     .addItem("POLYGON",2)
     .setNoneSelectedAllowed(false)
     .activate(0); //default Circle
     ;
  elementSpacing=cp5.addTextfield("elementSpacing")
     .setPosition(width*.76f,height*.52f)
     .setSize(PApplet.parseInt(width*.23f),19)
     .setText("1.00")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Shape spacing");
     ; 
  elementMaxSize=cp5.addTextfield("elementMaxSize")
     .setPosition(width*.76f,height*.59f)
     .setSize(PApplet.parseInt(width*.23f),19)
     .setText("5.00")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Maximum shape size");
     ; 
  elementMinSize=cp5.addTextfield("elementMinSize")
     .setPosition(width*.76f,height*.66f)
     .setSize(PApplet.parseInt(width*.23f),19)
     .setText("0.00")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Minimum shape size");
     ; 
  minCutSize=cp5.addTextfield("minCutSize")
     .setPosition(width*.76f,height*.73f)
     .setSize(PApplet.parseInt(width*.23f),19)
     .setText("0.5")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Minimum cut size");
     ; 
     
  cp5.addButton("preview")
    .setPosition(width*.76f,height*.87f)
    .setSize(PApplet.parseInt(width*.23f),19)
    .setLabel("Update");
    ;
  cp5.addButton("saveSVG")
    .setPosition(width*.76f,height*.94f)
    .setSize(PApplet.parseInt(width*.23f),19)
    .setLabel("save SVG file");
    ;
}
//---------------------------------------------------------------------------------------------------------------------DRAW
public void draw(){
  if(negativeImage.getState()){
    background(255);
  }else{
    background(0);
  }
  //draw from svg data
  grid.grid.disableStyle(); 
  if(negativeImage.getState()){
    fill(0); 
  }else{
    fill(255);   
  } 
  noStroke(); 
  shape(grid.grid);
       
  //TROUBLESHOOTING------------------
  
  if(imageLoaded){
    PImage D=grid.currentImage.get();
    if(negativeImage.getState()){D.filter(INVERT);}
    image(D,D.width,0);
  }
  
  rectMode(CORNER);  noStroke();  fill(127);  rect(width*.75f,0,width,height);  //background for menu
}
//---------------------------------------------------------------------------------------------------------------------LOADIMAGE
public void LoadImage(int theValue) {
  println("loading image");
  selectInput("Select a file to process:", "fileSelected");
}
//---------------------------------------------------------------------------------------------------------------------FILESELECTED
public void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    inputString=selection.getAbsolutePath();
    inputImage=loadImage(inputString);
    imageLoaded=true;
    outputSizeX.setText(str(PApplet.parseFloat(outputScale.getText())*inputImage.width));
    outputSizeY.setText(str(PApplet.parseFloat(outputScale.getText())*inputImage.height));
    updateSVGData();
  }
}
//---------------------------------------------------------------------------------------------------------------------OUTPUTSCALE
public void outputScale(String theText) {
  outputSizeX.setText(str(PApplet.parseFloat(outputScale.getText())*inputImage.width));
  outputSizeY.setText(str(PApplet.parseFloat(outputScale.getText())*inputImage.height));
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------OUTPUTSIZEX
public void outputSizeX(String theText) {
  outputScale.setText(str(PApplet.parseFloat(outputSizeX.getText())/inputImage.width));
  outputSizeY.setText(str(PApplet.parseFloat(outputScale.getText())*inputImage.height));
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------OUTPUTSIZEY
public void outputSizeY(String theText) {
  outputScale.setText(str(PApplet.parseFloat(outputSizeY.getText())/inputImage.height));
  outputSizeX.setText(str(PApplet.parseFloat(outputScale.getText())*inputImage.width));
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------ELEMENTSPACING
public void elementSpacing(String theText) {
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------ELEMENTMAXSIZE
public void elementMaxSize(String theText) {
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------ELEMENTMINSIZE
public void elementMinSize(String theText) {
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------MINCUTSIZE
public void minCutSize(String theText) {
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------NEGATIVEIMAGE
public void negativeImage(boolean theFlag) {
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------CONTROLEVENT
public void controlEvent(ControlEvent theEvent) {
  if(theEvent.isFrom(gridType)) {
    updateSVGData();
  }
  if(theEvent.isFrom(shapeType)) {
    updateSVGData();
  }
}
//---------------------------------------------------------------------------------------------------------------------UPDATESVGDATA
public void updateSVGData(){ 
  float maxSize=PApplet.parseFloat(elementMaxSize.getText());  //get values from text boxes
  float minSize=PApplet.parseFloat(elementMinSize.getText());
  float spacing=PApplet.parseFloat(elementSpacing.getText());
  float minCut=PApplet.parseFloat(minCutSize.getText());
  boolean negative=negativeImage.getState();
  float outScale=PApplet.parseFloat(outputScale.getText());
  if(gridType.getState(0)){                                              //IF TRIANGLE GRIDS
    if(shapeType.getState(1)){                                             //IF POLYGONS
      grid.createGrid(inputImage,"TRIANGLE","POLYGON",maxSize,minSize,spacing,minCut,negative,outScale);
    }else{                                                                 //IF CIRCLES
      grid.createGrid(inputImage,"TRIANGLE","CIRCLE",maxSize,minSize,spacing,minCut,negative,outScale);
    }
  }else if(gridType.getState(1)){                                        //IF SQUARE GRIDS
    if(shapeType.getState(1)){                                             //IF POLYGONS
      grid.createGrid(inputImage,"SQUARE","POLYGON",maxSize,minSize,spacing,minCut,negative,outScale);
    }else{                                                                 //IF CIRCLES
      grid.createGrid(inputImage,"SQUARE","CIRCLE",maxSize,minSize,spacing,minCut,negative,outScale);
    }
  }else if(gridType.getState(2)){                                        //IF HEX GRIDS
    if(shapeType.getState(1)){                                             //IF POLYGONS
      grid.createGrid(inputImage,"HEXAGON","POLYGON",maxSize,minSize,spacing,minCut,negative,outScale);
    }else{                                                                 //IF CIRCLES
      grid.createGrid(inputImage,"HEXAGON","CIRCLE",maxSize,minSize,spacing,minCut,negative,outScale);
    }
  }
}
//---------------------------------------------------------------------------------------------------------------------PREVIEW
public void preview(){
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------SAVESVG
public void saveSVG(){
  float outScale=PApplet.parseFloat(outputScale.getText());
  String Units;
  if(units.getState(0)){
    Units="in";
  }else{
    Units="mm";
  }
  
  svgExport(grid.grid,  grid.currentImage.width, grid.currentImage.height, outScale, Units);
}
String[] OUTPUT={};   //strings array for main output

public void svgExport(PShape INPUT,float WIDTH,float HEIGHT,float SCALE, String UNITS){
  OUTPUT=new String[0];   //clear the output strings
  OUTPUT = append(OUTPUT, "<svg width=\"" + WIDTH + UNITS + "\" height=\"" + HEIGHT + UNITS + "\" viewBox=\"0,0,"+WIDTH+","+HEIGHT+"\">");
  
  if(INPUT.getKind()==0){ //if the shape is a group - for me, always yes; the shape grid is always created that way (it's faster than managing an array of PShapes)
    for(int i=0;i<INPUT.getChildCount();i++){
      PShape s=INPUT.getChild(i);
      if(s.getKind()==ELLIPSE){   //ELLIPSE
        OUTPUT = append(OUTPUT, "<circle cx=\"" + (s.getParams()[0]+s.getParams()[2]*.5f) + "\" cy=\"" + (s.getParams()[1]+s.getParams()[3]*.5f) + "\" r=\"" + s.getWidth()*.5f + "\" style=\"fill:rgb(0,255,0);stroke:none\" />");
      }else if(s.getKind()==RECT){   //RECT
        OUTPUT = append(OUTPUT, "<rect x=\"" + s.getParams()[0] + "\" y=\"" + s.getParams()[1] + "\" width=\"" + s.getParams()[2] + "\" height=\""+ s.getParams()[3] + "\" style=\"fill:rgb(0,255,0);stroke:none\" />");
      }else{   //PATH (for hexagons and triangles)
        //String P="<path d=\"M" + s.getParams()[0] + " " + s.getParams()[1] + " L" + s.getParams()[2] + " " + s.getParams()[1] +L75 200 L225 200 "Z\" />";
        String P="<path d=\"M" + s.getVertexX(0) + " " + s.getVertexY(0);
        for(int j=1;j<s.getVertexCount();j++){
          P=P + " L" + s.getVertexX(j) + " " + s.getVertexY(j);
        }
        P=P+" Z\" style=\"fill:rgb(0,255,0);stroke:none\" />";
        OUTPUT = append(OUTPUT,P);
      }
    }
  }  
  OUTPUT = append(OUTPUT, "</svg>");
  selectOutput("Select a file to write to:", "saveFileSelected");
  println("Estimated Cut Time: " + str(round((OUTPUT.length-2)*.015f)) + " minutes");
  
}

public void saveFileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    String s=selection.getAbsolutePath();
    if(s.substring(s.length()-4).equals(".svg")){
    }else{
      s=s+".svg";
    }
    println("User selected " + s);
    
    saveStrings(s,OUTPUT);
  }
}
//---------------------------------------------------------------------------------------------------------------------SVGSHAPESYSTEM
class svgShapeSystem{
  String gridType;
  String shapeType;
  float maxSize;
  float minSize;
  float shapeSpacing;
  float minCutSize;
  PShape grid;
  PImage currentImage;
  float HEXratio=cos(PI/6);
  float TRIratio=1/sqrt(3);
  PGraphics currentShape;
  
  //----------------------------------------------------------------------------------------------------------------------------------------------------------CONSTRUCTOR
  svgShapeSystem(){
    grid=createShape(GROUP);
  }
  
  //----------------------------------------------------------------------------------------------------------------------------------------------------------CREATEGRID
  public void createGrid(PImage INPUTIMAGE,String GRIDTYPE,String SHAPETYPE,float MAXSIZE,float MINSIZE,float SHAPESPACING,float MINCUTSIZE,boolean NEGATIVE,float OUTPUTSCALE){
    currentImage=INPUTIMAGE.get();
    if(NEGATIVE){currentImage.filter(INVERT);}
    currentImage.resize(PApplet.parseInt(INPUTIMAGE.width*OUTPUTSCALE),PApplet.parseInt(INPUTIMAGE.height*OUTPUTSCALE));
    
    grid=createShape(GROUP);        //clear the current grid
    gridType=GRIDTYPE;
    maxSize=MAXSIZE;
    minSize=MINSIZE;
    minCutSize=MINCUTSIZE;
    shapeType=SHAPETYPE;
    shapeSpacing=SHAPESPACING;
    
    if(gridType=="HEXAGON"){
      for(float y=maxSize*.5f;  y<currentImage.height-maxSize*.5f;  y+=maxSize+shapeSpacing){
        for(float x=maxSize*.5f/HEXratio;  x<currentImage.width-maxSize*.5f/HEXratio;  x+=(maxSize/HEXratio)*1.5f+shapeSpacing*2*HEXratio){
          float b=getBrightness(currentImage,hexagon(maxSize*.5f/HEXratio,maxSize*.5f,maxSize),x,y,maxSize/HEXratio,maxSize);    
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(hexagon(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5f,y-shapeSize*.5f,shapeSize,shapeSize));
            }
          }
        }
      }
      for(float y=maxSize+shapeSpacing*.5f;  y<currentImage.height-maxSize*.5f*HEXratio;  y+=maxSize+shapeSpacing){
        for(float x=(maxSize/HEXratio)*1.25f+shapeSpacing*HEXratio;  x<currentImage.width-maxSize*.5f;  x+=(maxSize/HEXratio)*1.5f+shapeSpacing*2*HEXratio){
          float b=getBrightness(currentImage,hexagon(maxSize*.5f/HEXratio,maxSize*.5f,maxSize),x,y,maxSize/HEXratio,maxSize);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(hexagon(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5f,y-shapeSize*.5f,shapeSize,shapeSize));
            }
          }
        }
      }
    }else if(gridType=="SQUARE"){
      for(float y=maxSize*.5f;  y<currentImage.height-maxSize*.5f;  y+=maxSize+shapeSpacing){
        for(float x=maxSize*.5f;  x<currentImage.width-maxSize*.5f;  x+=maxSize+shapeSpacing){
          float b=getBrightness(currentImage,createShape(RECT,0,0,maxSize,maxSize),x,y,maxSize,maxSize);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(createShape(RECT,x-shapeSize*.5f,y-shapeSize*.5f,shapeSize,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5f,y-shapeSize*.5f,shapeSize,shapeSize));
            }
          }
        }
      }
    }else if(gridType=="TRIANGLE"){
      for(float y=maxSize*TRIratio+shapeSpacing*TRIratio;  y<currentImage.height-maxSize*TRIratio*.5f;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize*.5f;  x<currentImage.width-maxSize*.5f;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,tri(maxSize*.5f,maxSize*TRIratio,maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(tri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5f*TRIratio,y-shapeSize*.5f*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
      for(float y=maxSize*2*TRIratio+shapeSpacing+shapeSpacing*TRIratio;  y<currentImage.height-maxSize*TRIratio;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize*.5f;  x<currentImage.width-maxSize*.5f;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,itri(maxSize*.5f,maxSize*(HEXratio/3),maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(itri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5f*TRIratio,y-shapeSize*.5f*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
      for(float y=maxSize*0.5f*TRIratio;  y<currentImage.height-maxSize*TRIratio;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize+shapeSpacing*HEXratio;  x<currentImage.width-maxSize*.5f;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,itri(maxSize*.5f,maxSize*(HEXratio/3),maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(itri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5f*TRIratio,y-shapeSize*.5f*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
      for(float y=maxSize*TRIratio*2.5f+2*shapeSpacing;  y<currentImage.height-maxSize*TRIratio*.5f;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize+shapeSpacing*HEXratio;  x<currentImage.width-maxSize*.5f;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,tri(maxSize*.5f,maxSize*TRIratio,maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(tri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5f*TRIratio,y-shapeSize*.5f*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
    }
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------ADDSHAPE
  public void addShape(PShape INPUTSHAPE){
    grid.addChild(INPUTSHAPE);
  }
  
  //----------------------------------------------------------------------------------------------------------------------------------------------------------GETBRIGHTNESS
  public float getBrightness(PImage INPUTIMAGE,PShape INPUTSHAPE,float X,float Y,float SHAPEWIDTH,float SHAPEHEIGHT){  //bodge
    X-=SHAPEWIDTH*.5f;
    Y-=SHAPEHEIGHT*.5f;
    float average=0;
    float averageCount=0;
    INPUTSHAPE.disableStyle();    
    currentShape=createGraphics(ceil(SHAPEWIDTH),ceil(SHAPEHEIGHT));  //uses the shape width and height to create an offscreen render of the grid shape being used
    currentShape.beginDraw();
    currentShape.background(0);
    currentShape.fill(255);
    currentShape.noStroke();
    currentShape.shape(INPUTSHAPE,0,0,SHAPEWIDTH,SHAPEHEIGHT);        //draw the input shape, white on a black background
    currentShape.endDraw();
    for(int x=0;x<currentShape.width;x++){
      for(int y=0;y<currentShape.height;y++){
        if(brightness(currentShape.get(x,y))>0){                      //if the brightness of the offscreen graphics is>0
          float bright=brightness(INPUTIMAGE.get((int)X+x,(int)Y+y));  //grab the pixel brightness from the original image, and add to average
          average+=bright/255;
          averageCount+=1.00f;
        }
      }
    }
    if(averageCount>0){average=average/averageCount;}
    average=sqrt(average); //brightness to area      
    return average;
  }
  
}

//---------------------------------------------------------------------------------------------------------------------HEXAGON
public PShape hexagon(float X, float Y, float RAD){;
  float ratio=cos(PI/6);
  PShape HEX;
  RAD*=.5f;
  HEX=createShape();
  HEX.beginShape();
  HEX.noStroke();
  HEX.vertex(  X-(RAD/ratio)*.5f,  Y-RAD,        X+(RAD/ratio)*.5f, Y-RAD);
  HEX.vertex(  X+(RAD/ratio)*.5f,  Y-RAD,        X+(RAD/ratio),    Y);
  HEX.vertex(  X+(RAD/ratio),     Y,            X+(RAD/ratio)*.5f, Y+RAD);
  HEX.vertex(  X+(RAD/ratio)*.5f,  Y+RAD,        X-(RAD/ratio)*.5f, Y+RAD);
  HEX.vertex(  X-(RAD/ratio)*.5f,  Y+RAD,        X-(RAD/ratio),    Y);
  HEX.vertex(  X-(RAD/ratio),     Y,            X-(RAD/ratio)*.5f, Y-RAD);
  HEX.endShape(CLOSE);
  return HEX;
}
//---------------------------------------------------------------------------------------------------------------------ITRI
public PShape itri(float X, float Y, float SIZE){
  float ratio=1/sqrt(3);
  PShape ITRI;
  ITRI=createShape();
  ITRI.beginShape();
  ITRI.noStroke();
  ITRI.vertex(X-SIZE*.5f,  Y-SIZE*ratio*.5f,  X+SIZE*.5f,  Y-SIZE*ratio*.5f);
  ITRI.vertex(X+SIZE*.5f,  Y-SIZE*ratio*.5f,  X        ,  Y+SIZE*ratio   );
  ITRI.vertex(X        ,  Y+SIZE*ratio   ,  X-SIZE*.5f,  Y-SIZE*ratio*.5f);
  ITRI.endShape(CLOSE);
  return ITRI;
}
//---------------------------------------------------------------------------------------------------------------------TRI
public PShape tri(float X, float Y, float SIZE){
  float ratio=1/sqrt(3);
  PShape TRI;
  TRI=createShape();
  TRI.beginShape();
  TRI.noStroke();
  TRI.vertex(X        ,  Y-SIZE*ratio   ,  X+SIZE*.5f,  Y+SIZE*ratio*.5f);
  TRI.vertex(X+SIZE*.5f,  Y+SIZE*ratio*.5f,  X-SIZE*.5f,  Y+SIZE*ratio*.5f);
  TRI.vertex(X-SIZE*.5f,  Y+SIZE*ratio*.5f,  X        ,  Y-SIZE*ratio   );
  TRI.endShape(CLOSE);
  return TRI;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MakeLaserSvg7" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
