package org.openmrs.module.dss.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

/**
 *
 */
public class ParseTreeFile {
	
	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	public static void parseTree(){
		
		String treeFile = "C:\\Users\\tmdugan\\git\\Obesity_Prediction\\src\\util\\resources\\ID3_small_model.txt";
		final String DELIMITER = "|  ";
		Node root = new Node();
		Tree tree = new Tree(root);
		BufferedReader reader = null;
		int prevLevel = -1;
		Node parentNode = root;
		try {
			reader = new BufferedReader(new FileReader(treeFile));
			
			String line = reader.readLine();
			while (line != null) {
				int level = org.apache.commons.lang.StringUtils.countMatches(line, DELIMITER);
				String strippedLine = line.replaceAll(DELIMITER.replaceAll("|", "\\\\|"), "").trim();
				StringTokenizer tokenizer = new StringTokenizer(strippedLine, "=");
				String attribute = null;
				String value = null;
				
				if (tokenizer.hasMoreElements()) {
					attribute = tokenizer.nextToken().trim();
				}
				if (tokenizer.hasMoreElements()) {
					value = tokenizer.nextToken().trim();
				}
				
				Node node = new Node(attribute, value);
				
				if (level > prevLevel) {
					parentNode.addChild(node);
				} else {
					
					while (level < prevLevel && parentNode != null) {
						parentNode = parentNode.getParent();
						prevLevel--;
					}
					
					if (parentNode != null) {
						parentNode = parentNode.getParent();
						if (parentNode != null) {
							parentNode.addChild(node);
						} 
					}
				}
				
				parentNode = node;
				prevLevel = level;
				line = reader.readLine();
			}
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			}
			catch (Exception e) {}
		}
		tree.getRoot().traverseDepthFirst("associated_answer");
		
		String fileName = "C:\\Users\\tmdugan\\git\\Obesity_Prediction\\src\\util\\resources\\output_test.txt";
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.write(tree.getRoot().toString());
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
