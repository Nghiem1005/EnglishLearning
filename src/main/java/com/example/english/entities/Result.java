package com.example.english.entities;

import com.example.english.entities.keys.ResultKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_result")
@IdClass(ResultKey.class)
public class Result {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "question_id")
  private Question question;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull(message = "Question choice is required")
  private int choice;

  @NotNull(message = "Correct content is required")
  private boolean correct;
}
