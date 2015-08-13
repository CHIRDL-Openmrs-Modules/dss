package org.openmrs.module.dss.util;

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
public class Tree {
	
	private Node root = null;
	
	public Tree(){
		
	}
	
	public Tree(Node root) {
		this.root = root;
	}
	
	/**
	 * @return the root
	 */
	public Node getRoot() {
		return root;
	}
	
	/**
	 * @param root the root to set
	 */
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public String toString() {
		return this.root.toString();
	}
}
