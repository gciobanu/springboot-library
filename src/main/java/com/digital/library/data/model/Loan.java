package com.digital.library.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loan")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bookId", referencedColumnName = "id")
    private Book book;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usermemberId", referencedColumnName = "id")
    private UserMember usermember;

    @NonNull
    //@DateTimeFormat(pattern="dd-MM-yyyy")
    private LocalDate loanDate;
    //@DateTimeFormat(pattern="dd-MM-yyyy")
    private LocalDate returnDate;

}
