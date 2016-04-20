import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yxj130830
 */
public class Instance {
    char[] attributes;       // there are 22 attributes, according to https://archive.ics.uci.edu/ml/datasets/Mushroom
    boolean label;          //if p, then label is true, else false
    int size;
    
    public Instance(String str,int size){
    	//democrat for 1
    	//republican for 0
    	this.size=size-1;
        attributes=new char[size-1];
        label=false;
        String[] list=str.split(",");
        for(int i=1;i<list.length;i++){
            attributes[i-1]=list[i].charAt(0);
        }
        if(list[0].charAt(0)=='d') 
            label=true;
        else if(list[0].charAt(0)=='r')
        	label=false;
        else
        	System.out.println("Error");
    }
    
    public Instance(Instance instance){
    	this.attributes=new char[instance.size];
    	for(int i=0;i<instance.size;i++)
    		this.attributes[i]=instance.getValue(i);
    	this.size=instance.size;
    	this.label=instance.label;
    }
    
    public void setInstance(int index,char att){
    	this.attributes[index]=att;
    }
    
    public Instance imputation(Instance instance,char[] major){
    	Instance tmp=new Instance(instance);
    	for(int i=0;i<tmp.size;i++){
    		if(tmp.getValue(i)=='?')
    			tmp.setInstance(i, major[i]);
    	}
    	return tmp;
    }
    
    public String print(){
    	StringBuilder builder=new StringBuilder();
    	char result=label?'d':'r';
    	builder.append(result);
    	for(int i=0;i<size;i++){
    		builder.append(',');
    		builder.append(attributes[i]);
    	}
    	//builder.append("\n");
    	System.out.println(builder.toString());
    	return builder.toString();
    }
    
    public char getValue(int att){
    	return attributes[att];
    }
    
    public double getDiscretValue(int att){
    	if(this.getValue(att)=='y') return 1.0;
    	if(this.getValue(att)=='n') return -1.0;
    	else {
    		//System.out.println("error");
    		return 0;
    	}
    }
    
    
    public boolean check(ArrayList<ArrayList<Character>> mushroom){
    	for(int i=0;i<attributes.length;i++){
    		if (!mushroom.get(i).contains(attributes[i])){
    			System.out.println(i+1);
    			return false;
    		}
    			
    	}
		return true;
    }
    
    public boolean getLabel(){
    	return this.label;
    }
    
}
