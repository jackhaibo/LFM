package lfm;

public class Ralated_user implements Comparable<Ralated_user>{
	private int id;
	private double simlarity=0.0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
//	double:类型名
//	Double:类名
//	类里返回的应该是一个类 而不是一个变量~~~
//	具体可以去手册Obj.compareTo(Obj);(我也是遇到这个问题了~
	public Double getSimlarity() {
		return simlarity;
	}
	public void setSimlarity(double simlarity) {
		this.simlarity = simlarity;
	}
	@Override
	public int compareTo(Ralated_user o) {
		return this.getSimlarity().compareTo(o.getSimlarity());
	}
	 

}
