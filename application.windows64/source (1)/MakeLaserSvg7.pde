import controlP5.*;

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
void setup(){
  size(windowSizeX,windowSizeY,P2D);
  smooth();
  grid=new svgShapeSystem();
  cp5 = new ControlP5(this);
  cp5.addButton("LoadImage")
    .setPosition(width*.76,height*.03)
    .setSize(int(width*.23),19)
    .setLabel("Load image (.gif, .jpg, .tga, .png)");
    ;
 units=cp5.addRadioButton("units")
     .setPosition(width*.76,height*.10)
     .setSize(int(width*.0575),19)
     .setItemsPerRow(2)
     .setSpacingColumn(int(width*.0575))
     .addItem("inches",1)
     .addItem("mm",2)
     .setNoneSelectedAllowed(false)
     .activate(1); //default mm
     ;
     
  outputScale=cp5.addTextfield("outputScale")
     .setValue("1.00")
     .setPosition(width*.76,height*.17)
     .setSize(int(width*.23),19)
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Output scale");
     ;
  outputSizeX=cp5.addTextfield("outputSizeX")
     .setPosition(width*.76,height*.24)
     .setSize(int(width*.11),19)
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Output Size X");
     ;
  outputSizeY=cp5.addTextfield("outputSizeY")
     .setPosition(width*.88,height*.24)
     .setSize(int(width*.11),19)
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Output Size Y");
     ;
  negativeImage=cp5.addToggle("negativeImage")
     .setPosition(width*.76,height*.31)
     .setSize(int(width*.23),19)
     .setValue(false)
     .setLabel("Negative output");
     ;
  gridType=cp5.addRadioButton("gridType")
     .setPosition(width*.76,height*.38)
     .setSize(int(width*.0383),19)
     .setItemsPerRow(3)
     .setSpacingColumn(int(width*.0383))
     .addItem("TRIANGLE",1)
     .addItem("SQUARE",2)
     .addItem("HEXAGON",3)
     .setNoneSelectedAllowed(false)
     .activate(2); //default HEX
     
     ;
  shapeType=cp5.addRadioButton("shapeType")
     .setPosition(width*.76,height*.45)
     .setSize(int(width*.0575),19)
     .setItemsPerRow(2)
     .setSpacingColumn(int(width*.0575))
     .addItem("CIRCLE",1)
     .addItem("POLYGON",2)
     .setNoneSelectedAllowed(false)
     .activate(0); //default Circle
     ;
  elementSpacing=cp5.addTextfield("elementSpacing")
     .setPosition(width*.76,height*.52)
     .setSize(int(width*.23),19)
     .setText("1.00")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Shape spacing");
     ; 
  elementMaxSize=cp5.addTextfield("elementMaxSize")
     .setPosition(width*.76,height*.59)
     .setSize(int(width*.23),19)
     .setText("5.00")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Maximum shape size");
     ; 
  elementMinSize=cp5.addTextfield("elementMinSize")
     .setPosition(width*.76,height*.66)
     .setSize(int(width*.23),19)
     .setText("0.00")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Minimum shape size");
     ; 
  minCutSize=cp5.addTextfield("minCutSize")
     .setPosition(width*.76,height*.73)
     .setSize(int(width*.23),19)
     .setText("0.5")
     .setColor(color(255))
     .setAutoClear(false)
     .setLabel("Minimum cut size");
     ; 
     
  cp5.addButton("preview")
    .setPosition(width*.76,height*.87)
    .setSize(int(width*.23),19)
    .setLabel("Update");
    ;
  cp5.addButton("saveSVG")
    .setPosition(width*.76,height*.94)
    .setSize(int(width*.23),19)
    .setLabel("save SVG file");
    ;
}
//---------------------------------------------------------------------------------------------------------------------DRAW
void draw(){
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
  
  rectMode(CORNER);  noStroke();  fill(127);  rect(width*.75,0,width,height);  //background for menu
}
//---------------------------------------------------------------------------------------------------------------------LOADIMAGE
public void LoadImage(int theValue) {
  println("loading image");
  selectInput("Select a file to process:", "fileSelected");
}
//---------------------------------------------------------------------------------------------------------------------FILESELECTED
void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    inputString=selection.getAbsolutePath();
    inputImage=loadImage(inputString);
    imageLoaded=true;
    outputSizeX.setText(str(float(outputScale.getText())*inputImage.width));
    outputSizeY.setText(str(float(outputScale.getText())*inputImage.height));
    updateSVGData();
  }
}
//---------------------------------------------------------------------------------------------------------------------OUTPUTSCALE
public void outputScale(String theText) {
  outputSizeX.setText(str(float(outputScale.getText())*inputImage.width));
  outputSizeY.setText(str(float(outputScale.getText())*inputImage.height));
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------OUTPUTSIZEX
public void outputSizeX(String theText) {
  outputScale.setText(str(float(outputSizeX.getText())/inputImage.width));
  outputSizeY.setText(str(float(outputScale.getText())*inputImage.height));
  updateSVGData();
}
//---------------------------------------------------------------------------------------------------------------------OUTPUTSIZEY
public void outputSizeY(String theText) {
  outputScale.setText(str(float(outputSizeY.getText())/inputImage.height));
  outputSizeX.setText(str(float(outputScale.getText())*inputImage.width));
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
void updateSVGData(){ 
  float maxSize=float(elementMaxSize.getText());  //get values from text boxes
  float minSize=float(elementMinSize.getText());
  float spacing=float(elementSpacing.getText());
  float minCut=float(minCutSize.getText());
  boolean negative=negativeImage.getState();
  float outScale=float(outputScale.getText());
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
  float outScale=float(outputScale.getText());
  String Units;
  if(units.getState(0)){
    Units="in";
  }else{
    Units="mm";
  }
  
  svgExport(grid.grid,  grid.currentImage.width, grid.currentImage.height, outScale, Units);
}
