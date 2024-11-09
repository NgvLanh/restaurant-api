package org.edu.restaurantapi.request;

import lombok.Data;

@Data
public class BranchRequest {

    private String name;
    private String phoneNumber;
    private String address;
    private String wardName;
    private String wardId;
    private String districtName;
    private String districtId;
    private String provinceName;
    private String provinceId;
    private Long branchStatus;
}