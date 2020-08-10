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
public class RelationshipForm {

    @Required(message = "\"idPersonalRelationship\" is not allowed to be blank")
    private String idPersonalRelationship;
    private String withUserId;

}
