import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainClass implements Serializable{
	static Router routers[] = new Router[4];
	static int num_of_neighbors;
	static boolean neighbors[][];
	static int cost[][];

	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("src/info.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		cost = new int[4][4];
		neighbors = new boolean[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				cost[i][j] = Integer.MAX_VALUE;
				if (i == j) {
					cost[i][j] = 0;
				}
				neighbors[i][j] = false;
			}
			routers[i] = new Router();
			routers[i].setId(i);
		}

		routers[0].setRtr_name("queeg");
		routers[0].setIp("129.21.30.37");

		routers[1].setRtr_name("comet");
		routers[1].setIp("129.21.34.80");
		routers[2].setRtr_name("rhea");
		routers[2].setIp("129.21.37.49");
		routers[3].setRtr_name("glados");
		routers[3].setIp("129.21.22.196");

		for (int k = 0; k < 4; k++) {
			System.out.println("Enter number of neighbors" + " for "
					+ routers[k].getRtr_name() + ":");
			num_of_neighbors = Integer.parseInt(sc.nextLine());
			if (num_of_neighbors != 1 && num_of_neighbors != 2) {
				System.out.println("Number of neighbors for "
						+ "each router is limited upto two");
				System.exit(0);
			}

			for (int i = 0; i < num_of_neighbors; i++) {
				System.out.println("Enter neighbour IP:");
				String str = sc.nextLine();
				for (int j = 0; j < 4; j++) {
					if (str.equals(routers[j].getIp())) {
						neighbors[k][j] = true;
						System.out.println("Enter cost:");
						cost[k][j] = Integer.parseInt(sc.nextLine());
						System.out.println("done");
						break;
					} else if (j == 3) {
						System.out.println("Invalid IP");
					}
				}
			}

			routers[0].setNeighbors(neighbors[0]);
			routers[1].setNeighbors(neighbors[1]);
			routers[2].setNeighbors(neighbors[2]);
			routers[3].setNeighbors(neighbors[3]);
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(routers[i].getNeighbors()[j] + " ");
			}
			System.out.println();
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(cost[i][j] + " ");
			}
			System.out.println();
		}

		for (Router r : routers) {
			int[][] cur_tbl = new int[4][2];
			for (int i = 0; i < 4; i++) {
				if (i == r.getId()) {
					cur_tbl[i][0] = 0;
					cur_tbl[i][1] = r.getId();
				} else if (r.getNeighbors()[i]) {
					cur_tbl[i][0] = cost[r.getId()][i];
					cur_tbl[i][1] = r.getId();
				} else {
					cur_tbl[i][0] = Integer.MAX_VALUE;
					cur_tbl[i][1] = Integer.MAX_VALUE;
				}
			}
			r.setRouting_tbl(cur_tbl);

		}
		Packet p = new Packet();
		
		int x = 0;
		while (x != 2) {
			x++;
			for (Router r : routers) {
				for (int j = 0; j < 4; j++) {
					if (r.getNeighbors()[j]) {
						 System.out.println(r.getId() + "****" + j);
						try {
							Socket client = new Socket(r.getIp(), 22);
							OutputStream outToServer = client.getOutputStream();
							ObjectOutputStream out = new ObjectOutputStream(
									outToServer);
							p.setR1(r);
							p.setR2(routers[j]);
							p.setId1(r.getId());
							p.setId2(j);
							out.writeObject(p);
							InputStream inFromServer = client.getInputStream();
							ObjectInputStream in = new ObjectInputStream(
									inFromServer);
							int [][] result = (int[][]) in.readObject();
							r.setRouting_tbl(result);
							client.close();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						updateRoute(r, routers[j], r.getId(), j);
					}
				}
			}

			for (Router r : routers) {
				System.out.println(r.getRtr_name() + ":");
				for (int i = 0; i < 4; i++) {
					System.out.print(r.getRouting_tbl()[i][0]);
					System.out.print("===");
					System.out.print(r.getRouting_tbl()[i][1] + " ");
				}
				System.out.println();
			}
		}

	}

	private static void updateRoute(Router r1, Router r2, int id1, int id2) {
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
