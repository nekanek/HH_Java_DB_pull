/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hh.hw.javadb.employers;

import java.util.List;


public interface EmployerDAO {

    void addEmployer(Employer employer);

    List<Employer> getAllEmployers();

    Employer getEmployerById(Integer id);

    void updateEmployer(Employer employer);
    
}
