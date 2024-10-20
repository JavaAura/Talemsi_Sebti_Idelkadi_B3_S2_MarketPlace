package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.AdminDao;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Client;
import com.market.marketplace.service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public List<Admin> findAllAdmins(int page, int size) {
        return adminDao.findAllAdmins(page, size);
    }

    @Override
    public long countAdmins() {
        return adminDao.countAdmins();
    }

    @Override
    public List<Admin> findSuperAdmins(int page, int size) {
        return adminDao.findSuperAdmins(page, size);
    }

    @Override
    public long countSuperAdmins() {
        return adminDao.countSuperAdmins();
    }

    @Override
    public List<Client> findAllClients(int page, int size) {
        return adminDao.findAllClients(page, size);
    }

    @Override
    public long countClients() {
        return adminDao.countClients();
    }

    @Override
    public void assignRole(int userId, int niveauAcces) {
        adminDao.assignRole(userId, niveauAcces);

    }

    @Override
    public void addAdminNormal(Admin admin) {
        // Set access level to 0 for normal admin
        admin.setAccessLevel(0);
        adminDao.addAdminNormal(admin);
    }

    @Override
    public void updateAdminNormal(Admin admin) {
        adminDao.updateAdminNormal(admin);
    }

    @Override
    public void deleteAdminNormal(int adminId) {
        adminDao.deleteAdminNormal(adminId);
    }

    @Override
    public void addSuperAdmin(Admin admin) {
        // Set access level to 1 for super admin
        admin.setAccessLevel(1);
        adminDao.addSuperAdmin(admin);
    }

    @Override
    public void updateSuperAdmin(Admin admin) {
        adminDao.updateSuperAdmin(admin);
    }

    @Override
    public void deleteSuperAdmin(int adminId) {
        adminDao.deleteSuperAdmin(adminId);
    }

    @Override
    public void deleteClientById(int clientId) {
        adminDao.deleteClientById(clientId);
    }

    @Override
    public List<Admin> findSuperAdminsByEmail(String email) {
        return adminDao.findSuperAdminsByEmail(email);
    }

    @Override
    public List<Admin> findAdminsByEmail(String email) {
        return adminDao.findAdminsByEmail(email);

    }

    @Override
    public List<Client> findClientsByEmail(String email) {
        return adminDao.findClientsByEmail(email);
    }

}

