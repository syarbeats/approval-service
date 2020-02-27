package com.mitrais.cdc.approvalmicroservice.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalNumber {

    private long rownum;

    public ApprovalNumber(long rownum) {
        this.rownum = rownum;
    }

    public ApprovalNumber() {
    }
}
