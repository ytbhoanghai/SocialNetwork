package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.FamilyRelationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberInfoResponse {

    private FamilyRelationship familyRelationship;
    private BasicUserInfoResponse basicInfo;
    private Date dateCreated;

}
