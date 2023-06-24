package com.example.english.entities;

import com.example.english.entities.enums.PartType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_part")
public class Part {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Part description is required")
  private String description;

  private int serial = 1;

  @Enumerated(EnumType.STRING)
  private PartType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exam_id")
  private Exam exam;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;
}
