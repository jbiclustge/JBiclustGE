/************************************************************************** 
 * Copyright 2015 - 2017
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 *  
 * Created by Orlando Rocha (ornrocha@gmail.com) inside BIOSYSTEMS Group (https://www.ceb.uminho.pt/BIOSYSTEMS)
 */
package jbiclustge.graph.io.graphml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FilenameUtils;

import jbiclustge.graph.biclusters.BiclustersGraph;
import jbiclustge.graph.components.EdgeConnectionType;
import toools.text.xml.TextNode;
import toools.text.xml.XMLNode;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractBiclustGraphMLWriter.
 */
public abstract class AbstractBiclustGraphMLWriter {
	
	/** The graph. */
	protected BiclustersGraph graph;
	
	/** The edgetype. */
	protected EdgeConnectionType edgetype=EdgeConnectionType.UNDIRECTED;

	
	/**
	 * Instantiates a new abstract biclust graph ML writer.
	 *
	 * @param graph the graph
	 */
	public AbstractBiclustGraphMLWriter(BiclustersGraph graph){
		this.graph=graph;
	}
	
	/**
	 * Adds the auxiliar attributes.
	 *
	 * @param graphmlNode the graphml node
	 */
	protected abstract void addAuxiliarAttributes(XMLNode graphmlNode);
	
	/**
	 * Adds the vertex information.
	 *
	 * @param graphNode the graph node
	 */
	protected abstract void addVertexInformation(XMLNode graphNode);
	
	/**
	 * Adds the edge information.
	 *
	 * @param graphNode the graph node
	 */
	protected abstract void addEdgeInformation(XMLNode graphNode);
	
	
	
	/**
	 * Builds the graph ML.
	 *
	 * @return the string
	 */
	public String buildGraphML(){
		String output=null;
		if(!graph.isHypergraph()){
			
			XMLNode graphmlNode = new XMLNode("graphml");
			graphmlNode.getAttributes().put("xmlns", "http://graphml.graphdrawing.org/xmlns");
			
			addAuxiliarAttributes(graphmlNode);
			
			
			XMLNode graphNode = new XMLNode("graph");
			graphNode.setParent(graphmlNode);
			graphNode.getAttributes().put("id", "" + graph.getGraphName());
			graphNode.getAttributes().put("edgedefault", edgetype.toString());
			
			addVertexInformation(graphNode);
			addEdgeInformation(graphNode);
			
			output=graphmlNode.toString('\t');
			
		}
			
		
		return output;	
	}
	
	/**
	 * Write.
	 *
	 * @param pw the pw
	 */
	public void write(PrintWriter pw){
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		pw.println(buildGraphML());
		pw.close();
	}
	
	/**
	 * Write.
	 *
	 * @param w the w
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void write(FileWriter w) throws IOException{
		PrintWriter pw=new PrintWriter(w);
		write(pw);
		w.close();
	}
	

	/**
	 * Export to graph ML.
	 *
	 * @param filepath the filepath
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void exportToGraphML(String filepath) throws IOException{
		
		String ext=FilenameUtils.getExtension(filepath);
		
		if(ext!=null && !ext.isEmpty() && !ext.toLowerCase().equals("graphml")){
			String filename=FilenameUtils.getBaseName(filepath);
			String path=FilenameUtils.getFullPath(filepath);
			filepath=path+filename+".graphml";
		}
		else{
			if(ext==null || ext.isEmpty()){
				filepath=filepath+".graphml";
			}
		}
		
		FileWriter w=new FileWriter(filepath);
		write(w);
	}
	
	/**
	 * Adds the node attribute.
	 *
	 * @param parentnode the parentnode
	 * @param nodename the nodename
	 * @param attributekey the attributekey
	 * @param attributevalue the attributevalue
	 * @param attributetext the attributetext
	 */
	public static void addNodeAttribute(XMLNode parentnode, String nodename, String attributekey, String attributevalue, String attributetext){
	    	XMLNode node=new XMLNode(nodename);
	    	node.setParent(parentnode);
	    	node.getAttributes().put(attributekey, attributevalue);
	    	if(attributetext!=null && !attributetext.isEmpty()){
	    		TextNode te = new TextNode();
	    		te.setText(attributetext);
	    		te.setParent(node);
	    	}
	    	
	}
	
	/**
	 * Adds the node attributes.
	 *
	 * @param parentnode the parentnode
	 * @param nodename the nodename
	 * @param keys the keys
	 * @param values the values
	 * @throws Exception the exception
	 */
	public static void addNodeAttributes(XMLNode parentnode, String nodename,String[] keys, String[] values) throws Exception{
		if(keys.length==values.length){
			XMLNode node=new XMLNode(nodename);
		    node.setParent(parentnode);
			for (int i = 0; i < keys.length; i++) {
				node.getAttributes().put(keys[i], values[i]);
			}
		}
		else
			throw new Exception("Attribute keys must have the same size of its values");
	}

}
