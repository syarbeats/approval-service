package com.mitrais.cdc.approvalmicroservice.payload;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BlogNumberPerCategory {

    private long y;
    private String label;

    public BlogNumberPerCategory(long y, String label){
        this.y = y;
        this.label= label;
    }

    public BlogNumberPerCategory() {
    }
}
