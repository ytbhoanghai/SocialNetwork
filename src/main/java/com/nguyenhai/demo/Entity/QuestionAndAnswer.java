package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@TypeAlias("questionAndAnswer")
public class QuestionAndAnswer {

    @Id
    private String id;
    private String question;
    private String answer;

}
