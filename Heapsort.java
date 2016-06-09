package lfm;

import java.util.List;
import java.util.ListIterator;


public class Heapsort {


	//<T extends Comparable<? super T>>
	public <T> void sort(List<T> list,int k) {
		Object[] a = list.toArray();
		sort(a,k);
		ListIterator<T> i = list.listIterator();
//		for (int j=0; j<a.length; j++) {
//		    i.next();
//		    i.set((T)a[j]);
//		}
		for(int j=a.length-1;j>=0;j--){
			i.next();
			i.set((T)a[j]);
		}
		
	    }
	
	
	
	public void sort(Object[] a,int k) {
        //Object[] aux = (Object[])a.clone();
        buildMaxHeapify(a);
        heapSort(a,k);
        //(aux, a, 0, a.length, 0)
    }
	
	
	private void heapSort(Object[] a, int k) {
		for(int i=a.length-1;i>=(a.length-k);i--){
			Object temp=a[0];
			a[0]=a[i];
			a[i]=temp;
			max_heapify(a,0,i);
			}
		
	}


	public void buildMaxHeapify(Object[] a){
		for(int j=(a.length>>1)-1;j>=0;j--){
			max_heapify(a,j,a.length);
		}
	}
//	//先建好堆
//	buildMaxHeapify(recommendedMovies);
//	//堆排序
//	N_movies=new ArrayList<Object>();
//	//获取前K个user的相似度
//	for(int count=1;count<=n;count++){
//		N_movies.add( recommendedMovies.remove(0));
//		recommendedMovies.set(0, recommendedMovies.remove(recommendedMovies.size()-1));
//		max_heapify(recommendedMovies,recommendedMovies.size()/2-1,recommendedMovies.size());
//	}
	public void max_heapify(Object[] a,int index,int heapSize){
		//当前点与左右子节点比较
		int left=(index<<1)+1;
		int right=(index<<1)+2;
		 
		int largest=index;
		
//		ralated_user ul=null;
//		ralated_user ur=null;
//		ralated_user ui = (ralated_user) a.get(index);
			
		
//		if(left<heapSize){
////			ul= (ralated_user) a.get(left);
//			if(ul.getSimlarity()>ui.getSimlarity()){
//				largest=left;
//			}
//			
//		}
//		if(left<heapSize&&((Comparable)a[left]).compareTo(a[index])>0){
//			largest=left;
//		}
        if(left<heapSize&& ((Comparable<Object>) a[left]).compareTo(a[index])>0){
        	largest=left;
		}
        if(right<heapSize&&((Comparable<Object>) a[right]).compareTo(a[largest])>0){
        	largest=right;
        }
//		(Comparable) dest[j-1]).compareTo(dest[j])>0; j--)

		
		//得到最大值后可能需要交换，如果交换了，其子节点可能就不是最大堆了，需要重新调整
		if(largest!=index){
			//交换
			Object object=a[index];
			a[index]=a[largest];
			a[largest]=object;
			max_heapify(a,largest,heapSize);
		}
		
		
	}


}
