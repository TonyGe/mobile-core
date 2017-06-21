/**
 *
 */
package com.dianping.mobile.core.datatypes;

/**
 * @author kewen.yao
 */
public class Result<FT, ST> {

    private FT first;
    private ST second;

    public Result() {

    }

    public Result(FT first, ST second) {
        this.first = first;
        this.second = second;
    }

    public FT getFirst() {
        return first;
    }

    public void setFirst(FT first) {
        this.first = first;
    }

    public ST getSecond() {
        return second;
    }

    public void setSecond(ST second) {
        this.second = second;
    }

}
