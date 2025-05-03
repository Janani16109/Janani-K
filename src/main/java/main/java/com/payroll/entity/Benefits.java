package main.java.com.payroll.entity;

import lombok.Data;

import javax.persistence.*;

import com.payroll.entity.Employee;

@Data
@Entity
@Table(name = "benefits")
public class Benefits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String healthInsurance;
    private String providentFund;
    private String gratuity;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
