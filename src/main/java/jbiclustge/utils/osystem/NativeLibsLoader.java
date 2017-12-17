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
package jbiclustge.utils.osystem;

import java.lang.reflect.Field;

// TODO: Auto-generated Javadoc
/**
 * The Class NativeLibsLoader.
 */
public final class NativeLibsLoader {

	
	/** The Constant JAVA_LIBRARY_PATH. */
	private static final String JAVA_LIBRARY_PATH = "java.library.path";
	
	/** The Constant SYS_PATHS. */
	private static final String SYS_PATHS = "sys_paths";

	/**
	 * Instantiates a new native libs loader.
	 */
	private NativeLibsLoader() {
	}

	/**
	 * Adds the libs to java library path.
	 *
	 * @param tmpDirName the tmp dir name
	 */
	public static void addLibsToJavaLibraryPath(final String tmpDirName) {
	    try {
	        System.setProperty(JAVA_LIBRARY_PATH, tmpDirName);
	        /* Optionally add these two lines */
	        System.setProperty("jna.library.path", tmpDirName);
	        System.setProperty("jni.library.path", tmpDirName);
	        final Field fieldSysPath = ClassLoader.class.getDeclaredField(SYS_PATHS);
	        fieldSysPath.setAccessible(true);
	        fieldSysPath.set(null, null);
	    } catch (IllegalAccessException | NoSuchFieldException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
}
