public class Router {
	private int id;
	private String rtr_name;
	private String ip;
	private int[][] routing_tbl;
	private boolean neighbors[];
	
	public Router() {
		rtr_name = new String();
		ip = new String();
	}

	public int[][] getRouting_tbl() {
		return routing_tbl;
	}

	public void setRouting_tbl(int[][] routing_tbl) {
		this.routing_tbl = routing_tbl;
	}

	

	public String getRtr_name() {
		return rtr_name;
	}

	public void setRtr_name(String rtr_name) {
		this.rtr_name = rtr_name;
	}

	

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getId() {
		return id;
	}

	public void setId(int i) {
		this.id = i;
	}

	public boolean[] getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(boolean neighbors[]) {
		this.neighbors = neighbors;
	}
	
	public void updateRoute(Router r1, Router r2, int id1, int id2) {
		int r1_tbl[][] = r1.getRouting_tbl();
		int r2_tbl[][] = r2.getRouting_tbl();

		for (int i = 0; i < 4; i++) {
			if (i != r1.getId() && r2_tbl[i][0] != Integer.MAX_VALUE) {
				if (r1_tbl[i][0] > r1_tbl[id2][0] + r2_tbl[i][0]) {
					r1_tbl[i][0] = r1_tbl[id2][0] + r2_tbl[i][0];
					r1_tbl[i][1] = r2.getId();
				}
			}
		}
		r1.setRouting_tbl(r1_tbl);
	}

}
