import java.net.*;
import java.io.*;

public class Server extends Thread {
	private ServerSocket serverSocket;
	private Router myRtTbl;

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client on port "
						+ serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());
				ObjectInputStream in = new ObjectInputStream(
						server.getInputStream());
				Packet p = null;
				try {
					p = (Packet) in.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < 4; i++)
					System.out.println(p.getR1().getRtr_name() + "-------"
							+ p.getR2().getRtr_name() + "---------" + p.getId1() + "--------" + p.getId2());
				int result[][] = updateRoute(p.getR1(), p.getR2(), p.getId1(), p.getId2());
				ObjectOutputStream out = new ObjectOutputStream(
						server.getOutputStream());
				out.writeObject(result);
				server.close();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt("22");
		try {
			Thread t = new Server(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Router getMyRtTbl() {
		return myRtTbl;
	}

	public void setMyRtTbl(Router myRtTbl) {
		this.myRtTbl = myRtTbl;
	}
	
	private static int[][] updateRoute(Router r1, Router r2, int id1, int id2) {
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
		for (int i = 0; i < 4; i++){
			System.out.println(r1_tbl[i][0]+"========"+r1_tbl[i][1]);
		}
		return r1_tbl;
	}
}