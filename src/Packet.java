import java.io.Serializable;


public class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	private Router r1 = new Router();
	private Router r2 = new Router();
	private int id1;
	private int id2;
	public Router getR1() {
		return r1;
	}
	public void setR1(Router r1) {
		this.r1 = r1;
	}
	public Router getR2() {
		return r2;
	}
	public void setR2(Router r2) {
		this.r2 = r2;
	}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public int getId2() {
		return id2;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	
}
