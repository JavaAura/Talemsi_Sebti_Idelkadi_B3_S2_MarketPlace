package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.dao.daoImpl.CommandProductDAOImpl;
import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.CommandProduct;
import com.market.marketplace.entities.Product;
import com.market.marketplace.service.CommandProductService;

import java.util.List;

public class CommandProductServiceImpl implements CommandProductService {
    private CommandProductDAO commandProductDAO ;

    public CommandProductServiceImpl(CommandProductDAO commandProductDAO) {
        this.commandProductDAO = commandProductDAO;
    }

    public void addProductToCommand(int productId, int quantity, int commandId) {
        Command command = new Command();
        command.setId(commandId);

        Product product = new Product();
        product.setId(productId);

        CommandProduct commandProduct = new CommandProduct();
        commandProduct.setCommand(command);
        commandProduct.setProduct(product);
        commandProduct.setQuantity(quantity);
        commandProduct.setValid(false);

        commandProductDAO.addCommandProduct(commandProduct);
    }

    @Override
    public List<CommandProduct> getCurrentCartForClient(int clientId) {
        return commandProductDAO.findCurrentCartForClient(clientId);
    }

}
