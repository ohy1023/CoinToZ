package com.example.financialfinalproject.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.example.financialfinalproject.domain.enums.UserRole.USER;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE post SET deleted_at = current_timestamp WHERE id = ?")
@Where(clause = "deleted_at is NULL")

public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String body;

    private Long likeCount;

    private Long disLikeCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        this.likeCount = this.likeCount == null ? 0l : this.likeCount;
    }

    public void updatePost(String updatedTitle, String updatedBody) {
        this.title = updatedTitle;
        this.body = updatedBody;
    }

}
