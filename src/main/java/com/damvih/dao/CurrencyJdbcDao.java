package com.damvih.dao;

import com.damvih.entities.Currency;
import com.damvih.exceptions.DatabaseOperationException;
import com.damvih.exceptions.AlreadyExistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyJdbcDao extends JdbcDao<Currency> {

    @Override
    public Currency create(Currency entity) {
        String sqlQuery = "INSERT INTO currencies (code, name, sign) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getSign());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            Long id = resultSet.getLong(1);
            entity.setId(id);
            return entity;
        } catch (SQLException exception) {
            if (isUniqueError(exception)) {
                throw new AlreadyExistsException("Currency already exists");
            }
            throw new DatabaseOperationException();
        }
    }

    @Override
    public List<Currency> findAll() {
        String sqlQuery = "SELECT * FROM currencies";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException exception) {
            throw new DatabaseOperationException();
        }
    }

    public Optional<Currency> findByCode(String code) {
        String sqlQuery = "SELECT * FROM currencies WHERE code = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new DatabaseOperationException();
        }
    }

    private Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("name"),
                resultSet.getString("sign")
        );
    }

}
