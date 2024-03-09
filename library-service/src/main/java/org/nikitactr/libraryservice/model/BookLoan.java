package org.nikitactr.libraryservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_loans")
@NoArgsConstructor
@Getter
@Setter
public class BookLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDateTime loanTime;

    @Column(nullable = false)
    private LocalDateTime returnTime;

    public BookLoan(Book book, LocalDateTime loanTime, LocalDateTime returnTime) {
        this.book = book;
        this.loanTime = loanTime;
        this.returnTime = returnTime;
    }
}