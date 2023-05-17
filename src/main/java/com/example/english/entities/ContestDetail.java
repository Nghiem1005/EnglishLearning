package com.example.english.entities;

import com.example.english.entities.keys.ContestDetailKey;
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
@Table(name = "tbl_contest_detail")
@IdClass(ContestDetailKey.class)
public class ContestDetail {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "part_id")
  private Part part;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "contest_id")
  private Contest contest;

  private int serial = 1;
}
