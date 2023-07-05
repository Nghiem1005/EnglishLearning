package com.example.english.entities;

import com.example.english.entities.keys.DiscountDetailKey;
import com.example.english.entities.keys.PracticeDetailKey;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_discount_course")
@IdClass(DiscountDetailKey.class)
public class DiscountDetail {
  @Id
  @ManyToOne
  @JoinColumn(name = "discount_id")
  private Discount discount;

  @Id
  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

}
