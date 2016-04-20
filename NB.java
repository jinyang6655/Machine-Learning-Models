import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NB {
	double[] countD;//count for democrat which is Y
	double[] countR;//count for republican which is Y
	double republican;
	double democrat;
	
	public NB(Data data){
    	this.countD=new double[data.total-1];//since we do not count for the label
    	this.countR=new double[data.total-1];
    	republican=0;
    	democrat=0;
    	this.getCount(data);
	}
	
	public double classfy(Data data){
		double correct=0;
		for(Instance instance:data.dataSet){
			if(instance.label==this.classfy(instance))
				correct++;
		}
		correct= correct/(double)data.dataSet.size();
		System.out.println("Accuracy "+correct);
		return correct;
	}
	
	public boolean classfy(Instance instance){
		double d=this.Prob(instance, countD,democrat);
		double r=this.Prob(instance, countR,republican);
		if(d>r) return true;
		return false;
	}
	
	public double Prob(Instance instance,double[] count,double prior){
		double result=prior;
		for(int i=0;i<instance.size;i++){
			if(instance.getValue(i)=='?') continue;
			if(instance.getValue(i)=='y') result*=count[i];
			else result*=(1-count[i]);
		}
		return result;
	}
	
	public void getCount(Data data){
		// to get the probability, only for democrat

		for(Instance instance:data.dataSet)
			if(instance.label) democrat++;
			else 	republican++;
		democrat=democrat/(double)data.dataSet.size();
		republican=republican/(double)data.dataSet.size();
		for(int i=0;i<data.total-1;i++){
			int r=0;
			int d=0;
			int rY=0;
			int dY=0;
			int total=0;
			for(Instance instance:data.dataSet){
				char tmp=instance.getValue(i);
				if(tmp=='?') 
					continue;
				total++;
				if(instance.label){
					d++;
					if(tmp=='y'||tmp=='Y') dY++;
				}else{
					r++;
					if(tmp=='y'||tmp=='Y') rY++;
				}
			}
			double result=(double)rY/(double)r;
			countR[i]=result;
			result=(double)dY/(double)d;
			countD[i]=result;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		Data train=new Data("voting_train.data");
		//train.ignoreMissingInstance();
		//train.writeToDisk(new File("train.txt"));
		train.dataImputation();
		Data test=new Data("voting_test.data");
		test.ignoreMissingInstance();
		NB nb=new NB(train);
		//nb.getCount(train);
		nb.classfy(train);
		nb.classfy(test);
	}

}
