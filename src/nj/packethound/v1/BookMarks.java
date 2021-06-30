/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nj.packethound.v1;

import java.io.Serializable;

/**
 *
 * @author nafis
 */
public class BookMarks implements Serializable{
    String filterName;
    String filterExpression;

    public BookMarks(String filterName, String filterExpression) {
        this.filterName = filterName;
        this.filterExpression = filterExpression;
    }

    public BookMarks() {
        filterName="Add Name";
        filterExpression="Add Expression";
    }
    
    

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }
    
}
