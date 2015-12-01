package kp.ezi.service;

import ir.webutils.Graph;
import ir.webutils.HTMLPage;
import ir.webutils.Link;
import ir.webutils.Spider;
import kp.ezi.model.PageRankFile;

import java.util.List;

public class PageRankSpider extends Spider {
	private static final double ALPHA = 0.15;
	private static final int ITERATIONS = 50;
	private Graph siteGraph = new Graph();

	@Override
	protected List getNewLinks(HTMLPage page) {
		String actualNode = page.getLink().toString();
		List links = super.getNewLinks(page);
		for (Object o : links) {
			Link link = (Link) o;
			siteGraph.addEdge(actualNode, link.toString());
		}
		return links;
	}


	public static void main(String args[]) {
		new PageRankSpider().go(args);
	}

	@Override
	public void go(String[] args) {
		super.go(args);
		siteGraph.print();

		PageRankFile pageRankFile = new PageRankFile(false);
		pageRankFile.runAndSave(siteGraph, ALPHA, ITERATIONS, saveDir);

	}


}
