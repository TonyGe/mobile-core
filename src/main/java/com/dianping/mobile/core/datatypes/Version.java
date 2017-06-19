package com.dianping.mobile.core.datatypes;

import org.apache.commons.lang.StringUtils;

public class Version {
	public Version(String version) {	
		boolean isValid = true;
		if(!StringUtils.isBlank(version)) {
			String[] s = version.split("\\.");
			int[] versions = new int[s.length];
			for(int i=0; i< s.length; i++) {
			int num = Integer.parseInt(s[i]);
				if(num < 0 ) {
					isValid = false;
				} else {
					versions[i] = num;
				}
			}
			if(isValid) {
				this.version = version;
				this.versionNum = versions;
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	 
	public String getVersionStr() {
		return this.version;
	}
	


	public int[] getVersionNum() {
		return versionNum;
	}


	/** return 1,versionA > versionB
	 *  return 0,versionA = versionB
	 *  return -1,versionA < versionB	  	
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	@Deprecated
	public static int compare(String versionA, String versionB) {
		if (StringUtils.isBlank(versionA)) {
			return -1;
		}
		else if (StringUtils.isBlank(versionB)) {
			return 1;
		}
		return compare(new Version(versionA), new Version(versionB));	
	}
	
	/** 
	 * 
	 * return 1,versionA > versionB
	 * return 0,versionA = versionB
	 * return -1,versionA < versionB	 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	@Deprecated
	public static int compare(Version versionA, Version versionB) {
		if (versionA == null) {
			return -1;
		} else if (versionB == null) {
			return 1;
		}
		int[] versionASplit = versionA.getVersionNum();
		int[] versionBSplit = versionB.getVersionNum();
		
		if(versionASplit.length != versionBSplit.length) {
			if(versionASplit.length > versionBSplit.length) {
				for(int l = 0; l < versionBSplit.length; l++) {
					if(versionASplit[l] > versionBSplit[l]) {
						return 1;
					} else if(versionASplit[l] < versionBSplit[l]) {
						return -1;
					} else {
						continue;
					}
				}
				for(int i = versionASplit.length - 1; i >= versionBSplit.length; i--) {
					if(versionASplit[i] != 0 ) {
						return 1;
					}
				}
				return 0;
			} else {
				for(int h = 0; h < versionASplit.length; h++) {
					if(versionASplit[h] > versionBSplit[h]) {
						return 1;
					} else if(versionASplit[h] < versionBSplit[h]) {
						return -1;
					} else {
						continue;
					}
				}
				for(int j = versionBSplit.length - 1; j >= versionASplit.length; j--) {
					if(versionBSplit[j] != 0) {
						return -1;
					}
				}
				return 0;
			}
		} else {
			for(int k = 0; k < versionASplit.length; k++) {
				if(versionASplit[k] > versionBSplit[k]) {
					return 1;
				} else if(versionASplit[k] < versionBSplit[k]) {
					return -1;
				} else {
					continue;
				}
			}
			return 0;
		}		
	}
	
	private String version;  //major version + "."+minor version +"." +build + ...
	private int[] versionNum;
}
