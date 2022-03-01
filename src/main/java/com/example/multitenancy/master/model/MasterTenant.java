package com.example.multitenancy.master.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Xutingzhou
 */
@Getter
@Setter
@Entity
@Table(name = "MASTER_TENANT")
public class MasterTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tenant;
    private String url;
    private String username;
    private String password;
    @Version
    private int version = 0;
}
