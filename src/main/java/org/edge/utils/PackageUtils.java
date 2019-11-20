package org.edge.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageUtils {
	/**
	 * 

	 */
	public static List<Class> getClasssFromPackage(String pack) {
	  List<Class> clazzs = new ArrayList<Class>();

	  // whether recurving the sub pakcage
	  boolean recursive = true;

	  // package name
	  String packageName = pack;
	  // the path in relation to the package
	  String packageDirName = packageName.replace('.', '/');

	  Enumeration<URL> dirs;

	  try {
	    dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
	    while (dirs.hasMoreElements()) {
	      URL url = dirs.nextElement();
		
	      String protocol = url.getProtocol();

	      if ("file".equals(protocol)) {
	        System.out.println("file scanning type");
	        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
	        findClassInPackageByFile(packageName, filePath, recursive, clazzs);
	      } else if ("jar".equals(protocol)) {
	        System.out.println("jar scanning type");
	      }
	    }

	  } catch (Exception e) {
	    e.printStackTrace();
	  }

	  return clazzs;
	}

	/**
	 * find all the classes in relation the this packageName
	 * 
	 * @param packageName
	 *        
	 * @param filePath
	 *          
	 * @param recursive
	 *          
	 * @param clazzs
	 *           the list of classes found
	 */
	public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> clazzs) {
	  File dir = new File(filePath);
	  if (!dir.exists() || !dir.isDirectory()) {
	    return;
	  }
	  // filter
	  File[] dirFiles = dir.listFiles(new FileFilter() {

	    @Override
	    public boolean accept(File file) {
	      boolean acceptDir = recursive && file.isDirectory();// accept the directory
	      boolean acceptClass = file.getName().endsWith("class");// accept class file
	      return acceptDir || acceptClass;
	    }
	  });

	  for (File file : dirFiles) {
	    if (file.isDirectory()) {
	      findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
	    } else {
	      String className = file.getName().substring(0, file.getName().length() - 6);
	      try {
	        clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	  }
	}
}
