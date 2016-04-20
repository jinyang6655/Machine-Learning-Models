import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class LogisticRegression {
	double[] w;
	double b;
	double rate;
	double gap;
	
	public LogisticRegression(int attNum){
		w=new double[attNum];
		b=0;
		rate=0.01;
		gap=0.00001;
	}
	
	public void print(){
		System.out.println("b "+b+ " w "+Arrays.toString(w));
		
	}
	
	public void train(Data data){
		double preb;
		double[] prew=new double[w.length];
		int time=0;
		do{
			preb=b;
			prew=copyArray(w);
			double[] tmpw=new double[w.length];
			double tmpb=b+this.rate*this.updateB(data);
			double[] tmp=this.updateW(data);
			for(int i=0;i<prew.length;i++)
				tmpw[i]=w[i]+this.rate*tmp[i];
			b=tmpb;
			w=tmpw;
			time++;
			if(time>50)
				break;
		}while(terminate(prew,w,preb,b));
		System.out.println("times "+time);
	}
	
	public double updateB(Data data){
		double sum=0;
		for(Instance instance:data.dataSet){
			sum+=this.updateTerm(instance);
		}
		return sum;
	}
	
	public double[] updateW(Data data){
		double[] ans=new double[this.w.length];
		for(int i=0;i<ans.length;i++)
			for(Instance instance:data.dataSet){
				ans[i]+=this.updateTerm(instance)*instance.getDiscretValue(i);
			}
		return ans;
	}
	
	public double updateTerm(Instance instance){
		double predict=this.predict(instance);
		double y=instance.label?1.0:0;
		return y-predict;
	}
	
	public double[] copyArray(double[] w){
		double[] ans=new double[w.length];
		for(int i=0;i<w.length;i++)
			ans[i]=w[i];
		return ans;
	}
	
	public boolean terminate(double[] prew,double[] w,double preb,double b){
		if(Math.abs(preb-b)>this.gap) return true;
		for(int i=0;i<prew.length;i++)
			if(Math.abs(prew[i]-w[i])>this.gap)
				return true; 
		return false;
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
		if(this.predict(instance)>0.5) return true;
		return false;
	}
	
	public double predict(Instance instance){
		double res=b;
		for(int i=0;i<instance.size;i++){
			res+=w[i]*instance.getDiscretValue(i);
		}
	    res=Math.exp(res);
		return res/(1+res);
	}
	
	public Data ignoreMissingInstance(Data data){
		Data ans=new Data();
		for(Instance instance:data.dataSet){
			boolean flag=true;
			for(int i=0;i<instance.size;i++)
				if(instance.getValue(i)=='?')
					flag=false;
			if(flag)
				ans.addInstance(instance);			
		}
		return ans;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		Data train=new Data("voting_train.data");
	//	train.ignoreMissingInstance();
		train.dataImputation();
		Data test=new Data("voting_test.data");
		test.ignoreMissingInstance();
		LogisticRegression lg=new LogisticRegression(train.dataSet.get(0).size);	
//		File file = new File("train.txt");
//		train.writeToDisk(file);		
//		file = new File("test.txt");
//		test.writeToDisk(file);
		lg.train(train);
		lg.print();
		lg.classfy(train);
		lg.classfy(test);
	}
}
