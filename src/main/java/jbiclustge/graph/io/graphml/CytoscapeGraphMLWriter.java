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

import java.util.HashMap;

import jbiclustge.graph.biclusters.BiclustersGraph;
import toools.collection.bigstuff.longset.LongCursor;
import toools.text.xml.XMLNode;


// TODO: Auto-generated Javadoc
/**
 * The Class CytoscapeGraphMLWriter.
 */
public class CytoscapeGraphMLWriter extends AbstractBiclustGraphMLWriter{


	/** The currentedgeid. */
	private int currentedgeid=0;
	
	/** The vertexid 2 name. */
	private HashMap<Long, String> vertexid2name=null;
	
	
	/**
	 * Instantiates a new cytoscape graph ML writer.
	 *
	 * @param graph the graph
	 */
	public CytoscapeGraphMLWriter(BiclustersGraph graph) {
		super(graph);
	}

	/* (non-Javadoc)
	 * @see graph.io.graphml.AbstractBiclustGraphMLWriter#addAuxiliarAttributes(toools.text.xml.XMLNode)
	 */
	@Override
	protected void addAuxiliarAttributes(XMLNode graphmlNode) {
	   addFixedAuxiliarAttributes(graphmlNode);
	}
	
	/**
	 * Adds the fixed auxiliar attributes.
	 *
	 * @param graphmlNode the graphml node
	 */
	private void addFixedAuxiliarAttributes(XMLNode graphmlNode){
		
		try {
			addNodeAttributes(graphmlNode,"key",new String[]{"attr.name","attr.type","for","id"},new String[]{"SUID","long","node","SUID"});
			addNodeAttributes(graphmlNode,"key",new String[]{"attr.name","attr.type","for","id"},new String[]{"shared name","string","node","shared name"});
			addNodeAttributes(graphmlNode,"key",new String[]{"attr.name","attr.type","for","id"},new String[]{"name","string","node","name"});
			addNodeAttributes(graphmlNode,"key",new String[]{"attr.name","attr.type","for","id"},new String[]{"selected","boolean","node","selected"});
			addNodeAttributes(graphmlNode,"key",new String[]{"attr.name","attr.type","for","id"},new String[]{"Human Readable Label","string","node","Human Readable Label"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/* (non-Javadoc)
	 * @see graph.io.graphml.AbstractBiclustGraphMLWriter#addVertexInformation(toools.text.xml.XMLNode)
	 */
	@Override
	protected void addVertexInformation(XMLNode graphNode) {
		
		for (LongCursor c : graph.getVertices())
		{
			long v = c.value;
			XMLNode vertexNode = new XMLNode("node");
			vertexNode.setParent(graphNode);
			vertexNode.getAttributes().put("id", "" + v);
			addVertexExtraInformation(v, vertexNode);
		}
		
	}

	/* (non-Javadoc)
	 * @see graph.io.graphml.AbstractBiclustGraphMLWriter#addEdgeInformation(toools.text.xml.XMLNode)
	 */
	@Override
	protected void addEdgeInformation(XMLNode graphNode) {
		
		currentedgeid=(int)graph.getNextVertexAvailable();
		
		for (LongCursor c : graph.getEdges())
		{
			long e = c.value;
			long v1 = graph.getOneVertex(e);
			XMLNode edgeNode = new XMLNode("edge");
			edgeNode.setParent(graphNode);
			edgeNode.getAttributes().put("source", "" + v1);
			long v2=graph.getTheOtherVertex(e, v1);
			edgeNode.getAttributes().put("target", "" + v2);
            addEdgeExtraInformation(e, v1, v2, edgeNode);
		}
		
	}
	

	
    /**
     * Adds the vertex extra information.
     *
     * @param id the id
     * @param vertexNode the vertex node
     */
    private void addVertexExtraInformation(long id, XMLNode vertexNode){

    	String humanreadablelabel=null;
    	
   
    	if(graph.getVertexLabelProperty()!=null)
    	   humanreadablelabel=graph.getVertexLabelProperty().getValueAsString(id);
       
    	if(vertexid2name!=null && vertexid2name.containsKey(id)){
    		addNodeAttribute(vertexNode, "data", "key", "SUID", vertexid2name.get(id));
    		addNodeAttribute(vertexNode, "data", "key", "shared name", vertexid2name.get(id));
    	}
    	else{
    		if(humanreadablelabel!=null){
    			addNodeAttribute(vertexNode, "data", "key", "shared name", humanreadablelabel);
    	    	addNodeAttribute(vertexNode, "data", "key", "name", humanreadablelabel);
    		}
    		else{
    			addNodeAttribute(vertexNode, "data", "key", "shared name", String.valueOf(id));
    			addNodeAttribute(vertexNode, "data", "key", "name", String.valueOf(id));
    		}
    			
    	}

    	if(humanreadablelabel!=null)
    		addNodeAttribute(vertexNode, "data", "key", "Human Readable Label", humanreadablelabel);
    	addNodeAttribute(vertexNode, "data", "key", "selected", String.valueOf(false));

    }
    
    /**
     * Adds the edge extra information.
     *
     * @param edgeid the edgeid
     * @param v1 the v 1
     * @param v2 the v 2
     * @param edgeNode the edge node
     */
    private void addEdgeExtraInformation(long edgeid, long v1, long v2,  XMLNode edgeNode){
    	
    	String name=null;
    	if(vertexid2name!=null && vertexid2name.containsKey(v1) && vertexid2name.containsKey(v2)){
    		name=vertexid2name.get(v1)+" ("+currentedgeid+") "+vertexid2name.get(v2);	
    	}
    	else if(graph.getVertexLabelProperty()!=null){
    		String v1name=graph.getVertexLabelProperty().getValueAsString(v1);
    		String v2name=graph.getVertexLabelProperty().getValueAsString(v2);
     	    name=v1name+" ("+currentedgeid+") "+v2name; 
    	}
    	else
    		name=String.valueOf(v1)+" ("+currentedgeid+") "+String.valueOf(v2);
    	
    	addNodeAttribute(edgeNode, "data", "key", "SUID", String.valueOf(currentedgeid));
    	addNodeAttribute(edgeNode, "data", "key", "shared name", name);
    	addNodeAttribute(edgeNode, "data", "key", "name", name);
    	addNodeAttribute(edgeNode, "data", "key", "selected", String.valueOf(false));
    	currentedgeid++;
    }



}
