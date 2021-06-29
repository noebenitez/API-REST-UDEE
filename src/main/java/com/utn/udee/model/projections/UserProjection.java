package com.utn.udee.model.projections;


public interface UserProjection {

    String getDni();
    void setDni(String dni);
    String getFirstname();
    void setFirstname(String firstname);
    String getLastname();
    void setLastname(String lastname);
    String getUsername();
    void setUsername(String username);
    Float getSum();
    void setSum(Float sum);

}
