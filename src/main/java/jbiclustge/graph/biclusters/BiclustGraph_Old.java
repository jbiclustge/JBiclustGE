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
package jbiclustge.graph.biclusters;

import java.util.ArrayList;
import java.util.Map;

import com.koloboke.collect.map.hash.HashLongObjMaps;
import com.koloboke.collect.map.hash.HashObjLongMaps;
import com.koloboke.collect.set.hash.HashObjSet;
import com.koloboke.collect.set.hash.HashObjSets;

import grph.in_memory.InMemoryGrph;
import jbiclustge.graph.components.NameProperty;

public abstract class BiclustGraph_Old extends InMemoryGrph{
	
	
	private Map<String, Long> vertexname2idmap;
	private Map<Long, String> vertexid2namemap;
	private Map<String, Long> edgename2idmap;
	private Map<Long, String> edgeid2namemap;
	private HashObjSet<String> edgestore;
	
	private NameProperty vertexnames;
	private NameProperty edgenames;
	private String name="";
	
	
	public BiclustGraph_Old(String name, DIRECTION navigation){
		super(name, true, navigation);
		this.name=name;
		this.vertexname2idmap=HashObjLongMaps.newMutableMap();
		this.vertexid2namemap=HashLongObjMaps.newMutableMap();
		this.edgeid2namemap=HashLongObjMaps.newMutableMap();
		this.edgename2idmap=HashObjLongMaps.newMutableMap();
		this.vertexnames=new NameProperty(name+".vertex labels");
		this.edgenames=new NameProperty(name+".edge labels");
		this.edgestore=HashObjSets.newMutableSet();
	}
	
	public BiclustGraph_Old(String name){
		this(name,DIRECTION.in_out);
	}
	
	public String getName() {
		return name;
	}
	
	
	public void addVertex(String name){
		if(!vertexname2idmap.containsKey(name)){
			Long pos=super.getNextVertexAvailable();
			vertexname2idmap.put(name, pos);
			vertexid2namemap.put(pos, name);
			super.addVertex(pos);
			vertexnames.setValue(pos, name);
		}
	}
	
	public void addVertexList(ArrayList<String> names){
		for (int i = 0; i <names.size(); i++) {
			addVertex(names.get(i));
		}
	}
	
	public Long getVertexId(String name){
		if(vertexname2idmap.containsKey(name))
			return vertexname2idmap.get(name);
		return null;
	}
	
	@Override
	public void removeVertex(long id){
		
		if(vertexid2namemap.containsKey(id)){
			String name=vertexid2namemap.get(id);
			vertexid2namemap.remove(id);
			vertexname2idmap.remove(name);
			vertexnames.unset(id);
			super.removeVertex(id);
		}
	}
	
	public NameProperty getVertexNames(){
		return vertexnames;
	}
	
	public NameProperty getEdgeNames(){
		return edgenames;
	}
	
	public void removeVertex(String name){
		if(vertexname2idmap.containsKey(name)){
			long id=vertexname2idmap.get(name);
			removeVertex(id);
		}
	}
	
	/*public void addUndirectedEdge(String vertex1, String vertex2){
		if(!vertexname2idmap.containsKey(vertex1))
			addVertex(vertex1);
		if(!vertexname2idmap.containsKey(vertex2))
			addVertex(vertex2);
	
		if(!containsUndirectedEdge(vertex1, vertex2)){
			Long vertex1id=vertexname2idmap.get(vertex1);
			Long vertex2id=vertexname2idmap.get(vertex2);
			Long vertex1id=getVertexId(vertex1);
			Long vertex2id=getVertexId(vertex2);
			if(vertex1id!=null && vertex2id!=null){
				System.out.println("adding edge: "+vertex1id+"<->"+vertex2id);
				addUndirectedSimpleEdge(vertex1id, vertex2id);
				long n=getNumberOfUndirectedEdges();
				String name=vertex1+"_"+vertex2;
				edgename2idmap.put(name, n);
				edgeid2namemap.put(n, name);
				edgenames.setValue(n, name);
			}
		}
	}*/
	
	public void addUndirectedEdge(String vertex1, String vertex2){
		if(!vertexname2idmap.containsKey(vertex1))
			addVertex(vertex1);
		if(!vertexname2idmap.containsKey(vertex2))
			addVertex(vertex2);
		
		Long vertex1id=vertexname2idmap.get(vertex1);
		Long vertex2id=vertexname2idmap.get(vertex2);
		
		String pseudohash=null;
		if(vertex1id>vertex2id)
			pseudohash=vertex1id+"_"+vertex2id;
		else
			pseudohash=vertex2id+"_"+vertex1id;
		
		if(!edgestore.contains(pseudohash)){
			addUndirectedSimpleEdge(vertex1id, vertex2id);
			long n=getNumberOfUndirectedEdges();
			String name=vertex1+"_"+vertex2;
			edgename2idmap.put(name, n);
			edgeid2namemap.put(n, name);
			//edgenames.setValue(n, hash);

			edgestore.add(pseudohash);	
		}
	
	}
	
	
	@Override
	public void removeEdge(long u, long v){
		if(vertexid2namemap.containsKey(u) && vertexid2namemap.containsKey(v)){
			String gene1=vertexid2namemap.get(u);
			String gene2=vertexid2namemap.get(v);
			String edgename=getUndirectedEdgeName(gene1, gene2);
			if(edgename!=null){
				long pos=edgename2idmap.get(edgename);
				edgeid2namemap.remove(pos);
				edgename2idmap.remove(edgename);
				edgenames.unset(pos);
				super.removeEdge(u, v);
			}
		}
	}
	
	
	
	/*public boolean containsUndirectedEdge(String vertex1, String vertex2){
		String name=vertex1+"_"+vertex2;
		if(edgename2idmap.containsKey(name))
			return true;
		else{
			name=vertex2+"_"+vertex1;
			if(edgename2idmap.containsKey(name))
				return true;
			else
				return false;
		}
	}*/
	
	public boolean containsUndirectedEdge(String vertex1, String vertex2){
		String name=vertex1+"_"+vertex2;
		String nameinv=vertex2+"_"+vertex1;
		if(edgename2idmap.containsKey(name) || edgename2idmap.containsKey(nameinv))
			return true;
        return false;
	}
	
	public String getUndirectedEdgeName(String vertex1, String vertex2){
		if(containsUndirectedEdge(vertex1, vertex2)){
			String name=vertex1+"_"+vertex2;
			if(edgename2idmap.containsKey(name))
				return name;
			else
				return vertex2+"_"+vertex1;
		}
		return null;
	}
	
	

}
