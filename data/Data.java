import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Data {
	ArrayList<Instance> dataSet;
	int total;//the attributes of the number of instance, include the label
	
	public Data(String filename) throws FileNotFoundException, IOException{
		this.dataSet=new ArrayList<Instance>();
		this.load(filename);
	}
	
	public Data(){
		this.dataSet=new ArrayList<Instance>();
	}
	
	public void addInstance(Instance instance){
		this.dataSet.add(instance);
	}
	
	public void load(String filename) throws FileNotFoundException, IOException{
    	//HashMap<String,String> set=new HashMap<String,String>();
    	this.total=17;
    	ArrayList<Instance> data=new ArrayList<Instance>();
        File file=new File(filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
        	//System.out.println(line);
        	//checkDuplicate(set,line);
        	Instance instance=new Instance(line,total);
        	//instance.check(mushroom);
            data.add(instance);
        }
        this.dataSet=data;
        System.out.println("done");
    }
	
	public void writeToDisk(File file) throws IOException{
		Writer output = new BufferedWriter(new FileWriter(file));
		for(Instance instance:this.dataSet){
			output.write(instance.print());
			//output.write('\n');
			output.write(System.getProperty( "line.separator" ));
		}
		output.close();
	}
	
	public void ignoreMissingInstance(){
		ArrayList<Instance> ans=new ArrayList<Instance>();
		for(Instance instance:this.dataSet){
			boolean flag=true;
			for(int i=0;i<instance.size;i++)
				if(instance.getValue(i)=='?')
					flag=false;
			if(flag)
				ans.add(instance);			
		}
		this.dataSet=ans;
	}
	
	public void dataImputation(){
		char[] majorityY=new char[this.dataSet.get(0).size];
		char[] majorityN=new char[this.dataSet.get(0).size];
		for(int i=0;i<majorityY.length;i++){
			int yY=0;
			int nY=0;
			int yN=0;
			int nN=0;
			for(Instance instance:this.dataSet){
				if(instance.label){
					if(instance.getValue(i)=='y')
						yY++;
					else if(instance.getValue(i)=='n')
						nY++;
				}else{
					if(instance.getValue(i)=='y')
						yN++;
					else if(instance.getValue(i)=='n')
						nN++;
				}	
			}
			if(yY>nY)
				majorityY[i]='y';
			else
				majorityY[i]='n';
			if(yN>nN)
				majorityN[i]='y';
			else
				majorityN[i]='n';
		}
		ArrayList<Instance> ans=new ArrayList<Instance>();
		for(Instance instance:this.dataSet){
			Instance tmp=null;
			if(instance.label)
				tmp=instance.imputation(instance, majorityY);
			else
				tmp=instance.imputation(instance, majorityN);
			ans.add(tmp);
		}
		this.dataSet=ans;
	}
			
	public void print(){
		for(Instance instance:this.dataSet)
			instance.print();
		System.out.println("total size "+this.dataSet.size());
	}
	
	
public static void main(String[] args) throws FileNotFoundException, IOException{
	Data train=new Data("voting_train.data");
	train.print();
}

}
