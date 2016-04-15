String[] OUTPUT={};   //strings array for main output

void svgExport(PShape INPUT,float WIDTH,float HEIGHT,float SCALE, String UNITS){
  OUTPUT=new String[0];   //clear the output strings
  OUTPUT = append(OUTPUT, "<svg width=\"" + WIDTH + UNITS + "\" height=\"" + HEIGHT + UNITS + "\" viewBox=\"0,0,"+WIDTH+","+HEIGHT+"\">");
  
  if(INPUT.getKind()==0){ //if the shape is a group - for me, always yes; the shape grid is always created that way (it's faster than managing an array of PShapes)
    for(int i=0;i<INPUT.getChildCount();i++){
      PShape s=INPUT.getChild(i);
      if(s.getKind()==ELLIPSE){   //ELLIPSE
        OUTPUT = append(OUTPUT, "<circle cx=\"" + (s.getParams()[0]+s.getParams()[2]*.5) + "\" cy=\"" + (s.getParams()[1]+s.getParams()[3]*.5) + "\" r=\"" + s.getWidth()*.5 + "\" style=\"fill:rgb(0,255,0);stroke:none\" />");
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
  println("Estimated Cut Time: " + str(round((OUTPUT.length-2)*.015)) + " minutes");
  
}

void saveFileSelected(File selection) {
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
