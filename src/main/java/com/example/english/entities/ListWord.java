package com.example.english.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_list_word")
public class ListWord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Name list word is required")
  private String name;

  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;

  @OneToMany(mappedBy = "listWord", cascade = CascadeType.ALL, orphanRemoval = true)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Word> words = new ArrayList<>();
}
