package com.nguyenhai.demo.Form;

import com.nguyenhai.demo.Annotation.DateBeforeNow;
import com.nguyenhai.demo.Annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInformationForm {

    @Required(message = "field \"firstName\" is not allowed to be blank")
    private String firstName;
    @Required(message = "field \"lastName\" is not allowed to be blank")
    private String lastName;
    @DateBeforeNow(message = "field \"birthDay\" is invalid")
    private Date birthDay;
    @NotNull(message = "field \"gender\" is not allowed to be null")
    private Boolean gender;
    @Required(message = "idCountry \"firstName\" is not allowed to be blank")
    private String idCountry;
    @Size(max = 4, message = "size of \"languages list\" should not be less than 4")
    private List<String> idLanguages;
    @Length(max = 128, message = "field \"address\" cannot contain more than 128 characters")
    private String address;
    @Length(max = 256, message = "field \"favoriteQuotes\" cannot contain more than 256 characters")
    private String favoriteQuotes;
    @Length(max = 64, message = "field \"favoriteQuotes\" cannot contain more than 64 characters")
    private String otherName;
    @Length(max = 512, message = "field \"favoriteQuotes\" cannot contain more than 512 characters")
    private String aboutMe;
}
