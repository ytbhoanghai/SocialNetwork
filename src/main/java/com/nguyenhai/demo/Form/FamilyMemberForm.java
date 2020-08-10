package com.nguyenhai.demo.Form;

import com.nguyenhai.demo.Annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberForm {

    @Required(message = "\"idFamilyRelationship\" is not allowed to be blank")
    private String idFamilyRelationship;
    @Required(message = "\"withUserId\" is not allowed to be blank")
    private String withUserId;
}
