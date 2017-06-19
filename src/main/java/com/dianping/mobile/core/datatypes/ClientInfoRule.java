/**
 * 
 */
package com.dianping.mobile.core.datatypes;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.core.util.VersionUtil;

/**
 * @author kewen.yao
 *
 */
public class ClientInfoRule {
	
	private ClientType clientType;
	private String maxVersion;
	private String minVersion;
	
	public ClientInfoRule(ClientType clientType, String version, boolean max) {
		this.clientType = clientType;
		if(max) {
			this.maxVersion = version;
		} else {
			this.minVersion = version;
		}
	}
	
	public ClientInfoRule(ClientType clientType, String minVersion) {
		this.clientType = clientType;
		
	}
	
	public ClientInfoRule(ClientType clientType, String maxVersion, String minVersion) {
		this.clientType = clientType;
		this.maxVersion = maxVersion;
		this.minVersion = minVersion;
	}
	
	public ClientType getClientType() {
		return clientType;
	}
	
	public String getMinVersion() {
		return minVersion;
	}
	
	public String getMaxVersion() {
		return maxVersion;
	}
	
	/**
	 * or 关系 只有有一条符合 则通过
	 * @param rules
	 * @param clientType
	 * @param version
	 * @return
	 */
	public static boolean validateOr(List<ClientInfoRule> rules, ClientType clientType, String version) {
		if(clientType == null || StringUtils.isBlank(version))
			throw new IllegalArgumentException("invalide input client==null || version is empty");
		if(rules != null && rules.size() > 0) {
			boolean pass = false;
			for(ClientInfoRule rule : rules) {
				if(ClientInfoRule.validate(rule, clientType, version)) {
					pass = true;
					break;
				}
			}
			return pass;
		}
		return true;
	}
	
	public static boolean validate(ClientInfoRule rule, ClientType clientType, String version) {
		if(clientType == null || StringUtils.isBlank(version))
			throw new IllegalArgumentException("invalide input client==null || version is empty");
		if(rule != null) {
			boolean clientCheck = false;
			boolean maxVersionCheck = false;
			boolean minVersionCheck = false;
			
			if(rule.getClientType().getValue() == clientType.getValue()) {
				clientCheck = true;
			}
			
			if(rule.getMaxVersion() == null || VersionUtil.compare(version, rule.getMaxVersion()) <= 0) {
				maxVersionCheck = true;
			}
			
			if(rule.getMinVersion() == null || VersionUtil.compare(version, rule.getMinVersion()) >= 0) {
				minVersionCheck = true;
			}
			
			return clientCheck && maxVersionCheck && minVersionCheck;
		}
		return true;	
	}
}
