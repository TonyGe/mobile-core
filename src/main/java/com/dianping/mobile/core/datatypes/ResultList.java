package com.dianping.mobile.core.datatypes;

import java.util.ArrayList;
import java.util.List;

public abstract class ResultList<LType> {

	private int recordCount = 0;
	private int startIndex;
	private boolean isEnd;
	private List<LType> list;
	private int nextStartIndex;
	private boolean judgeSetList = false;
	private boolean judgeSetStart = false;
	private String emptyMsg;
	/**
	 * used to identify the unique query;
	 */
	private String queryId = "-1";

	/* 
	 * 以下参数是给使用页码的传统分页使用的，如果显示的时候需要做传统分页，请在Service中
	 * Set这些参数
	 */
	
	/**
	 * 每页显示条目
	 * 默认为10
	 */
	private int limit = 10;
	
	/**
	 * 总页数
	 * 在setRecordCount()或者setLimit()时自动计算
	 */
	private int pageCount;
	
	/**
	 * 当前页码
	 * 在setStartIndex()或者setLimit()时自动计算
	 */
	private int page = 1;
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String nextStartIndex) {
		this.queryId = nextStartIndex;
	}

	public int getNextStartIndex() {
		return nextStartIndex;
	}

	public void setNextStartIndex(int nextStartIndex) {
		this.nextStartIndex = nextStartIndex;
	}

	protected ResultList() {
		this.recordCount = 0;
		this.list = new ArrayList<LType>();
	}

	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * FIXME: the recordCount should means the total record set size, not just
	 * current pagination query return size;
	 * 
	 * @param recordCount
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
		calPageCount();
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
		judgeSetStart = true;
		autoChangeNextStartIndex();
		calPage();
	}

	private void autoChangeNextStartIndex() {
		if (judgeSetStart && judgeSetList
				&& (this.nextStartIndex <= this.startIndex)
				&& this.list != null && this.list.size() > 0) {
			this.nextStartIndex = this.startIndex + this.list.size();
		}
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public List<LType> getList() {
		return list;
	}

	public void setList(List<LType> list) {
		this.list = list;
		this.judgeSetList = true;
		autoChangeNextStartIndex();
	}

	public String getEmptyMsg() {
		return emptyMsg;
	}

	public void setEmptyMsg(String emptyMsg) {
		this.emptyMsg = emptyMsg;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if(limit > 0){
			this.limit = limit;
			calPageCount();
			calPage();
		}
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getPage() {
		return page;
	}

	private void calPageCount(){
		if(recordCount == 0){
			pageCount = 0;
		}else{
			pageCount = (recordCount - 1) / limit + 1;
		}
	}
	
	private void calPage(){
		page = startIndex / limit + 1;
	}
}
