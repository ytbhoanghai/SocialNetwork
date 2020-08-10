package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.PersonalRelationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipInfoResponse {

    private PersonalRelationship personalRelationship;
    private BasicUserInfoResponse basicInfo;
    private Date dateCreated;

}
