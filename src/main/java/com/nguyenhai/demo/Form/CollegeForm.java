package com.nguyenhai.demo.Form;

import com.nguyenhai.demo.Annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeForm {

    @Required(message = "\"idCollege\" is not allowed to be blank")
    private String idCollege;
    @NotNull(message = "\"dateStart\" is not allowed to be null")
    private Date dateStart;
    @NotNull(message = "\"dateEnd\" is not allowed to be null")
    private Date dateEnd;
}
