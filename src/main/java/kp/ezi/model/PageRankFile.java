package kp.ezi.model;

import ir.utilities.MoreMath;
import ir.webutils.Graph;
import ir.webutils.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PageRankFile {
	private boolean debugMode;

	public PageRankFile(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public void runAndSave(Graph graph, double alpha, int iterations, File saveDir) {
		double[] pageRank = run(graph, alpha, iterations);
		print(getPageRankString(graph, pageRank, false), saveDir);
		System.out.println(getPageRankString(graph, pageRank, true));
	}

	private String getPageRankString(Graph graph, double[] pageRank, boolean withLabel) {
		StringBuilder sb = new StringBuilder();
		graph.resetIterator();
		Node node = graph.nextNode();
		int i = 0;
		while (node != null) {
			if (withLabel) {
				sb.append("PR(");
			}
			sb.append(node.toString());
			if (withLabel) {
				sb.append(")");
			}
			sb.append(" ");
			sb.append(pageRank[i++]);
			sb.append("\n");
			node = graph.nextNode();
		}
		return sb.toString();
	}

	public double[] run(Graph graph, double alpha, int iterations) {
		Node[] nodes = graph.nodeArray();
		HashMap<String, Integer> indexMap = new HashMap<>();
		double[] r = new double[nodes.length];
		double[] rp = new double[nodes.length];
		double[] e = new double[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			indexMap.put(nodes[i].toString(), i);
			System.out.print(nodes[i] + " ");
			r[i] = 1.0 / nodes.length;
			e[i] = alpha / nodes.length;
		}
		if (debugMode) {
			System.out.print("\nR = ");
			MoreMath.printVector(r);
			System.out.print("\nE = ");
			MoreMath.printVector(e);
		}
		for (int j = 0; j < iterations; j++) {
			if (debugMode) {
				System.out.println("\nIteration " + (j + 1) + ":");
			}
			for (int i = 0; i < nodes.length; i++) {
				ArrayList inNodes = nodes[i].getEdgesIn();
				rp[i] = 0;
				for (Object inNode1 : inNodes) {
					Node inNode = (Node) inNode1;
					String inName = inNode.toString();
					int fanOut = inNode.getEdgesOut().size();
					rp[i] = rp[i] + r[indexMap.get(inName)] / fanOut;
				}
				rp[i] = rp[i] + e[i];
			}
			if (debugMode) {
				System.out.println("R' = ");
				MoreMath.printVector(rp);
			}
			System.arraycopy(rp, 0, r, 0, r.length);
			normalize(r);
			if (debugMode) {
				System.out.println("\nNorm R = ");
				MoreMath.printVector(r);
				System.out.println("");
			}
		}
		return r;
	}

	private void normalize(double[] x) {
		double length = MoreMath.vectorOneNorm(x);
		for (int i = 0; i < x.length; i++)
			x[i] = x[i] / length;
	}

	private void print(String sb, File saveDir) {
		try {

			File file = new File(saveDir.getPath() + "/pageRanks.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
