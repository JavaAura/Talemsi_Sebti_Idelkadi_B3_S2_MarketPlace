package com.market.marketplace.dao;

import com.market.marketplace.entities.CommandProduct;

import java.util.List;

public interface CommandProductDAO {
    void addCommandProduct(CommandProduct commandProduct);

    List<CommandProduct> findCurrentCartForClient(int clientId, int commandId);

    public void deleteByCommandId(int commandId);

    void updateCommandStatusToValid(int commandId);
}
