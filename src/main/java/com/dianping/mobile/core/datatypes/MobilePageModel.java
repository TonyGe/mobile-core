/**
 *
 */
package com.dianping.mobile.core.datatypes;

import java.util.List;

/**
 * @author kewen.yao
 */
public class MobilePageModel<LType> {

    private List<LType> records;
    private int start;
    private int limit;
    private boolean isEnd;
    private int totalCount;

    public List<LType> getRecords() {
        return records;
    }

    public void setRecords(List<LType> records) {
        this.records = records;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
