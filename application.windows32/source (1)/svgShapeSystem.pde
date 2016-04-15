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
  void createGrid(PImage INPUTIMAGE,String GRIDTYPE,String SHAPETYPE,float MAXSIZE,float MINSIZE,float SHAPESPACING,float MINCUTSIZE,boolean NEGATIVE,float OUTPUTSCALE){
    currentImage=INPUTIMAGE.get();
    if(NEGATIVE){currentImage.filter(INVERT);}
    currentImage.resize(int(INPUTIMAGE.width*OUTPUTSCALE),int(INPUTIMAGE.height*OUTPUTSCALE));
    
    grid=createShape(GROUP);        //clear the current grid
    gridType=GRIDTYPE;
    maxSize=MAXSIZE;
    minSize=MINSIZE;
    minCutSize=MINCUTSIZE;
    shapeType=SHAPETYPE;
    shapeSpacing=SHAPESPACING;
    
    if(gridType=="HEXAGON"){
      for(float y=maxSize*.5;  y<currentImage.height-maxSize*.5;  y+=maxSize+shapeSpacing){
        for(float x=maxSize*.5/HEXratio;  x<currentImage.width-maxSize*.5/HEXratio;  x+=(maxSize/HEXratio)*1.5+shapeSpacing*2*HEXratio){
          float b=getBrightness(currentImage,hexagon(maxSize*.5/HEXratio,maxSize*.5,maxSize),x,y,maxSize/HEXratio,maxSize);    
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(hexagon(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5,y-shapeSize*.5,shapeSize,shapeSize));
            }
          }
        }
      }
      for(float y=maxSize+shapeSpacing*.5;  y<currentImage.height-maxSize*.5*HEXratio;  y+=maxSize+shapeSpacing){
        for(float x=(maxSize/HEXratio)*1.25+shapeSpacing*HEXratio;  x<currentImage.width-maxSize*.5;  x+=(maxSize/HEXratio)*1.5+shapeSpacing*2*HEXratio){
          float b=getBrightness(currentImage,hexagon(maxSize*.5/HEXratio,maxSize*.5,maxSize),x,y,maxSize/HEXratio,maxSize);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(hexagon(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5,y-shapeSize*.5,shapeSize,shapeSize));
            }
          }
        }
      }
    }else if(gridType=="SQUARE"){
      for(float y=maxSize*.5;  y<currentImage.height-maxSize*.5;  y+=maxSize+shapeSpacing){
        for(float x=maxSize*.5;  x<currentImage.width-maxSize*.5;  x+=maxSize+shapeSpacing){
          float b=getBrightness(currentImage,createShape(RECT,0,0,maxSize,maxSize),x,y,maxSize,maxSize);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(createShape(RECT,x-shapeSize*.5,y-shapeSize*.5,shapeSize,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5,y-shapeSize*.5,shapeSize,shapeSize));
            }
          }
        }
      }
    }else if(gridType=="TRIANGLE"){
      for(float y=maxSize*TRIratio+shapeSpacing*TRIratio;  y<currentImage.height-maxSize*TRIratio*.5;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize*.5;  x<currentImage.width-maxSize*.5;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,tri(maxSize*.5,maxSize*TRIratio,maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(tri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5*TRIratio,y-shapeSize*.5*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
      for(float y=maxSize*2*TRIratio+shapeSpacing+shapeSpacing*TRIratio;  y<currentImage.height-maxSize*TRIratio;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize*.5;  x<currentImage.width-maxSize*.5;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,itri(maxSize*.5,maxSize*(HEXratio/3),maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(itri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5*TRIratio,y-shapeSize*.5*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
      for(float y=maxSize*0.5*TRIratio;  y<currentImage.height-maxSize*TRIratio;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize+shapeSpacing*HEXratio;  x<currentImage.width-maxSize*.5;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,itri(maxSize*.5,maxSize*(HEXratio/3),maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(itri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5*TRIratio,y-shapeSize*.5*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
      for(float y=maxSize*TRIratio*2.5+2*shapeSpacing;  y<currentImage.height-maxSize*TRIratio*.5;  y+=2*maxSize*HEXratio+3*shapeSpacing){
        for(float x=maxSize+shapeSpacing*HEXratio;  x<currentImage.width-maxSize*.5;  x+=maxSize+2*shapeSpacing*HEXratio){
          float b=getBrightness(currentImage,tri(maxSize*.5,maxSize*TRIratio,maxSize),x,y,maxSize,maxSize*HEXratio);
          float shapeSize=minSize+b*(maxSize-minSize);
          if(shapeSize>minCutSize){
            if(shapeType=="POLYGON"){
              addShape(tri(x,y,shapeSize));
            }else{
              addShape(createShape(ELLIPSE,x-shapeSize*.5*TRIratio,y-shapeSize*.5*TRIratio,shapeSize*TRIratio,shapeSize*TRIratio));
            }
          }
        }
      }
    }
  }
  //----------------------------------------------------------------------------------------------------------------------------------------------------------ADDSHAPE
  void addShape(PShape INPUTSHAPE){
    grid.addChild(INPUTSHAPE);
  }
  
  //----------------------------------------------------------------------------------------------------------------------------------------------------------GETBRIGHTNESS
  float getBrightness(PImage INPUTIMAGE,PShape INPUTSHAPE,float X,float Y,float SHAPEWIDTH,float SHAPEHEIGHT){  //bodge
    X-=SHAPEWIDTH*.5;
    Y-=SHAPEHEIGHT*.5;
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
          averageCount+=1.00;
        }
      }
    }
    if(averageCount>0){average=average/averageCount;}
    average=sqrt(average); //brightness to area      
    return average;
  }
  
}

//---------------------------------------------------------------------------------------------------------------------HEXAGON
PShape hexagon(float X, float Y, float RAD){;
  float ratio=cos(PI/6);
  PShape HEX;
  RAD*=.5;
  HEX=createShape();
  HEX.beginShape();
  HEX.noStroke();
  HEX.vertex(  X-(RAD/ratio)*.5,  Y-RAD,        X+(RAD/ratio)*.5, Y-RAD);
  HEX.vertex(  X+(RAD/ratio)*.5,  Y-RAD,        X+(RAD/ratio),    Y);
  HEX.vertex(  X+(RAD/ratio),     Y,            X+(RAD/ratio)*.5, Y+RAD);
  HEX.vertex(  X+(RAD/ratio)*.5,  Y+RAD,        X-(RAD/ratio)*.5, Y+RAD);
  HEX.vertex(  X-(RAD/ratio)*.5,  Y+RAD,        X-(RAD/ratio),    Y);
  HEX.vertex(  X-(RAD/ratio),     Y,            X-(RAD/ratio)*.5, Y-RAD);
  HEX.endShape(CLOSE);
  return HEX;
}
//---------------------------------------------------------------------------------------------------------------------ITRI
PShape itri(float X, float Y, float SIZE){
  float ratio=1/sqrt(3);
  PShape ITRI;
  ITRI=createShape();
  ITRI.beginShape();
  ITRI.noStroke();
  ITRI.vertex(X-SIZE*.5,  Y-SIZE*ratio*.5,  X+SIZE*.5,  Y-SIZE*ratio*.5);
  ITRI.vertex(X+SIZE*.5,  Y-SIZE*ratio*.5,  X        ,  Y+SIZE*ratio   );
  ITRI.vertex(X        ,  Y+SIZE*ratio   ,  X-SIZE*.5,  Y-SIZE*ratio*.5);
  ITRI.endShape(CLOSE);
  return ITRI;
}
//---------------------------------------------------------------------------------------------------------------------TRI
PShape tri(float X, float Y, float SIZE){
  float ratio=1/sqrt(3);
  PShape TRI;
  TRI=createShape();
  TRI.beginShape();
  TRI.noStroke();
  TRI.vertex(X        ,  Y-SIZE*ratio   ,  X+SIZE*.5,  Y+SIZE*ratio*.5);
  TRI.vertex(X+SIZE*.5,  Y+SIZE*ratio*.5,  X-SIZE*.5,  Y+SIZE*ratio*.5);
  TRI.vertex(X-SIZE*.5,  Y+SIZE*ratio*.5,  X        ,  Y-SIZE*ratio   );
  TRI.endShape(CLOSE);
  return TRI;
}
