package lfm;

public class Rank implements Comparable<Rank>{
	private int movie;
	private double sum_simlatrity=0.0;//设置默认值
	public int getMovie() {
		return movie;
	}
	public void setMovie(int movie) {
		this.movie = movie;
	}
	public Double getSum_simlatrity() {
		return sum_simlatrity;
	}
	public void setSum_simlatrity(double sumSimlatrity) {
		sum_simlatrity = sumSimlatrity;
	}
	//重写equals方法
	public boolean equals(Object obj) {
		return (this.getMovie() == ((Rank) obj).getMovie());
	    }
	public int compareTo(Rank o) {
		return this.getSum_simlatrity().compareTo(o.getSum_simlatrity());
	}


}
