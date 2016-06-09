package lfm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LFM {
	HashMap<Integer,Set<Integer>> trainset=new HashMap<Integer,Set<Integer>>();
	HashMap<Integer,Set<Integer>> testset=new HashMap<Integer,Set<Integer>>();
	HashMap<Integer,Set<Integer>> inverse_table=new HashMap<Integer,Set<Integer>>();
	HashMap<Integer,Integer> movie_popular=new HashMap<Integer,Integer>();
	int i=0;
	int trainset_length;
	int testset_length;
	int user_sim_mat[][];
	double p[][];
	double q[][];
	int F=100;
	double alpha=0.02;
	double lambda=0.01;
	double  user_simlarity[][];
	int movie_count=0;
	int N=100;
	List<Rank> recommendedMoviesList=null;
	List<Ralated_user> ralatedUsersList=null;
	int n=10;
	Random random=new Random(0);

	public void generate_dataset(int pivot) throws IOException{
		
		File file=new File("E:\\workspace\\ml-1m\\ratings.dat");
		
		if(!file.exists()||file.isDirectory())
			throw new FileNotFoundException();
		
		BufferedReader br=new BufferedReader(new FileReader(file));
		String temp=null;
		
		while ((temp=br.readLine())!=null) {
			
			String[] content=temp.replaceAll("\n\t", "").split("::");
			if(random.nextInt(8)==pivot){
				if(testset.containsKey(Integer.parseInt(content[0]))){
					HashSet<Integer> set =(HashSet<Integer>) testset.get(Integer.parseInt(content[0]));
					set.add(Integer.parseInt(content[1]));
					testset.put(Integer.parseInt(content[0]),set);
				}else{
					Set<Integer> set=new HashSet<Integer>();
					set.add(Integer.parseInt(content[1]));
					testset.put(Integer.parseInt(content[0]),set);
				}
				testset_length++;
				
			}else{
				if(trainset.containsKey(Integer.parseInt(content[0]))){
					HashSet<Integer> set =(HashSet<Integer>) trainset.get(Integer.parseInt(content[0]));
					set.add(Integer.parseInt(content[1]));
					trainset.put(Integer.parseInt(content[0]),set);
					
				}else{
					Set<Integer> set=new HashSet<Integer>();
					set.add(Integer.parseInt(content[1]));
					trainset.put(Integer.parseInt(content[0]),set);
				}
				
				trainset_length++;
				
			}
			i++;
			if (i%100000 == 0)
                System.out.println("已装载"+i+"文件");
	   }
		System.out.println("测试集和训练集分割完成，测试集长度："+testset_length+",训练集长度："+trainset_length);
		
	}	
	
public void calc_user_sim(){
		
		for(int obj : trainset.keySet()){ 
			
			Set<Integer> value = trainset.get(obj);
			Iterator<Integer> it=value.iterator();
			
		       while(it.hasNext())
		       {
		           int o=it.next();
		           //  count item popularity at the same time
		           if(!movie_popular.containsKey(o)){
		        	   movie_popular.put(o,1);
		           }else {
		        	   movie_popular.put(o,movie_popular.get(o)+1);
				   }
		          
		       }
			
			
			
			}
		movie_count=movie_popular.size();
		System.out.println("movie number is"+movie_count);		
}
	public HashMap<Integer, Integer> RandomSelectNegativeSample(int user){
		HashMap<Integer,Integer> userMap=new HashMap<Integer,Integer>();
		
		Set<Integer> movies=trainset.get(user);
		Iterator<Integer> it=movies.iterator();
		while(it.hasNext()){
			userMap.put(it.next(), 1);//把用户有过行为的电影都置为1
		}
		
		int n=0;
		int k=-1;
		Set<Integer> movieSet=movie_popular.keySet();
		
		for(int i=0;i<movies.size()*3;i++){    //将范围上限设为len(items) * 3，主要是为保证正、负样本数量接近。
			
			for(int j=0;j<movie_popular.size();j++){//进行movie_popular的长度次数的尝试	
				k=random.nextInt(movie_popular.size()-1);
				if(!movieSet.contains(k)){
					continue;
				}	
				else {
					break;
				}
			}
			if(userMap.containsKey(k)){
				continue;
			}else{
				userMap.put(k, 0);
			}
			n++;
			if(n>movies.size())
				break;
			
		}
		return userMap;
		
	}
	
	public void LatentFactorModel(){
		InitModel();

		Iterator<Integer> user_it=trainset.keySet().iterator();
		for(int step=0;step<N;step++){
			while(user_it.hasNext()){
				int user=user_it.next();
				HashMap<Integer, Integer> samples=RandomSelectNegativeSample(user);
				for(int item :samples.keySet()){
					double eui=samples.get(item)-Predict(user,item);
					for(int f=0;f<F+1;f++){
						p[user][f]+=alpha*(eui*q[item][f]-lambda*p[user][f]);
						q[item][f]+=alpha*(eui*p[user][f]-lambda*q[item][f]);
					}
				}
			}
			alpha *= 0.9;
		}
		System.out.println("模型训练完毕");
		
	}
	//初始化模型
	public void InitModel(){
		Random r=new Random();
		p=new double[6041][F+1];//有6040个用户
		q=new double[3953][F+1];

		for(int i=0;i<6041;i++){
			for(int j=0;j<F+1;j++){
				p[i][j]=r.nextDouble()/Math.sqrt(F);
			}
		}
		for(int i=0;i<3953;i++){
			for(int j=0;j<F+1;j++){
				q[i][j]=r.nextDouble()/Math.sqrt(F);				
			}
		}
		System.out.println("初始化模型完毕");
	}
	
	//预测
	public double Predict(int user,int item){
		double sum=0.0;
		for(int i=0;i<F+1;i++){
			sum+=p[user][i]
			             *q[item][i];
		}
		return sum;
	}	

	public void recommend(int user){
		recommendedMoviesList=new ArrayList<Rank>();	
		Set<Integer> watched_movies=trainset.get(user);				
		for(int i : movie_popular.keySet()){
			
			if(watched_movies.contains(i)) continue;
			
			double simularity=0.0;
			for(int f=0;f<F+1;f++){
				simularity+=p[user][f]*q[i][f];
			}
			Rank r=new Rank();
			r.setMovie(i);
			r.setSum_simlatrity(simularity);
			
			recommendedMoviesList.add(r);
		}
		if(recommendedMoviesList.size()>n){
		Heapsort ss=new Heapsort();
		ss.sort(recommendedMoviesList, n);
		}
		
		
	}	
	public void evaluate(){
		System.out.println("开始推荐");
		int rec_count=0;
		int test_count=0;
		int hit=0;
		double popularSum=0;
		Set<Integer> all_rec_movies=new HashSet<Integer>();
		Iterator<Integer> it=trainset.keySet().iterator();
		while(it.hasNext()){
			int user=it.next();
			if(user%500==0)
				System.out.println("已经推荐了"+user+"个用户");
			
			Set<Integer> test_movies=testset.get(user);
			recommend(user);
			
			if(recommendedMoviesList!=null&&test_movies!=null){
				if(recommendedMoviesList.size()<n) 
					n=recommendedMoviesList.size();
			for(int i=0;i<n;i++){
				Rank rec_movie=recommendedMoviesList.get(i);
				if(test_movies.contains(rec_movie.getMovie())){
					hit++;
				}
				all_rec_movies.add(rec_movie.getMovie());
				popularSum+=Math.log(1+movie_popular.get(rec_movie.getMovie()));

			}
			
			rec_count+=n;
			test_count+=test_movies.size();
			}
			
		}
		
		double precision=hit/(1.0*rec_count);
		double recall=hit/(1.0*test_count);
		double coverage=all_rec_movies.size()/(1.0*movie_count);
		double popularity=popularSum/(1.0*rec_count);
		System.out.println("precision=%"+precision*100+"\trecall=%"+recall*100+"\tcoverage=%"+coverage*100+"\tpopularity="+popularity);
		
	}
	public static void main(String[] args) throws IOException {
		LFM ss=new LFM();
		ss.generate_dataset(3);
		ss.calc_user_sim();
		ss.LatentFactorModel();
		ss.evaluate();
    }
}
