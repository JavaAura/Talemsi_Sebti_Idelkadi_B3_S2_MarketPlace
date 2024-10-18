package com.market.marketplace.dao;

import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Client;

import java.util.List;

public interface AdminDao {
     List<Admin> findAllAdmins();
     List<Admin> findSuperAdmins();
     List<Client> findAllClients();
     void assignRole(int userId, int niveauAcces);
     void addAdminNormal(Admin admin);
     void updateAdminNormal(Admin admin);
     void deleteAdminNormal(int adminId);
     void addSuperAdmin(Admin admin);
     void updateSuperAdmin(Admin admin);
     void deleteSuperAdmin(int adminId);
     Admin getAdminInfoByEmail(String email);
     void deleteClientById(int clientId);


}
