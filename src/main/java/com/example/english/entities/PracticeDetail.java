package com.example.english.entities;

import com.example.english.entities.keys.PracticeDetailKey;
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
@Table(name = "tbl_practice_detail")
@IdClass(PracticeDetailKey.class)
public class PracticeDetail {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "part_id")
  private Part part;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "practice_id")
  private Practice practice;

  private int serial = 1;
}
