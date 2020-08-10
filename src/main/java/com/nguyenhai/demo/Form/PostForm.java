package com.nguyenhai.demo.Form;

import com.nguyenhai.demo.Annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostForm {

    @Required
    private String content;
    private String idFeeling;
    private String idCheckin;
    private List<String> tagFriends;
    private List<String> photos;

}
